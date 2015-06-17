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
import android.location.Location;

import com.baidu.location.BDLocation;

import com.zeroapp.utils.Log;

/**
 * <p>
 * Title: TODO.
 * </p>
 * <p>
 * Description: TODO.
 * </p>
 * 
 * @author Alex(zeroapp@126.com) 2015-5-27.
 * @version $Id$
 */
public class PositionTracer implements Runnable {

    private static final double EARTH_RADIUS = 6378137.0;
    /**
     * 从startPaking开始计时,到开始追踪用户与起始位置之间距离的时间间隔;
     */
    private static final int WAIT_TIME = 5 * 1 * 1000;
    /**
     * 每次获取新位置和起始位置比较的时间间隔
     */
    private static final int EVERY_TRACE_TIME = 5 * 1000;
    /**
     * 正在追踪状态
     */
    private static boolean TRACING = true;

    private Context mContext;
//    private Locator mLocator;
    private BDLocation mStratLocation = null;

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param context
     */
    public PositionTracer(Context context) {
        mContext = context;
    }
    public void setStratLocation(BDLocation stratLocation) {
        mStratLocation = stratLocation;
    }

    @Override
    public void run() {
        // 当没有起始位置信息时,始终等待
        while (mStratLocation == null) {
            try {
                Thread.sleep(EVERY_TRACE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("get mStratLocation");
        // 当起始位置信息确定,开始等待5钟,
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("WAIT_TIME over");
        // 之后开始追踪最新位置和起始位置的距离,当距离小于一定值时,触发
        while (TRACING) {
            try {
                Thread.sleep(EVERY_TRACE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BDLocation cacheLocation = Locator.getInstance(mContext).getLocation();
            double lat_a = mStratLocation.getLatitude();
            double lng_a = mStratLocation.getLongitude();
            double lat_b = cacheLocation.getLatitude();
            double lng_b = cacheLocation.getLongitude();
            Log.i("EVERY_TRACE_TIME get new location");
            if (getDistance1(lat_a, lng_a, lat_b, lng_b) < 100) {

            }
        }
    }

    public double getDistance1(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public double getDistance2(double lat_a, double lng_a, double lat_b, double lng_b) {
        float[] results = new float[1];
        Location.distanceBetween(lat_a, lng_a, lat_b, lng_b, results);
        return results[0];
    }

    public double getDistance3(double lat_a, double lng_a, double lat_b, double lng_b) {
        double theta = lng_a - lng_b;
        double dist = Math.sin(deg2rad(lat_a)) * Math.sin(deg2rad(lat_b)) + Math.cos(deg2rad(lat_a)) * Math.cos(deg2rad(lat_b)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double miles = dist * 60 * 1.1515;
        double kilomiters = miles * 1.609344;
        return kilomiters;
    }

    // 将角度转换为弧度
    static double deg2rad(double degree) {
        return degree / 180 * Math.PI;
    }

    // 将弧度转换为角度
    static double rad2deg(double radian) {
        return radian * 180 / Math.PI;
    }

//    public double getDistance4(double lat_a, double lng_a, double lat_b, double lng_b) {
//        double d1 = 3.141592653589793D * (paramDouble2 - paramDouble1) / 180.0D;  
//        double d2 = 3.141592653589793D * (paramDouble4 - paramDouble3) / 180.0D;  
//        double d3 = Math.sin(d1 / 2.0D) * Math.sin(d1 / 2.0D) + Math.cos(3.141592653589793D * paramDouble1 / 180.0D) * Math.cos(3.141592653589793D * paramDouble2 / 180.0D) * Math.sin(d2 / 2.0D) * Math.sin(d2 / 2.0D);  
//        return 6371.0D * (2.0D * Math.atan2(Math.sqrt(d3), Math.sqrt(1.0D - d3)));  
//    }

    /**
     * <p>
     * Title: 获取方位角.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param lat_a
     * @param lng_a
     * @param lat_b
     * @param lng_b
     * @return
     */
    public double getAzimuth(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;
        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;
        // d = Math.round(d*10000);
        return d;
    }

}
