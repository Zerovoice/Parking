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
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zeroapp.parking.R;
import com.zeroapp.parking.message.AMessage;
import com.zeroapp.parking.message.ClientServerMessage;
import com.zeroapp.parking.message.MessageConst;
import com.zeroapp.utils.Log;


/**
 * <p>
 * Title: BiddingFragment.
 * </p>
 * <p>
 * Description: 用来显示bidding列表.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-28.
 * @version $Id$
 */
public class BiddingFragment extends BaseFragment {

    private MainActivity mainActivity;
    private View mainView;
    private TextView cityName;
    private ListView listViewBiddings;
    private ProgressBar loadingBar;
    private LinearLayout llBidding;

    @Override
    public void onAttach(Activity activity) {
        Log.i("onAttach");
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView");
        mainView = inflater.inflate(R.layout.fragment_bidddings, null);
        llBidding = (LinearLayout) mainView.findViewById(R.id.ll_bidding);
        cityName = (TextView) mainView.findViewById(R.id.city_name);
        cityName.setText("青岛");// TODO
        listViewBiddings = (ListView) mainView.findViewById(R.id.lv_biddings);
        loadingBar = (ProgressBar) mainView.findViewById(R.id.loading);
        requestBiddings();
        return mainView;
    }

    /**
     * <p>
     * Title: requestBiddings.
     * </p>
     * <p>
     * Description: 请求bidding列表.
     * </p>
     * 
     */
    private void requestBiddings() {
        ClientServerMessage m = new ClientServerMessage();
        m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_LIST_BIDDING);
        m.setMessageContent("qingdao");// TODO
        mainActivity.getBox().sendMessage(m);
    }

    private void updateListViewCars() {
        // TODO show on UI
        // test code
        Log.i("updateListViewCars");
        TextView t = new TextView(mainActivity);
        t.setText("get binddings!");
        LayoutParams lp = llBidding.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.MATCH_PARENT;
        t.setLayoutParams(lp);
        llBidding.addView(t, 0);

        // 显示主View
        llBidding.setVisibility(View.VISIBLE);
        // 隐藏缓冲圈
        loadingBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void refreshUI(AMessage msg) {
        Log.i("");
        switch (msg.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_USER_LIST_BIDDING:
                if (msg.getMessageResult() == MessageConst.MessageResult.MSG_RESULT_SUCCESS) {
                    Log.i("success");
                }
                updateListViewCars();
                break;

            default:
                break;
        }
    }

}
