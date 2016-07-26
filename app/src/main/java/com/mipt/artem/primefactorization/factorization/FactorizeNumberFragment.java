package com.mipt.artem.primefactorization.factorization;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mipt.artem.primefactorization.R;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FactorizeNumberFragment extends Fragment {
    private final int CANCEL_ACTION = 123;
    private LocalServiceConnection mLocalServiceConnection =
            new LocalServiceConnection();
    private PrimeFactorService mBoundLocalService;
    private boolean mIsBound;
    private String mNumber;
    private int mCurrentProgress;
    private TextView mPercentTextView;
    private List mResult;
    private Button mStopButton;
    private final String TAG = "FactorizeNumberFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getNumber();
        //set default value
        mCurrentProgress = -1;
        Activity currentActivity = getActivity();
        if (currentActivity != null) {
            if (isNeedToStartService(currentActivity)) {
                    PrimeFactorService.start(currentActivity);
            }
        }
    }

    boolean isNeedToStartService(Context context) {
        return (mResult == null) && (!PrimeFactorService.isRunning(context));
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
        Log.d(TAG, "onResume: ");
        super.onResume();
        syncWithService();
        updateUI();
    }

    void updateUI() {
        if(mResult != null) {
            mPercentTextView.setText(mResult.toString());
        } else {
            if (mCurrentProgress != -1) {
                mPercentTextView.setText(Integer.toString(mCurrentProgress));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
    
    

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (mResult == null) {
            bindToService();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
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

    void syncWithService() {
        if (mResult != null) {
            return;
        }
        if(mBoundLocalService != null) {
            mResult = mBoundLocalService.getResult(mNumber);
            if(mResult == null) {
                mCurrentProgress = mBoundLocalService.getCurrentProgress();
            } else {
                unbindFromService();
            }
        }
    }

    private class LocalServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            mBoundLocalService = ((PrimeFactorService.ServiceBinder)iBinder)
                    .getService();
            mBoundLocalService.setServiceListener(new ServiceListener(FactorizeNumberFragment.this));
            syncWithService();
            if (mResult == null) {
                mBoundLocalService.startFactor(mNumber);
            } else {
                updateUI();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBoundLocalService = null;
            Log.d(TAG, "onServiceDisconnected: ");
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
                            localReferenceActivity.syncWithService();
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
                            localReferenceActivity.mResult = result;
                            localReferenceActivity.mPercentTextView.setText(result.toString());
                            localReferenceActivity.unbindFromService();
                        }
                    });
                }
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == CANCEL_ACTION) {
            if(mBoundLocalService != null) {
                mBoundLocalService.setCanceled(true);
            }
            getActivity().finish();
        }
    }

    private void initView(View view) {
        mPercentTextView = (TextView)view.findViewById(R.id.percent_tv);
        mStopButton = (Button) view.findViewById(R.id.stop_btn);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getFragmentManager();
                ApproveExitDialogFragment dialog = new ApproveExitDialogFragment();
                dialog.setTargetFragment(FactorizeNumberFragment.this, CANCEL_ACTION);
                dialog.show(manager, null);
            }
        });
    }

}
