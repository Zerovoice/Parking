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

package com.zeroapp.parking.client;

import android.app.Application;
import android.content.Intent;

import com.zeroapp.parking.locator.LocateService;
import com.zeroapp.utils.Log;

/**
 * <p>Title: TODO.</p>
 * <p>Description: TODO.</p>
 *
 * @author Alex(zeroapp@126.com) 2015-6-17.
 * @version $Id$
 */

public class LocationApplication extends Application {

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     */
    @Override
    public void onCreate() {
        Log.i("");
        super.onCreate();
        startLocateService();
    }

    /**
     * <p>
     * Title: TODO.
     * </p>
     * <p>
     * Description: TODO.
     * </p>
     * 
     */
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    private void startLocateService() {
        Intent i = new Intent(this, LocateService.class);
        i.setAction("com.zeroapp.parking.locator.LocateService");
        startService(i);
    }
}
