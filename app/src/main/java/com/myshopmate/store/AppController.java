package com.myshopmate.store;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.franmontiel.localechanger.LocaleChanger;
import com.myshopmate.store.util.ConnectivityReceiver;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.ENGLISH);
        locales.add(new Locale("ar", "ARABIC"));
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        FirebaseCrashlytics.getInstance().setUserId(model+" "+version);
        LocaleChanger.initialize(getApplicationContext(), locales);
        LocaleChanger.setLocale(Locale.ENGLISH);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(MainActivity listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
