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
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeroapp.parking.R;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.utils.Log;


/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-10.
 * @version $Id$
 */

public class UserInfoFragment extends BaseFragment {

    private MainActivity mainActivity;
    private View mainView;
    private TextView name;
    private TextView phoneNum;
    private TextView IdNum;

    @Override
    public void onAttach(Activity activity) {
        Log.i("onAttach");
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView");
        mainView = inflater.inflate(R.layout.fragment_user, null);
        name = (TextView) mainView.findViewById(R.id.user_name);
        phoneNum = (TextView) mainView.findViewById(R.id.user_phone);
        IdNum = (TextView) mainView.findViewById(R.id.user_idnum);
        name.setText(mainActivity.me.getName());
        phoneNum.setText(mainActivity.me.getPhoneNum());
        IdNum.setText(mainActivity.me.getIdentityNum());
        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * <p>Title: TODO.</p>
     * <p>Description: TODO.</p>
     * 
     * @param msg
     */
    @Override
    public void refreshUI(AMessage msg) {
        Log.i("");

    }

}
