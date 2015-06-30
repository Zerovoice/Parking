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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.zeroapp.parking.R;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.JsonTool;
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
public class SignupFragment extends BaseFragment {

    private View mainView;
    private UserActivity mainActivity;
    private EditText accountEt;
    private EditText passwordEt;
    private CheckBox agreementCb;
    private CheckBox adManCb;
    private EditText phoneNumEt;

    @Override
    public void onAttach(Activity activity) {
        Log.i("onAttach");
        super.onAttach(activity);
        mainActivity = (UserActivity) getActivity();
    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_signup, null);
        accountEt = (EditText) mainView.findViewById(R.id.register_account);
        passwordEt = (EditText) mainView.findViewById(R.id.register_password);
        phoneNumEt = (EditText) mainView.findViewById(R.id.register_phone);
        adManCb = (CheckBox) mainView.findViewById(R.id.cb_ifadvertisingman);
        agreementCb = (CheckBox) mainView.findViewById(R.id.cb_agreement);
//        EditText nickEt = (EditText) mainView.findViewById(R.id.register_nick);
        RadioGroup group = (RadioGroup) mainView.findViewById(R.id.register_radiogroup);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup arg0, int id) {
                if (id == R.id.register_radio_nv) {
                    mainActivity.me.setSex(0);// 女
                }
            }
        });
        mainView.findViewById(R.id.rigister_btn_register).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (adManCb.isChecked()) {
                    mainActivity.me.setUserType("1_0");
                }
                if (accountEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")) {
                    Toast.makeText(mainActivity, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!agreementCb.isChecked()) {
                    Toast.makeText(mainActivity, "请阅读并同意<用户协议>", Toast.LENGTH_SHORT).show();
                } else if (phoneNumEt.getText().toString().equals("")) {//TODO 检查手机号码是否违规
                    Toast.makeText(mainActivity, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.me.setAccount(accountEt.getText().toString());
                    mainActivity.me.setPassword(passwordEt.getText().toString());
                    mainActivity.me.setPhoneNum(phoneNumEt.getText().toString());
                    ClientServerMessage m = new ClientServerMessage();
                    m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_SIGN_UP);
                    m.setMessageContent(JsonTool.getString(mainActivity.me));
//                    mainActivity.getBox().sendMessage(m);

                    // 跳转fragment TODO
//                    msg.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
//                    mainActivity.mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_UI, msg).sendToTarget();
                }
            }
        });
        mainView.findViewById(R.id.rigister_btn_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ClientServerMessage m = new ClientServerMessage();
                m.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_SIGN_IN);
//                mainActivity.mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_UI, m).sendToTarget();
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
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
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
            case MessageConst.MessageType.MSG_TYPE_USER_SIGN_UP:
                if (msg.getMessageResult() == MessageConst.MessageResult.MSG_RESULT_SUCCESS) {
                    mainActivity.me = JsonTool.getUser(msg.getMessageContent());
                    if (mainActivity.me.getUserType().startsWith("0")) {
                        msg.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
                    } else if (mainActivity.me.getUserType().startsWith("1")) {
                        msg.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_ADMAN_INFO);
                    }
//                    mainActivity.mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_UI, msg).sendToTarget();
                } else {
                    // TODO TOAST FAIL
                }
                break;

            default:
                break;
        }

    }

}
