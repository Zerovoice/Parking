package com.zeroapp.parking.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import com.zeroapp.parking.message.ClientServerMessage;

public class ObjectTransferClient {
	private String host;
	private int port;
    private ClientServerMessage mMsg;

    public ObjectTransferClient(String host, int port, ClientServerMessage message) {
		this.host = host;
		this.port = port;
        this.mMsg = message;
	}

	public void run() throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)), new ObjectTransferClientHandler(mMsg));
                }
            });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}