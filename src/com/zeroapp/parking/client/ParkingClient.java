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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.Config;
import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: TODO.
 * </p>
 * <p>
 * Description: TODO.
 * </p>
 * 
 * @author Bobby Zou(zouxiaobo@hisense.com) 2015-5-28.
 * @version $Id$
 */

public class ParkingClient extends Service {

	private final IBinder mBinder = new MyBinder();
	private Handler mHandler = null;
	private Socket mSocket = null;

	public class MyBinder extends Binder {

		public ParkingClient getClient(Handler handler) {
			mHandler = handler;
			return ParkingClient.this;
		}
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
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("");
		connectToServer();
	}

	/**
	 * <p>
	 * Title: TODO.
	 * </p>
	 * <p>
	 * Description: TODO.
	 * </p>
	 * 
	 * @param intent
	 * @return
	 */

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
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
	@Override
	public void onDestroy() {
		Log.e("");
		super.onDestroy();
	}

	public int sendMessageToServer(ClientServerMessage m) {
		Log.d("m content  " + m.getMessageContent());
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					mSocket.getOutputStream());
			oos.writeObject(m);
			oos.flush();
//			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
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
	private void handleMessage(ClientServerMessage message) {
		Log.d("content: " + message.getMessageContent());
		mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_FROM_SERVER,
				message).sendToTarget();

	}
}
