package com.mipt.artem.primefactorization.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.mipt.artem.primefactorization.factorization.FactoriztaionContainerActivity;

/**
 * Created by artem on 29.07.16.
 */
public class FactorApplication extends Application  implements Application.ActivityLifecycleCallbacks {
    private  boolean isFactorActivityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
}

    public static boolean isFactorActivityVisible(Context context) {
        FactorApplication factorApplication = (FactorApplication)context.getApplicationContext();
        return factorApplication.isFactorActivityVisible;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof FactoriztaionContainerActivity) {
            isFactorActivityVisible = true;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof FactoriztaionContainerActivity) {
            isFactorActivityVisible = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
