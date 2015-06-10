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
import android.widget.Toast;

import com.zeroapp.parking.R;
import com.zeroapp.parking.client.ContentToObj;
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
public class SignupFragment extends BaseFragment {

    private View mainView;
    private MainActivity mainActivity;
    private EditText accountEt;
    private EditText passwordEt;
    private CheckBox agreementCb;

    @Override
    public void onAttach(Activity activity) {
        Log.i("onAttach");
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
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
        agreementCb = (CheckBox) mainView.findViewById(R.id.cb_agreement);
//        EditText nickEt = (EditText) mainView.findViewById(R.id.register_nick);
//        RadioGroup group = (RadioGroup) mainView.findViewById(R.id.register_radiogroup);
//        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            public void onCheckedChanged(RadioGroup arg0, int id) {
//                if (id == R.id.register_radio_nv) {
//                    mainActivity.me.setSex(0);// 女
//                }
//            }
//        });
        mainView.findViewById(R.id.rigister_btn_register).setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (accountEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")) {
                    Toast.makeText(mainActivity, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!agreementCb.isChecked()) {
                    Toast.makeText(mainActivity, "请阅读并同意<用户协议>", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.me.setAccount(accountEt.getText().toString());
                    mainActivity.me.setPassword(passwordEt.getText().toString());
                    ClientServerMessage m = new ClientServerMessage();
                    m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_SIGN_UP);
                    m.setMessageContent(ObjToContent.getContent(mainActivity.me));
                    mainActivity.getBox().sendMessage(m);
                }
            }
        });
        return mainView;
    }

    // if(accountEt.getText().toString().equals("") ||
    // passwordEt.getText().toString().equals("")){
    // Toast.makeText(mainActivity, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
    // }else {
    // dialog = ProgressDialog.show(mainActivity, "YQ", "正在登陆中 …", true, true);
    // final User user=new User();
    // user.setAccount(Integer.parseInt(accountEt.getText().toString()));
    // user.setPassword(passwordEt.getText().toString());
    // user.setNick(nickEt.getText().toString());
    // user.setTrends("this should be fixed ");
    // user.setSex(sex);
    // user.setAvatar("3");
    // user.setLev(0);
    // user.setAge(0);
    // user.setTime(MyTime.geTimeNoS());
    // user.setTag(1);
    // user.setOperation("register");
    // new Thread( new Runnable() {
    // private boolean b;
    // public void run() {
    // b=new ActionClient(RegisterActivity.this).sendRegisterInfo(user);
    // ProgressBarControl();
    // }
    // private void ProgressBarControl() {
    // Log.i(TAG, "");
    // handler.post(new Runnable(){
    // public void run() {
    // if(b){
    // Message m=new Message();
    // m.what=1;
    // handler.sendMessage(m);
    // //转到主界面
    // Toast.makeText(RegisterActivity.this, "恭喜你，注册成功 ！",
    // Toast.LENGTH_SHORT).show();
    // startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    // }else {
    // dialog.dismiss();
    // Toast.makeText(RegisterActivity.this, "还要恭喜你，注册失败 ！",
    // Toast.LENGTH_SHORT).show();
    // }
    // }
    // });
    // }
    // }).start();
    // }
    // }
    // });
    // }

//    private Handler handler=new Handler(){  
//        public void handleMessage(Message msg){  
//            switch(msg.what){  
//                case 1:
//                    dialog.dismiss();  
//                    break;  
//            }  
//        }  
//    }; 
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

                    mainActivity.me = ContentToObj.getUser(msg.getMessageContent());
                    msg.setMessageType(MessageConst.MessageType.MSG_TYPE_UI_SHOW_USER_INFO);
                    mainActivity.mHandler.obtainMessage(MessageConst.MessageType.MESSAGE_UI, msg).sendToTarget();
                }
                break;

            default:
                break;
        }

    }

}
