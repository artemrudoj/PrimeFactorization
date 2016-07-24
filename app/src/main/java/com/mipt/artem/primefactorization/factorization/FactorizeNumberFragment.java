package com.mipt.artem.primefactorization.factorization;


import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mipt.artem.primefactorization.R;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FactorizeNumberFragment extends Fragment {
    private LocalServiceConnection mLocalServiceConnection =
            new LocalServiceConnection();
    private PrimeFactorService mBoundLocalService;
    private boolean mIsBound;
    private String mNumber;
    private int mCurrentProgress;
    private TextView mPercentTextView;
    private final String TAG = "FactorizeNumberFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getNumber();
        //set default value
        mCurrentProgress = -1;
        Activity currentActivity = getActivity();
        if (currentActivity != null) {
            if (!PrimeFactorService.isRunning(currentActivity)) {
                PrimeFactorService.start(currentActivity, mNumber);
            }
        }
    }

    String getNumber() {
        if (getArguments() != null && getArguments().containsKey(FactoriztaionContainerActivity.EXTRA_NUMBER)) {
            return getArguments().getString(FactoriztaionContainerActivity.EXTRA_NUMBER);
        } else {
            throw new IllegalArgumentException("can not found number");
        }
    }


    public static FactorizeNumberFragment newInstance(String number) {
        FactorizeNumberFragment factorizeNumberFragment = new FactorizeNumberFragment();
        Bundle args = new Bundle();
        args.putString(FactoriztaionContainerActivity.EXTRA_NUMBER, number);
        factorizeNumberFragment.setArguments(args);
        return factorizeNumberFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_factorize_number, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updatePercentCount();
        updateUI();
    }

    void updateUI() {
        if(mCurrentProgress != -1) {
            mPercentTextView.setText(Integer.toString(mCurrentProgress));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToService();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindFromService();
    }

    void bindToService() {
        Activity currentActivity = getActivity();
        if(currentActivity != null) {
            currentActivity.bindService(new Intent(currentActivity, PrimeFactorService.class),
                        mLocalServiceConnection, Service.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    }

    void unbindFromService() {
        Activity currentActivity = getActivity();
        if(currentActivity != null) {
            if (mIsBound) {
                try {
                    if(mBoundLocalService != null) {
                        mBoundLocalService.setServiceListener(null);
                    }
                    currentActivity.unbindService(mLocalServiceConnection);
                    mIsBound = false;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void updatePercentCount() {
        if(mBoundLocalService != null) {
            mCurrentProgress = mBoundLocalService.getCurrentProgress();
        }
    }

    private class LocalServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBoundLocalService = ((PrimeFactorService.ServiceBinder)iBinder)
                    .getService();
            mBoundLocalService.setServiceListener(new ServiceListener(FactorizeNumberFragment.this));
            updatePercentCount();
            updateUI();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBoundLocalService = null;
        }
    }


    public FactorizeNumberFragment() {
    }


    public static class ServiceListener implements ProgressChangerListener {
        private WeakReference<FactorizeNumberFragment> mWeakActivity;
        public ServiceListener(FactorizeNumberFragment fragment) {
            this.mWeakActivity = new WeakReference<>(fragment);
        }
        @Override
        public void setProgress(int progress) {
            final FactorizeNumberFragment localReferenceActivity = mWeakActivity.get();
            if (localReferenceActivity != null) {
                Activity activity = localReferenceActivity.getActivity();
                if(activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            localReferenceActivity.updatePercentCount();
                            localReferenceActivity.updateUI();
                        }
                    });
                }
            }
        }

        @Override
        public void setResult(final List result) {
            final FactorizeNumberFragment localReferenceActivity = mWeakActivity.get();
            if (localReferenceActivity != null) {
                Activity activity = localReferenceActivity.getActivity();
                if(activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            localReferenceActivity.mPercentTextView.setText(result.toString());
                        }
                    });
                }
            }
        }

    }



    private void initView(View view) {
        mPercentTextView = (TextView)view.findViewById(R.id.percent_tv);
    }

}
