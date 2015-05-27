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
public class Tracer {

    private Context mContext;
    private Locator mLocator;
    private Location mRequestLocation = null;
    public static Tracer tracer = null;

    private Tracer(Context context) {
        this.mContext = context;
        this.mLocator = new Locator(context);
    }

    public static Tracer getInstance(Context context) {
        if (tracer == null) {
            tracer = new Tracer(context);
        }
        return tracer;
    }

    public Location getRequestLocation() {
        return mRequestLocation;
    }

//    public void setRequestLocation(Location requestLocation) {
//        mRequestLocation = requestLocation;
//    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     * @param location
     * @param t
     */
    public void onLocationChanged(Location location) {
        Log.i("Time:" + location.getTime() + "\n new Location: " + location + "\n with speed:"
                + location.getSpeed());
        // test code
        mRequestLocation = location;
        // test code
    }

}
