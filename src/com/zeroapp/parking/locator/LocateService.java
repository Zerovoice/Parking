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

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;

import com.zeroapp.parking.common.ParkingInfo;
import com.zeroapp.utils.Log;

/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-17.
 * @version $Id$
 */

public class LocateService extends Service {

    /**
     * 坐标类型:gcj02(国测局加密经纬度坐标)\bd09ll(百度加密经纬度坐标)\bd09(百度加密墨卡托坐标)
     */
    private static final String COOR_TYPE = "bd09ll";
    /**
     * 定位时间间隔,单位ms
     */
    private static final int SCAN_SPAN = 5000;
    /**
     * 是否打开GPS
     */
    private static final boolean IS_OPEN_GPS = true;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocClient;

    private MyBinder mBinder = new MyBinder();
    private BDLocation mRequestLocation;
    private PositionTracer mTracer = null;
    private ArrayList<Tracer> mTracerManager = new ArrayList<Tracer>();
    private ArrayList<ParkingInfo> mParkingManager = new ArrayList<ParkingInfo>();

    public class MyBinder extends Binder {

        public LocateService getService() {
            return LocateService.this;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("");
        registLocation();
        // 初始化追踪线程;
        mTracer = new PositionTracer(this);
        new Thread(mTracer).start();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent i) {
        return mBinder;
    }

    public void addLocationListener(Tracer t) {
        mTracerManager.add(t);
    }

    public void removeLocationListener(Tracer t) {
        mTracerManager.remove(t);
    }

    public void addParkingInfo(ParkingInfo p) {
        mParkingManager.add(p);
    }

    public void removeParkingInfo(ParkingInfo p) {
        mParkingManager.remove(p);
    }

    private void registLocation() {
        Log.i("");
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(IS_OPEN_GPS);// 打开gps
        option.setCoorType(COOR_TYPE); // 设置坐标类型
        option.setScanSpan(SCAN_SPAN);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("Time: " + location.getTime() + "........Lat: " + location.getLatitude() + "........Lon: " + location.getLongitude());
            for (Tracer t : mTracerManager) {
                t.onLocationChanged(location);
                for (ParkingInfo p : mParkingManager) {
                    // TODO 时间判断不应该简单的是不等于,而应该是超过一定时长
                    if (!p.getTimeStart().equals(location.getTime())) {
                        t.onComingBack();
                    }
                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
