package com.mipt.artem.primefactorization.base;

import android.app.Application;
import android.content.Context;

import com.mipt.artem.primefactorization.cache.CacheRepository;
import com.mipt.artem.primefactorization.cache.SharedPrefrenciesCache;

/**
 * Created by artem on 24.07.16.
 */
public class PrimeFactorizationApp extends Application {
    CacheRepository mCache;
    @Override
    public void onCreate() {
        super.onCreate();
        mCache = new SharedPrefrenciesCache(this);
    }


    public CacheRepository getCache() {
        return mCache;
    }

    public static CacheRepository getCache(Context context) {
        return ((PrimeFactorizationApp)context.getApplicationContext()).getCache();
    }
}
