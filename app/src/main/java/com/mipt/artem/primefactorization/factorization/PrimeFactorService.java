package com.mipt.artem.primefactorization.factorization;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mipt.artem.primefactorization.Utils;
import com.mipt.artem.primefactorization.base.PrimeFactorizationApp;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by artem on 23.07.16.
 */
public class PrimeFactorService extends Service implements ProgressChangeListener {
    private static final String TAG = "PrimeFactorService";

    private int mCurrentProgress;
    private final ServiceBinder mBinder = new ServiceBinder();
    private ProgressChangerListener mProgressChangerListener;


    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    @Override
    public void setProgress(int progress) {
        mCurrentProgress = progress;
        if(mProgressChangerListener != null) {
            mProgressChangerListener.setProgress(progress);
        }
    }


    public static void start(Context context, String number) {
        Intent intent = new Intent(context, PrimeFactorService.class);
        intent.putExtra(FactoriztaionContainerActivity.EXTRA_NUMBER, number);
        context.startService(intent);
    }

    public static boolean isRunning(Context context) {
        return Utils.isServiceRunning(PrimeFactorService.class, context);
    }

    public void setServiceListener(FactorizeNumberFragment.ServiceListener serviceListener) {
        this.mProgressChangerListener = serviceListener;
    }

    public class ServiceBinder extends Binder {
        public PrimeFactorService getService() {
            return PrimeFactorService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if(intent == null) {
            throw new IllegalArgumentException("intent must not be null");
        }
        final String number = intent.getStringExtra(FactoriztaionContainerActivity.EXTRA_NUMBER);
        if(number == null) {
            throw new IllegalArgumentException("number can not be null");
        }
        final BigInteger factorizedNumber = new BigInteger(number);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: start factor");
                List result = FactorAlgorithm.start(factorizedNumber, PrimeFactorService.this);
                Log.d(TAG, "run: finish result is " + result);
                if(mProgressChangerListener != null) {
                    mProgressChangerListener.setResult(result);
                }
                PrimeFactorizationApp.getCache(PrimeFactorService.this).save(number, result);
                PrimeFactorService.this.stopSelf();
            }
        }).start();
        return START_REDELIVER_INTENT;
    }
}
