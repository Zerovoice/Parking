/* 
 * Copyright (C) 2015 Alex. 
 * All Rights Reserved.
 *
 * ALL RIGHTS ARE RESERVED BY Alex. ACCESS TO THIS
 * SOURCE CODE IS STRICTLY RESTRICTED UNDER CONTRACT. THIS CODE IS TO
 * BE KEPT STRICTLY CONFIDENTIAL.
 *
 * UNAUTHORIZED MODIFICATION OF THIS FILE WILL VOID YOUR SUPPORT CONTRACT
 * WITH Alex(zeroapp@126.com). IF SUCH MODIFICATIONS ARE FOR THE PURPOSE
 * OF CIRCUMVENTING LICENSING LIMITATIONS, LEGAL ACTION MAY RESULT.
 */

package com.zeroapp.parking.client;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.Config;
import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: ParkingClient.
 * </p>
 * <p>
 * Description: 处理消息的服务.
 * </p>
 * 
 * @author Bobby Zou(zouxiaobo@hisense.com) 2015-5-28.
 * @version $Id$
 */

public class ParkingClient extends Service {

    private static Handler mHandler = null;
	private Socket mSocket = null;
    private static ParkingClient mClient = null;
    
    public static ParkingClient getClient(Handler handler) {
        mHandler = handler;
        if (mClient == null) {
            mClient = new ParkingClient();
        }
        return mClient;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("");

	}

	@Override
	public IBinder onBind(Intent intent) {
        return null;
	}

	@Override
	public void onDestroy() {
		Log.e("");
		super.onDestroy();
	}

    public int sendMessageToServer(ClientServerMessage m) {
		Log.d("m content  " + m.getMessageContent());
        Messager mer = new Messager(ParkingClient.getClient(mHandler), m);
        ChannelThread t = new ChannelThread(mer);
        t.start();
        // connectToServerWithNetty();
//        Log.d("mChannel  " + mChannel);
//        mChannel.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)), new Messager(ParkingClient.getClient(), m));
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(
//					mSocket.getOutputStream());
//			oos.writeObject(m);
//			oos.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
		return 1;
	}


    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: use Netty to get connect to Server. use SocketChannel to
     * pass Message between Client&Server.
     * </p>
     * 
     */
    private void connectToServerWithNetty() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap();
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                try {
                    bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            mChannel = ch;
                            // ClientServerMessage m = new
                            // ClientServerMessage();
                            // m.setMessageContent("test1");
//                            mChannel.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)), new Messager(ParkingClient.this, m));
                        }
                    });
                    ChannelFuture future = bootstrap.connect(Config.HOST_ADRESS, Config.HOST_PORT).sync();
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    eventLoopGroup.shutdownGracefully();
                }

            }
        }).start();

    }

	/**
	 * <p>
	 * Title: TODO.
	 * </p>
	 * <p>
	 * Description: TODO.
	 * </p>
	 * 
	 */
	private void connectToServer() {
		mSocket = new Socket();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mSocket.connect(new InetSocketAddress(Config.HOST_ADRESS,
							Config.HOST_PORT), 2000);
					Log.i("connected to server");
				} catch (IOException e) {
					Log.i("SocketTimeoutException ");
					e.printStackTrace();
					// TODO what shall we do if timeout
				}
				while (true) {
					ObjectInputStream ois = null;
					ClientServerMessage message;
					InputStream is;
					try {
						is = mSocket.getInputStream();
						ois = new ObjectInputStream(is);
						message = (ClientServerMessage) ois.readObject();
						handleMessage(message);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}

			}
		}).start();

	}

	/**
	 * <p>
	 * Title: TODO.
	 * </p>
	 * <p>
	 * Description: TODO.
	 * </p>
	 * 
	 * @param message
	 */
    public void handleMessage(AMessage message) {
		Log.d("content: " + message.getMessageContent());
        mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_FROM_SERVER, message).sendToTarget();

	}
}
