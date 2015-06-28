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

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.zeroapp.parking.R;
import com.zeroapp.parking.bluetooth.BluetoothChatService;
import com.zeroapp.parking.client.MessageBox;
import com.zeroapp.parking.client.OnConnectStateChangeListener;
import com.zeroapp.parking.client.PostMan;
import com.zeroapp.parking.common.CarInfo;
import com.zeroapp.parking.common.User;
import com.zeroapp.parking.locator.LocateService;
import com.zeroapp.parking.locator.Park;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.MessageConst;
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
public class MainActivity extends FragmentActivity implements OnClickListener, OnConnectStateChangeListener {

    protected LocateService mService;

	protected static final int MESSAGE_NEW_LOCATION = 111110;// zxb
	protected static final int MESSAGE_READ_LOCATION_RECORD = 122220;// zxb
    public static final String PREF_NAME = "Parking";
    public SharedPreferences prefNoVersion = null;
    public User me = null;
    public static List<CarInfo> myCars = null;

	// Member object for the chat services
	private BluetoothChatService mChatService = null;
    private static MessageBox mBox;
	private BaseFragment f = null;
    private TextView balance = null;
    private long mExitTime = 0;
    private LinearLayout userButtonsLayout;
    private LinearLayout adButtonsLayout;
    private int lastClick = 0;// 记录上次点击的viewid,用于防止重复点击的逻辑


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("+++ ON CREATE +++");

		// Set up the window layout
		setContentView(R.layout.activity_main);
		initView();
        bindServer();
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
        Log.e("-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exitApp();
//        mService.unbindService(conn)
        // Stop the Bluetooth chat services
        if (mChatService != null) {
            mChatService.stop();
        }
        Log.e("--- ON DESTROY ---");
    }

    public static MessageBox getBox() {
        return mBox;
    }
    public void setBox(MessageBox box) {
        this.mBox = box;
    }

	private void initView() {
        userButtonsLayout = (LinearLayout) findViewById(R.id.llayout_user_buttons);
        adButtonsLayout = (LinearLayout) findViewById(R.id.llayout_ad_buttons);
        balance = (TextView) findViewById(R.id.balance);
        findViewById(R.id.btn_user_info).setOnClickListener(this);
        findViewById(R.id.btn_show_user_record).setOnClickListener(this);
        findViewById(R.id.btn_show_biddings).setOnClickListener(this);
        findViewById(R.id.btn_adman_info).setOnClickListener(this);
        findViewById(R.id.btn_show_business).setOnClickListener(this);
        findViewById(R.id.btn_show_adman_record).setOnClickListener(this);
	}

    private void bindServer() {
        mBox = new MessageBox(mHandler);
        PostMan man = new PostMan(mBox);
        man.setConnectStateChangeListener(this);
        new Thread(man).start();
        if (mService == null) {
            bindLocateService();
        }
    }

    public void initUser() {
        me = new User();
        myCars = null;
        lastClick = 0;
        prefNoVersion = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        me.setAccount(prefNoVersion.getString("account", null));
        me.setPassword(prefNoVersion.getString("password", null));
    }

	// The Handler that gets information back
    public final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MessageConst.MessageType.MESSAGE_FROM_SERVER:
                    f.refreshUI((AMessage) msg.obj);
				break;
                case MessageConst.MessageType.MESSAGE_UI:
                    dealUIMessage((AMessage) msg.obj);
                    break;
			case MESSAGE_NEW_LOCATION:
				// zxb
				// some message new Tracer
//				Tracer mTracer = Tracer.getInstance(getApplicationContext());
//				// some message new Park
//				Park mNewPark = new Park();
//				mNewPark.setParkStratTime(mTracer.getRequestLocation()
//						.getTime());
//				mNewPark.setParkLatitude(mTracer.getRequestLocation()
//						.getLatitude());
//				mNewPark.setParkLongitude(mTracer.getRequestLocation()
//						.getLongitude());

