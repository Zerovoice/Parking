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
import android.os.Handler;
import android.os.IBinder;
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

import com.zeroapp.parking.R;
import com.zeroapp.parking.bluetooth.BluetoothChatService;
import com.zeroapp.parking.client.ParkingClient;
import com.zeroapp.parking.locator.Park;
import com.zeroapp.parking.locator.Tracer;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: TODO.
 * </p>
 * <p>
 * Description: TODO.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-27.
 * @version $Id$
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

	protected static final int MESSAGE_NEW_LOCATION = 111110;// zxb
	protected static final int MESSAGE_READ_LOCATION_RECORD = 122220;// zxb

	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	private BaseFragment f = null;
	private FrameLayout topLayout = null;
	private Button buttonSignin;

	ParkingClient mClient = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("+++ ON CREATE +++");

		// Set up the window layout
		setContentView(R.layout.activity_main);
		initView();
		startClient();
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
	private void startClient() {
		Intent i = new Intent(MainActivity.this, ParkingClient.class);
		startService(i);
		bindService(i, new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.w("Disconnected " + name);
				mClient = null;// TODO
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				Log.w("Connected " + name);
				mClient = ((ParkingClient.MyBinder) binder).getClient(mHandler);
			}
		}, Context.BIND_AUTO_CREATE);

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

	/**
	 * <p>
	 * Title: TODO.
	 * </p>
	 * <p>
	 * Description: TODO.
	 * </p>
	 * 
	 */
	private void initView() {
		buttonSignin = (Button) findViewById(R.id.button_signin);
		buttonSignin.setOnClickListener(this);
		topLayout = (FrameLayout) findViewById(R.id.topfl_container);

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		Log.e("- ON PAUSE -");
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

	// The Handler that gets information back
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MessageConst.MessageType.MESSAGE_FROM_SERVER:
                    f.refreshUI((AMessage) msg.obj);
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

	    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     */
    private void exitApp() {
        Process.killProcess(Process.myPid());

    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param v
     */
	@Override
	public void onClick(View v) {
		showFragment(v.getId());
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
	public void showFragment(int id) {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		switch (id) {
		case R.id.button_signin:
			f = new SigninFragment();
			break;
		case R.id.btn_signup:
			f = new SignupFragment();
			break;
		case R.id.button_show_total:
			f = new TotalFragment();
			break;
		case R.id.button_show_ad:
			f = new AdFragment();
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

	/**
	 * <p>
	 * Title: TODO.
	 * </p>
	 * <p>
	 * Description: TODO.
	 * </p>
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			topLayout.setVisibility(View.GONE);
			return true;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

}
