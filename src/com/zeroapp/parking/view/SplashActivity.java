package com.zeroapp.parking.view;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import com.zeroapp.parking.R;

public class SplashActivity extends Activity{

    public static final String PREF_NAME_VERSION = "Parking" + 1;
    public static final String PREF_NAME = "Parking";
	public SharedPreferences prefNoVersion = null;

    private String account = null;
    private String pwd = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
        try {
            String appName = getString(R.string.app_name);
            String appVersion = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
            versionNumber.setText(appName + ":" + appVersion);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        prefNoVersion = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        account = prefNoVersion.getString("account", null);
        pwd = prefNoVersion.getString("password", null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        }, 1500);
	}


}
