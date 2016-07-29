package com.mipt.artem.primefactorization.factorization;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mipt.artem.primefactorization.utils.Utils;

import java.util.List;

/**
 * Created by artem on 23.07.16.
 */
public class PrimeFactorService extends Service {
    private static final String TAG = "PrimeFactorService";


    private final ServiceBinder mBinder = new ServiceBinder();
    private ProgressChangerListener mProgressChangerListener;
    private PrimeFactorHandlerThread mPrimeFactorHandlerThread;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mPrimeFactorHandlerThread = new PrimeFactorHandlerThread();
        mPrimeFactorHandlerThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
    }


    void startFactor(String number){
        mPrimeFactorHandlerThread.startFactor(number);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPrimeFactorHandlerThread.quit();
        Log.d(TAG, "onDestroy: " );
    }


    public int getCurrentProgress() {
        return mPrimeFactorHandlerThread.getCurrentProgress();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PrimeFactorService.class);
        context.startService(intent);
    }

    public static boolean isRunning(Context context) {
        return Utils.isServiceRunning(PrimeFactorService.class, context);
    }

    public void setServiceListener(FactorizeNumberFragment.ServiceListener serviceListener) {
        this.mProgressChangerListener = serviceListener;
    }

    public void setCanceled(boolean canceled) {
        mPrimeFactorHandlerThread.setCanceled(canceled);
    }

    public List getResult(String number) {
        return mPrimeFactorHandlerThread.getResult(number);
    }


    public class ServiceBinder extends Binder {
        public PrimeFactorService getService() {
            return PrimeFactorService.this;
        }
    }





    private class PrimeFactorHandlerThread extends HandlerThread implements ProcessController {

        private final int FACTOR = 1;

        public PrimeFactorHandlerThread() {
            super("PrimeFactorHandlerThread");
        }

        private Handler mHandler;
        private List mLastResult;
        private String mLastNumber;
        private boolean isCanceled;
        private boolean isTaskRun;
        private int mCurrentProgress;
        private Message lastMessage;


        public void startFactor(String number) {
            if (isTaskRun) {
                // correct task already running
                if (mLastNumber != null && mLastNumber.equals(number)){
                    Log.d(TAG, "startFactor: task already running");
                    return;
                } else {
                    // incorrect task running
                    Log.d(TAG, "startFactor: incorrect task is running");
                    isCanceled = true;
                }
            }
            Message tempMessage = Message.obtain(Message.obtain(mHandler,
                    FACTOR, number));
            //for sync
            if (mHandler == null) {
                lastMessage = tempMessage;
            } else {
                mHandler.sendMessage(tempMessage);
            }
        }

        @Override
        protected void onLooperPrepared() {
            Log.d(TAG, "onLooperPrepared: ");
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case FACTOR:
                            isTaskRun = true;
                            mLastResult = null;
                            mCurrentProgress = 0;
                            Log.d(TAG, "run: start factor");
                            mLastNumber = (String)msg.obj;
                            if(!isCanceled()) {
                                mLastResult = FactorAlgorithm.start(mLastNumber, PrimeFactorHandlerThread.this);
                                if (mLastResult != null) {
                                    Log.d(TAG, "run: finish result is " + mLastResult);
                                    if (mProgressChangerListener != null) {
                                        mProgressChangerListener.setResult(mLastResult);
                                    }
                                }
                            }
                            isCanceled = false;
                            isTaskRun = false;
                            break;
                    }
                }
            };
            if (lastMessage != null) {
                mHandler.sendMessage(lastMessage);
                lastMessage = null;
            }
        }

        public List getResult(String number) {
            if (!isTaskRun && number.equals(mLastNumber)) {
                return mLastResult;
            } else {
                return null;
            }
        }

        public void setCanceled(boolean canceled) {
            this.isCanceled = canceled;
        }


        @Override
        public boolean isCanceled() {
            return isCanceled;
        }

        @Override
        public void setProgress(int progress) {
            mCurrentProgress = progress;
            if (mProgressChangerListener != null) {
                mProgressChangerListener.setProgress(mCurrentProgress);
            }
        }

        public int getCurrentProgress() {
            if(isTaskRun) {
                return mCurrentProgress;
            } else {
                return -1;
            }
        }

    }
}
