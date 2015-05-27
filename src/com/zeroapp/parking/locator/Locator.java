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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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

    private static final double EARTH_RADIUS = 6378137.0;
    // 获得自己位置
//    private double selfLatitude;// 我的经度
//    private double selfLongitude;// 我的维度
    private Context mContext;

    public Locator(Context context) {
        super();
        this.mContext = context;
        registLocation();
    }

    public void registLocation() {
        LocationManager locationm = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationm.getBestProvider(criteria, true);
        Log.i("BestProvider: " + provider);
        Location location = locationm.getLastKnownLocation(provider);
//        Location location = locationm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // 获得上次的记录
//        gpsLocate(location);

        locationm.requestLocationUpdates(provider, 1000, 0, GPSListener);
    }

//    public double getSelfLatitude() {
//        return selfLatitude;
//    }
//
//    public void setSelfLatitude(double selfLatitude) {
//        this.selfLatitude = selfLatitude;
//    }
//
//    public double getSelfLongitude() {
//        return selfLongitude;
//    }
//
//    public void setSelfLongitude(double selfLongitude) {
//        this.selfLongitude = selfLongitude;
//    }

    public double getDistance1(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
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
        double dist = Math.sin(deg2rad(lat_a)) * Math.sin(deg2rad(lat_b))
                + Math.cos(deg2rad(lat_a)) * Math.cos(deg2rad(lat_b)) * Math.cos(deg2rad(theta));
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
        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b)
                * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;
        // d = Math.round(d*10000);
        return d;
    }

//    private void gpsLocate(Location location) {
//        Log.i(TAG, "gps_locate !");
//        if (location != null) {
//            selfLatitude = location.getLatitude();
//            selfLongitude = location.getLongitude();
//        } else {
//            selfLatitude = 0;
//            selfLongitude = 0;
//        }
//    }

    LocationListener GPSListener = new LocationListener() {

        // 监听位置变化，实时获取位置信息
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            // 位置发生改变时 发出
//            gpsLocate(location);
            Tracer.getInstance(mContext).onLocationChanged(location);
        }
    };

}
