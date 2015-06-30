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

package com.zeroapp.parking.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

import com.zeroapp.parking.client.ClientService;
import com.zeroapp.parking.client.ParkingApplication;
import com.zeroapp.parking.common.User;
import com.zeroapp.utils.JsonTool;
import com.zeroapp.utils.Log;

/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-30.
 * @version $Id$
 */

public abstract class BaseActivity extends FragmentActivity {

    protected ClientService mService;
    private ServiceConnection connection;
    protected User me = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("+++ ON CREATE +++");
        Intent i = getIntent();
        String s = i.getStringExtra("me");
        me = JsonTool.getUser(s);
        bindClientService();
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
        Log.e("--- ON DESTROY ---");
    }

    private void bindClientService() {
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("Disconnected " + name);
                mService = null;// TODO
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d("Connected " + name);
                mService = ((ClientService.MyBinder) binder).getService();
                Intent i = new Intent(ClientService.ACTION_SERVICE_CONNECTED);
                sendBroadcast(i);
                if (me == null) {
                    me = mService.getUserInfo();
                }
            }
        };
        Intent i = new Intent(this, ClientService.class);
        i.setAction(ParkingApplication.SERVICE_ACTION);
        bindService(i, connection, Context.BIND_AUTO_CREATE);

    }

}
