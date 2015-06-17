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

package com.zeroapp.parking.locator;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: TODO.
 * </p>
 * <p>
 * Description: TODO.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-26.
 * @version $Id$
 */
public class Locator {

    private static Locator locator = null;
    /**
     * 坐标类型:gcj02(国测局加密经纬度坐标)\bd09ll(百度加密经纬度坐标)\bd09(百度加密墨卡托坐标)
     */
    private static final String COOR_TYPE = "bd09ll";
    /**
     * 定位时间间隔,单位ms
     */
    private static final int SCAN_SPAN = 1000;
    /**
     * 是否打开GPS
     */
    private static final boolean IS_OPEN_GPS = true;
    public MyLocationListenner myListener = new MyLocationListenner();
    private Context mContext;
    private LocationClient mLocClient;
    private BDLocation mRequestLocation;
    private PositionTracer mTracer = null;

    private Locator(Context context) {
        super();
        this.mContext = context;
        // 初始化BaiduLocator
        registLocation();
        // 初始化追踪线程;
        mTracer = new PositionTracer(context);
        new Thread(mTracer).start();
    }

    public static Locator getInstance(Context context) {
        if (locator == null) {
            locator = new Locator(context.getApplicationContext());
        }
        return locator;
    }

    private void registLocation() {
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(IS_OPEN_GPS);// 打开gps
        option.setCoorType(COOR_TYPE); // 设置坐标类型
        option.setScanSpan(SCAN_SPAN);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public BDLocation getLocation() {
        Log.i("getLatitude:" + mRequestLocation.getLatitude() + "\ngetLongitude:" + mRequestLocation.getLongitude() + "\ngetTime:" + mRequestLocation.getTime() + "\ngetRadius:"
                + mRequestLocation.getRadius());
        return mRequestLocation;
    }

    public void rememberLocation(BDLocation l) {
        mTracer.setStratLocation(l);
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            } else {
                mRequestLocation = location;
//                ((LocationActivity) mContext).onNewLocation(location);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
