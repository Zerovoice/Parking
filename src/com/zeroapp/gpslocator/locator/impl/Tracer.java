/* 
 * Copyright (C) 2011 Hisense Electric Co., Ltd. 
 * All Rights Reserved.
 *
 * ALL RIGHTS ARE RESERVED BY HISENSE ELECTRIC CO., LTD. ACCESS TO THIS
 * SOURCE CODE IS STRICTLY RESTRICTED UNDER CONTRACT. THIS CODE IS TO
 * BE KEPT STRICTLY CONFIDENTIAL.
 *
 * UNAUTHORIZED MODIFICATION OF THIS FILE WILL VOID YOUR SUPPORT CONTRACT
 * WITH HISENSE ELECTRIC CO., LTD. IF SUCH MODIFICATIONS ARE FOR THE PURPOSE
 * OF CIRCUMVENTING LICENSING LIMITATIONS, LEGAL ACTION MAY RESULT.
 */

package com.zeroapp.gpslocator.locator.impl;

import android.content.Context;
import android.location.Location;

import com.zeroapp.utils.Log;



/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Bobby Zou(zouxiaobo@hisense.com) 2015-5-22.
 * @version $Id$
 */

public class Tracer {

    private Context mContext;
    private Locator mLocator;
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

    }

}
