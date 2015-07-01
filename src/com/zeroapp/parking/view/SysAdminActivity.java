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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.zeroapp.parking.R;
import com.zeroapp.parking.client.ClientService;
import com.zeroapp.parking.common.CarInfo;
import com.zeroapp.parking.common.User;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: MainActivity.
 * </p>
 * <p>
 * Description: MainActivity.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-27.
 * @version $Id$
 */
public class SysAdminActivity extends BaseActivity implements OnClickListener {

    public static List<CarInfo> myCars = null;
    private TextView search;
    private long mExitTime = 0;
    private TextView etUserName;
    private TextView etUserPhone;
    private TextView etUserIdNum;
    private ProgressBar loadingBar;
    private User user = new User();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("+++ ON CREATE +++");
		// Set up the window layout
        setContentView(R.layout.activity_admin);
		initView();
        initUser();
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.e("++ ON START ++");
	}

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.e("+ ON RESUME +");
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
        Log.e("-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("--- ON DESTROY ---");
    }

	private void initView() {
        search = (TextView) findViewById(R.id.et_search);
        etUserName = (TextView) findViewById(R.id.user_name);
        etUserPhone = (TextView) findViewById(R.id.user_phone);
        etUserIdNum = (TextView) findViewById(R.id.user_idnum);
        findViewById(R.id.btn_serach).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_signout).setOnClickListener(this);
        getActionBar().setTitle(me.getName());
        loadingBar = (ProgressBar) findViewById(R.id.loading);
	}
    public void initUser() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_serach:

                break;
            case R.id.btn_update:

                break;
            case R.id.btn_signout:
                // 删除用户名和密码记录
                SharedPreferences prefNoVersion = getApplicationContext().getSharedPreferences(ClientService.PREF_NAME, 0);
                prefNoVersion.edit().putString("account", null).commit();
                prefNoVersion.edit().putString("password", null).commit();
                // 启动登录界面
                Intent i = new Intent(SysAdminActivity.this, SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            default:
                break;
        }
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 连按两次back键退出
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (System.currentTimeMillis() - mExitTime > 1500) {
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();
                    } else {
                        moveTaskToBack(true);
                    }
                }
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
	}

    @Override
    public void dealMessage(AMessage m) {
        // TODO Auto-generated method stub

    }
}
