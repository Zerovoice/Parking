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

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zeroapp.parking.R;
import com.zeroapp.parking.common.ContentToObj;
import com.zeroapp.parking.common.ObjToContent;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
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
 * @author Alex(zeroapp@126.com) 2015-5-28.
 * @version $Id$
 */
public class SigninFragment extends BaseFragment {

	private View mainView;
	private EditText editTextAccount;
	private EditText editTextPwd;
	private Button buttonSingin;
	private Button buttonSingup;
	private MainActivity mainActivity;
    private ProgressBar loadingBar;
    private LinearLayout llSignin;

    @Override
	public void onAttach(Activity activity) {
		Log.i("onAttach");
		super.onAttach(activity);
		mainActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("onCreateView");
		mainView = inflater.inflate(R.layout.fragment_signin, null);
        llSignin = (LinearLayout) mainView.findViewById(R.id.ll_signin);
		editTextAccount = (EditText) mainView.findViewById(R.id.et_account);
		editTextPwd = (EditText) mainView.findViewById(R.id.et_password);
		buttonSingin = (Button) mainView.findViewById(R.id.btn_signin);
		buttonSingup = (Button) mainView.findViewById(R.id.btn_signup);
		buttonSingin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                if (editTextAccount.getText().toString().equals("") || editTextPwd.getText().toString().equals("")) {
                    Toast.makeText(mainActivity, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.me.setAccount(editTextAccount.getText().toString());
                    mainActivity.me.setPassword(editTextPwd.getText().toString());
                    sendMeToServer();
                }

			}
		});
		buttonSingup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                mainActivity.showFragment(v.getId());
			}
		});
        loadingBar = (ProgressBar) mainView.findViewById(R.id.loading);
		return mainView;
	}

    @Override
    public void onStart() {
        super.onStart();
        // 从SharedPreferences中取出了账号&密码;自动登录
        if (mainActivity.me.getAccount() != null && mainActivity.me.getPassword() != null) {
            mainView.setVisibility(View.INVISIBLE);
            sendMeToServer();
        } else {
            // 显示主View
            llSignin.setVisibility(View.VISIBLE);
            // 隐藏缓冲圈
            loadingBar.setVisibility(View.INVISIBLE);
        }
    }

    private void sendMeToServer() {
        ClientServerMessage m = new ClientServerMessage();
        m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN);
        m.setMessageContent(ObjToContent.getContent(mainActivity.me));
        mainActivity.getBox().sendMessage(m);

    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param msg
     */
    @Override
    public void refreshUI(AMessage msg) {
        Log.i("");
        switch (msg.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN:
                if (msg.getMessageResult() == MessageConst.MessageResult.MSG_RESULT_SUCCESS) {
                    mainActivity.me = ContentToObj.getUser(msg.getMessageContent());
                    // 记录用户名和密码
                    mainActivity.prefNoVersion.edit().putString("account", mainActivity.me.getAccount()).commit();
                    mainActivity.prefNoVersion.edit().putString("password", mainActivity.me.getPassword()).commit();

                    msg.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
                    mainActivity.mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_UI, msg).sendToTarget();
                }
                break;

            default:
                break;
        }

    }

}
