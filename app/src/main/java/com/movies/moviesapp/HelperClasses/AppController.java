package com.movies.moviesapp.HelperClasses;

import android.app.Application;

public class AppController extends Application {
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/WorkSans-Regular.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/WorkSans-Bold.ttf");

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
