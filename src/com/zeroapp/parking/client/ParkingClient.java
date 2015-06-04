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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.utils.Log;



/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-4.
 * @version $Id$
 */

public class ParkingClient extends Thread {

    private static ParkingClient mClient = null;
    private Handler mClientHandler = null;

    private ParkingClient() {
    }

    public static ParkingClient getClient() {
        if (mClient == null) {
            mClient = new ParkingClient();
        }
        return mClient;
    }

    public void receiveMessage(AMessage m) {
//        Log.d("1");
        MessageBox box = new MessageBox(mClientHandler, m);
//        Log.d("2");
        PostMan man = new PostMan(box);
//        Log.d("3");
        mClientHandler.post(man);
//        Log.d("4");
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
    public void run() {
        Log.d("");
        Looper.prepare();
        mClientHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        ClientServerMessage m = (ClientServerMessage) msg.obj;
                        Log.d(m.getMessageContent() + "");
                        break;

                    default:
                        break;
                }
            }
        };
        Log.d("" + mClientHandler);
        Looper.loop();
    }

}
