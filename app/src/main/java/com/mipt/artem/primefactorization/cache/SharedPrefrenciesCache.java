package com.mipt.artem.primefactorization.cache;

import android.content.Context;

import com.mipt.artem.primefactorization.base.PrimeFactorizationApp;

import java.util.List;

/**
 * Created by artem on 24.07.16.
 */
public class SharedPrefrenciesCache implements CacheRepository {


    public SharedPrefrenciesCache(Context context) {

    }

    @Override
    public void save(String number, List dividers) {

    }

    @Override
    public List getDividers(String number) {
        return null;
    }
}
