package com.mrprona.dota2assitant;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.util.TypefaceUtil;


/**
 * Created by DANGLV on 31/05/2016.
 */
public class SCApplication extends MultiDexApplication {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/brawlstars.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        // LeakCanary.install(this); // For debug only
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
