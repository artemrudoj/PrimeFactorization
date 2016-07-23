package com.mipt.artem.primefactorization.factorization;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mipt.artem.primefactorization.Utils;

/**
 * Created by artem on 23.07.16.
 */
public class PrimeFactorService extends Service implements ProgressChangerListener {

    private int mCurrentProgress;
    private final ServiceBinder mBinder = new ServiceBinder();

    @Override
    public void setProgress(int progress) {
        mCurrentProgress = progress;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public static void start(Context context, String number) {
        Intent intent = new Intent(context, PrimeFactorService.class);
        intent.putExtra(FactoriztaionContainerActivity.EXTRA_NUMBER, number);
        context.startService(intent);
    }

    public static boolean isRunning(Context context) {
        return Utils.isServiceRunning(PrimeFactorService.class, context);
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
        
        return START_REDELIVER_INTENT;
    }
}