				// save in Client DB
				break;
			case MESSAGE_READ_LOCATION_RECORD:
				// zxb
				// some message read record form DB;
				// Tracer mTracer =
				// Tracer.getInstance(getApplicationContext());
				// some message new Park
				Park mLastPark = new Park();
				mLastPark.setParkStratTime(0);// TODO
				mLastPark.setParkLatitude(0);// TODO
				mLastPark.setParkLongitude(0);// TODO
				// read data from HardWare,analysis data,find the end time
				// of last park record.
				mLastPark.setParkEndTime(0);// TODO
				break;
			}
		}
	};

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param obj
     */
    protected void dealUIMessage(AMessage obj) {
        ActionBar actionBar = getActionBar();
        switch (obj.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO:
                // update balance
                balance.setText(me.getAccountBanlance() + "");
                // show buttons
                userButtonsLayout.setVisibility(View.VISIBLE);
                adButtonsLayout.setVisibility(View.GONE);
                // 设置ActionBar
                actionBar.setTitle(me.getName());
                // remember me
                prefNoVersion.edit().putString("account", me.getAccount()).commit();
                prefNoVersion.edit().putString("password", me.getPassword()).commit();
                showFragment(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
                break;
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_ADMAN_INFO:
                // update balance
                balance.setText(me.getAccountBanlance() + "");
                // show buttons
                userButtonsLayout.setVisibility(View.GONE);
                adButtonsLayout.setVisibility(View.VISIBLE);
                // 设置ActionBar
                actionBar.setTitle(me.getName());
                // remember me
                prefNoVersion.edit().putString("account", me.getAccount()).commit();
                prefNoVersion.edit().putString("password", me.getPassword()).commit();
                showFragment(MessageConst.MessageType.MSG_TYPE_UI_SHOW_ADMAN_INFO);
                break;

            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_SIGN_IN:
                // update balance
                balance.setText(0 + "");
                // hide buttons
                userButtonsLayout.setVisibility(View.GONE);
                adButtonsLayout.setVisibility(View.GONE);
                // 设置ActionBar
                actionBar.setTitle(getString(R.string.app_name));

                showFragment(MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN);
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
            case R.id.secure_connect_scan:
                return true;
//            case R.id.insecure_connect_scan:
//                return true;
            case R.id.locate:
                Intent i = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(i);
//                new MapLocateDialog(MainActivity.this, R.layout.map_locate_dialog).show();
                return true;
		}
		return false;
	}
    private void exitApp() {
        System.exit(0);
    }

	@Override
	public void onClick(View v) {
        if (lastClick != v.getId()) {
            // 恢复之前click的view 的点击状态
            if (lastClick != 0) {
                findViewById(lastClick).setAlpha(1);
                findViewById(lastClick).setClickable(true);
            }
            // 设置新click的view 的状态
            v.setClickable(false);
            v.setAlpha(0.5f);
            lastClick = v.getId();
        }
		showFragment(v.getId());
	}
	public void showFragment(int id) {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.btn_signup:
                f = new SignupFragment();
                break;
            case MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN:
                f = new SigninFragment();
                break;
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO:
            case R.id.btn_user_info:
                if (lastClick == 0) {
                    lastClick = R.id.btn_user_info;
                    findViewById(lastClick).setAlpha(0.5f);
                    findViewById(lastClick).setClickable(false);
                }
                f = new UserInfoFragment();
                break;
            case R.id.btn_show_biddings:
                f = new BiddingFragment();
                break;
            case R.id.btn_show_user_record:
                f = new UserRecordFragment();
                break;
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_ADMAN_INFO:
            case R.id.btn_adman_info:
                if (lastClick == 0) {
                    lastClick = R.id.btn_adman_info;
                    findViewById(lastClick).setAlpha(0.5f);
                    findViewById(lastClick).setClickable(false);
                }
                f = new AdertisingManInfoFragment();
                break;
            case R.id.btn_show_business:
                f = new BusinessFragment();
                break;
            case R.id.btn_show_adman_record:
                f = new AdmanRecordFragment();
                break;

            default:
                break;
        }
        t.replace(R.id.topfl_container, f).commit();
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

//                        exitApp();
                    }
                }
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
	}

    @Override
    public void onConnect() {
        Log.d("");
        showFragment(MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN);
    }
    @Override
    public void onDisconnect() {
        // TODO Auto-generated method stub

    }



    private void bindLocateService() {
        Intent i = new Intent(this, LocateService.class);
        i.setAction("com.zeroapp.parking.locator.LocateService");
        bindService(i, new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.w("Disconnected " + name);
                mService = null;// TODO
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.w("Connected " + name);
                mService = ((LocateService.MyBinder) binder).getService();
//                mService.addLocationListener(MainActivity.this);
            }
        }, Context.BIND_AUTO_CREATE);

    }

}
