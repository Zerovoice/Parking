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
 * Title: TotalFragment.
 * </p>
 * <p>
 * Description: TODO.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-28.
 * @version $Id$
 */
public class AdmanRecordFragment extends BaseFragment {

    private View mainView;
    private MainActivity mainActivity;
    private TextView wodeName;
    private ListView listViewTotal;
    private ProgressBar loadingBar;
    private LinearLayout llTotal;

    @Override
    public void onAttach(Activity activity) {
        Log.i("onAttach");
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
        // reqData = new HashMap<String, Object>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView");
        mainView = inflater.inflate(R.layout.fragment_user_record, null);
        llTotal = (LinearLayout) mainView.findViewById(R.id.ll_total);
        wodeName = (TextView) mainView.findViewById(R.id.wode_name);
        wodeName.setText(mainActivity.me.getName());
        listViewTotal = (ListView) mainView.findViewById(R.id.lv_total);
        loadingBar = (ProgressBar) mainView.findViewById(R.id.loading);
        requestTotal();
        return mainView;
    }

    /**
     * <p>
     * Title: requestTotal.
     * </p>
     * <p>
     * Description: 请求历史Parking记录.
     * </p>
     * 
     */
    private void requestTotal() {
        ClientServerMessage m = new ClientServerMessage();
        m.setMessageType(MessageConst.MessageType.MSG_TYPE_USER_LIST_MONEY);
        m.setMessageContent("money");// TODO
        mainActivity.getBox().sendMessage(m);
    }

    private void updateListViewTotal() {
        // TODO show on UI
        // test code
        Log.i("updateListViewCars");
        TextView t = new TextView(mainActivity);
        t.setText("get Total success!");
        LayoutParams lp = llTotal.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.MATCH_PARENT;
        t.setLayoutParams(lp);
        llTotal.addView(t, 0);

        // 显示主View
        llTotal.setVisibility(View.VISIBLE);
        // 隐藏缓冲圈
        loadingBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void refreshUI(AMessage msg) {
        Log.i("");
        switch (msg.getMessageType()) {
            case MessageConst.MessageType.MSG_TYPE_USER_LIST_MONEY:
                if (msg.getMessageResult() == MessageConst.MessageResult.MSG_RESULT_SUCCESS) {
                    Log.i("success");
                }
                updateListViewTotal();
                break;

            default:
                break;
        }
    }

}
