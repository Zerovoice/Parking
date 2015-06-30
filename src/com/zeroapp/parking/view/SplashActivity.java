package com.zeroapp.parking.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.zeroapp.parking.R;
import com.zeroapp.parking.client.ClientService;
import com.zeroapp.utils.Log;

public class SplashActivity extends BaseActivity {


    private MyBroadcastReceiver mReceiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
        try {
            String appName = getString(R.string.app_name);
            String appVersion = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
            versionNumber.setText(appName + ":" + appVersion);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ClientService.ACTION_SERVICE_CONNECTED);
        registerReceiver(mReceiver, filter);
	}
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Log.e("--- ON DESTROY ---");
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent i) {
            Log.i(i.getAction());
            if (i.getAction().equals(ClientService.ACTION_SERVICE_CONNECTED)) {
                mService.singIn();
            }
        }

    }

}
