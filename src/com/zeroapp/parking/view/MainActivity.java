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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.zeroapp.parking.R;
import com.zeroapp.parking.bluetooth.BluetoothChatService;
import com.zeroapp.parking.client.MessageBox;
import com.zeroapp.parking.client.OnConnectStateChangeListener;
import com.zeroapp.parking.client.PostMan;
import com.zeroapp.parking.common.CarInfo;
import com.zeroapp.parking.common.User;
import com.zeroapp.parking.locator.Park;
import com.zeroapp.parking.locator.Tracer;
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

	protected static final int MESSAGE_NEW_LOCATION = 111110;// zxb
	protected static final int MESSAGE_READ_LOCATION_RECORD = 122220;// zxb
    public static final String PREF_NAME = "Parking";
    public SharedPreferences prefNoVersion = null;
    public User me = null;
    public List<CarInfo> myCars = null;

	// Member object for the chat services
	private BluetoothChatService mChatService = null;
    private MessageBox mBox;
	private BaseFragment f = null;
    private FrameLayout fLayout = null;
    private TextView balance = null;
    private LinearLayout buttonLayout = null;
	private Button buttonSignin;

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
        // Stop the Bluetooth chat services
        if (mChatService != null) {
            mChatService.stop();
        }
        Log.e("--- ON DESTROY ---");
    }
    public MessageBox getBox() {
        return mBox;
    }
    public void setBox(MessageBox box) {
        this.mBox = box;
    }

	private void initView() {
        fLayout = (FrameLayout) findViewById(R.id.topfl_container);
        buttonLayout = (LinearLayout) findViewById(R.id.llayout_button);
        balance = (TextView) findViewById(R.id.balance);
        findViewById(R.id.button_user_info).setOnClickListener(this);
        findViewById(R.id.button_show_total).setOnClickListener(this);
        findViewById(R.id.button_show_biddings).setOnClickListener(this);
	}

    private void bindServer() {
        mBox = new MessageBox(mHandler);
        PostMan man = new PostMan(mBox);
        man.setConnectStateChangeListener(this);
        new Thread(man).start();
    }

    public void initUser() {
        me = new User();
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
				Tracer mTracer = Tracer.getInstance(getApplicationContext());
				// some message new Park
				Park mNewPark = new Park();
				mNewPark.setParkStratTime(mTracer.getRequestLocation()
						.getTime());
				mNewPark.setParkLatitude(mTracer.getRequestLocation()
						.getLatitude());
				mNewPark.setParkLongitude(mTracer.getRequestLocation()
						.getLongitude());

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

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
        switch (obj.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO:
                // update balance
                balance.setText(me.getAccountBanlance() + "");
                // show buttons
                buttonLayout.setVisibility(View.VISIBLE);
                // 设置ActionBar
                ActionBar actionBar = getActionBar();
                // actionBar.setDisplayShowHomeEnabled(false);
                // actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setTitle(me.getName());
                // actionBar.setSubtitle("zxb");

                showFragment(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
                break;

            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_SIGN_IN:
                // update balance
                balance.setText(0 + "");
                // hide buttons
                buttonLayout.setVisibility(View.GONE);
                // 设置ActionBar
                ActionBar actionBar2 = getActionBar();
                actionBar2.setTitle(getString(R.string.app_name));

                showFragment(MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN);
                break;
            default:
                break;
        }

    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
            case R.id.secure_connect_scan:
                return true;
//            case R.id.insecure_connect_scan:
//                return true;
            case R.id.exit:
                exitApp();
                return true;
		}
		return false;
	}
    private void exitApp() {
        Process.killProcess(Process.myPid());

    }

	@Override
	public void onClick(View v) {
		showFragment(v.getId());
	}
	public void showFragment(int id) {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN:
                f = new SigninFragment();
                break;
            case MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO:
                f = new UserInfoFragment();
                break;
            case R.id.btn_signup:
                f = new SignupFragment();
                break;
            case R.id.button_show_total:
                f = new TotalFragment();
                break;
            case R.id.button_show_biddings:
                f = new BiddingFragment();
                break;
            case R.id.button_user_info:
                f = new UserInfoFragment();
                break;

            default:
                break;
        }
        t.replace(R.id.topfl_container, f).commit();

		// int h = topLayout.getHeight();
		// Animation inAnimotion = new TranslateAnimation(0, 0, -h, 0);
		// inAnimotion.setFillAfter(true);
		// inAnimotion.setDuration(1000);
		// topLayout.startAnimation(inAnimotion);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                fLayout.setVisibility(View.GONE);
                return true;

            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
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
}
