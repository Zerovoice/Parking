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

import com.zeroapp.parking.R;
import com.zeroapp.parking.client.MessageBox;
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

	@Override
	public void onAttach(Activity activity) {
		Log.i("onAttach");
		super.onAttach(activity);
		mainActivity = (MainActivity) getActivity();
		// reqData = new HashMap<String, Object>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("onCreateView");
		mainView = inflater.inflate(R.layout.fragment_signin, null);
		editTextAccount = (EditText) mainView.findViewById(R.id.et_account);
		editTextPwd = (EditText) mainView.findViewById(R.id.et_password);
		buttonSingin = (Button) mainView.findViewById(R.id.btn_signin);
		buttonSingup = (Button) mainView.findViewById(R.id.btn_signup);
		buttonSingin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                final ClientServerMessage m = new ClientServerMessage();
				m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_SIGN_IN);
                m.setMessageContent("click");
                MessageBox b = mainActivity.getBox();
                b.receiveMessage(m);

			}
		});
		buttonSingup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mainActivity.showFragment(v.getId());

			}
		});
		return mainView;
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

    }

}
