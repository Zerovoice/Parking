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
public class Park {

    private double mParkLatitude = 0.0;
    private double mParkLongitude = 0.0;
    private long mParkStratTime = 0;
    private long mParkEndTime = 0;

    public double getParkLatitude() {
        return mParkLatitude;
    }

    public void setParkLatitude(double parkLatitude) {
        mParkLatitude = parkLatitude;
    }

    public double getParkLongitude() {
        return mParkLongitude;
    }

    public void setParkLongitude(double parkLongitude) {
        mParkLongitude = parkLongitude;
    }

    public long getParkStratTime() {
        return mParkStratTime;
    }

    public void setParkStratTime(long parkStratTime) {
        mParkStratTime = parkStratTime;
    }

    public long getParkEndTime() {
        return mParkEndTime;
    }

    public void setParkEndTime(long parkEndTime) {
        mParkEndTime = parkEndTime;
    }

}
