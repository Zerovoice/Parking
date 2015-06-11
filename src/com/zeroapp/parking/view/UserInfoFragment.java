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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zeroapp.parking.R;
import com.zeroapp.parking.client.ContentToObj;
import com.zeroapp.parking.common.CarInfo;
import com.zeroapp.parking.common.ObjToContent;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.Log;


/**
 * <p>
 * Title: UserInfoFragment.
 * </p>
 * <p>
 * Description: 显示用户详情和汽车信息.
 * </p>
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
    private ListView listViewCars;
    private ProgressBar loadingBar;

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
        listViewCars = (ListView) mainView.findViewById(R.id.lv_cars);
        loadingBar = (ProgressBar) mainView.findViewById(R.id.loading);
        if (mainActivity.myCars == null) {
            requestMyCars();
        } else {
            updateListViewCars();
        }
        return mainView;
    }

    private void updateListViewCars() {
        // TODO show on UI
//        listViewCars.setAdapter(new MyCarsAdeptet(mainActivity.myCars));
        // 隐藏缓冲圈
        loadingBar.setVisibility(View.INVISIBLE);

    }

    /**
     * <p>
     * Title: requestMyCars.
     * </p>
     * <p>
     * Description: 请求用户汽车列表.
     * </p>
     * 
     */
    private void requestMyCars() {
        ClientServerMessage m = new ClientServerMessage();
        m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_LIST_MYCARS);
        m.setMessageContent(ObjToContent.getContent(mainActivity.me));
        mainActivity.getBox().sendMessage(m);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void refreshUI(AMessage msg) {
        Log.i("");
        switch (msg.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_USER_LIST_MYCARS:
                if (msg.getMessageResult() == MessageConst.MessageResult.MSG_RESULT_SUCCESS) {
                    mainActivity.myCars = ContentToObj.getUserCars(msg.getMessageContent());
                    // test code
//                    for (int i = 0; i < mainActivity.myCars.size(); i++) {
//                        Log.d(mainActivity.myCars.get(i).getCarNum());
//                        Log.d(mainActivity.myCars.get(i).getBiddingID() + "");
//                    }
                }
                updateListViewCars();
                break;

            default:
                break;
        }

    }

    public class MyCarsAdeptet extends BaseAdapter {

        private List<CarInfo> cars = null;

        public MyCarsAdeptet(List<CarInfo> l) {
            if (l == null) {
                cars = new ArrayList<CarInfo>();
            } else {
                cars = l;
            }
            cars.add(new CarInfo());
        }

        @Override
        public int getCount() {
            return cars.size();
        }

        @Override
        public Object getItem(int i) {
            return cars.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup group) {
            // TODO Auto-generated method stub
            return null;
        }
    }



}
