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

package com.zeroapp.parking.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import com.zeroapp.utils.BmapPoint;
import com.zeroapp.utils.Log;


/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-8.
 * @version $Id$
 */

public class ContentToObj {

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param messageContent
     * @return
     */
    public static User getUser(String messageContent) {
        Log.i("messageContent: " + messageContent);
        User u = new Gson().fromJson(messageContent, User.class);
        // TODO test code
//        u.setAccount("zxb");
//        u.setPassword("123");
        // test code
        return u;
    }

//    public static Business getBusiness(String messageContent) {
//        Log.i("messageContent: " + messageContent);
//        Business b = new Gson().fromJson(messageContent, Business.class);
//        return b;
//    }

    public static CarInfo getCarInfo(String messageContent) {
        Log.i("messageContent: " + messageContent);
        CarInfo o = new Gson().fromJson(messageContent, CarInfo.class);
        return o;
    }

    public static Bidding getBidding(String messageContent) {
        Log.i("messageContent: " + messageContent);
        Bidding o = new Gson().fromJson(messageContent, Bidding.class);
        return o;
    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param messageContent
     * @return
     */
    public static List<CarInfo> getUserCars(String messageContent) {
        Gson g = new Gson();
        List<CarInfo> cars = g.fromJson(messageContent, new TypeToken<List<CarInfo>>() {
        }.getType());
        return cars;
    }

    public static List<Business> getBusinessList(String messageContent) {
        Gson g = new Gson();
        List<Business> bs = g.fromJson(messageContent, new TypeToken<List<Business>>() {
        }.getType());
        return bs;
    }
    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param bmString
     * @return
     */
    public static BmapPoint[] getCoordinatesOfArea(String jString) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jString).getAsJsonArray();
        if (jArray.size() == 0) {
            return null;
        }
        BmapPoint[] bmapPoints = new BmapPoint[jArray.size()];
        Log.d("jArray: " + jArray);
        for (JsonElement obj : jArray) {
            int i = 0;

            BmapPoint bp = gson.fromJson(obj, BmapPoint.class);
            bmapPoints[i] = bp;
            Log.d("bmapPoints: " + bmapPoints[i].getLng());
            i++;
        }
        return bmapPoints;
    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param messageContent
     * @return
     */
    public static List<Bidding> getBiddingsList(String messageContent) {
        Gson g = new Gson();
        List<Bidding> bs = g.fromJson(messageContent, new TypeToken<List<Bidding>>() {
        }.getType());
        return bs;
    }

}
