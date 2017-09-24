package com.hypertrack.androidsdkonboarding;

import android.app.Application;

import com.hypertrack.lib.HyperTrack;

/**
 * Created by piyush on 08/05/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize HyperTrack SDK with your Publishable Key here
        // Refer to documentation at
        // https://docs.hypertrack.com/gettingstarted/authentication.html
        // @NOTE: Add **YOUR_PUBLISHABLE_KEY_HERE** here for SDK to be
        // authenticated with HyperTrack Server
        HyperTrack.initialize(this,"pk_300a8f24f2ab47d7f10d7b88dba6de46bd6518ea");
    }
}