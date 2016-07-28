package com.mipt.artem.primefactorization.factorization;


import android.app.Activity;
import android.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mipt.artem.primefactorization.R;
import com.mipt.artem.primefactorization.base.BaseFragment;
import com.mipt.artem.primefactorization.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FactorizeNumberFragment extends BaseFragment implements FactoriztaionContainerActivity.OnFactorCancel {
    public static  final int CANCEL_ACTION = 123;
    private LocalServiceConnection mLocalServiceConnection =
            new LocalServiceConnection();
    private PrimeFactorService mBoundLocalService;
    private boolean mIsBound;
    private String mNumber;
    private int mCurrentProgress;
    private TextView mPercentTextView;
    private List mResult;
    private Button mStopButton;
    private ProgressBar mProgressBar;
    private TextView mInfoTextView;
    private final String TAG = "FactorizeNumberFragment";
    private final String EXTRA_RESULT = "FactorizeNumberFragment.EXTRA_RESULT";
    private final String EXTRA_PROGRESS = "FactorizeNumberFragment.EXTRA_PROGRESS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getNumber();
        //set default value
        mCurrentProgress = -1;
        restoreData(savedInstanceState);
        Activity currentActivity = getActivity();
        if (currentActivity != null) {
            if (isNeedToStartService(currentActivity)) {
                    PrimeFactorService.start(currentActivity);
            }
            setCancelListenerIsNeeded(currentActivity);

        }
    }

    void setCancelListenerIsNeeded(Activity activity) {
        if (activity instanceof FactoriztaionContainerActivity) {
            if (!isFactorIsDone()) {
                ((FactoriztaionContainerActivity) activity).setOnFactorCancelListener(this);
            } else {
                ((FactoriztaionContainerActivity) activity).setOnFactorCancelListener(null);
            }
        }
    }

    private void restoreData(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        mResult = bundle.getStringArrayList(EXTRA_RESULT);
        mCurrentProgress = bundle.getInt(EXTRA_PROGRESS, -1);
        Log.d(TAG, "restoreData: restored " + mResult);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_PROGRESS, mCurrentProgress);
        if (!isFactorIsDone()) {
            return;
        }
        if (mResult instanceof ArrayList) {
            outState.putStringArrayList(EXTRA_RESULT, (ArrayList<String>) mResult);
        } else {
            throw new IllegalArgumentException("mResult should be ArrayList");
        }
    }

    boolean isNeedToStartService(Context context) {
        return (!isFactorIsDone()) && (!PrimeFactorService.isRunning(context));
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

    void updateUIfactorCompleted() {
        mPercentTextView.setText(mResult.toString());
        mProgressBar.setVisibility(View.INVISIBLE);
        mInfoTextView.setText(R.string.result);
        mStopButton.setText(R.string.done);
    }

    void updateUIfactorStillDoing() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mCurrentProgress != -1) {
            mPercentTextView.setText(Utils.createProgressString(Integer.toString(mCurrentProgress)));
        }
    }

    void updateUI() {
        if(isFactorIsDone()) {
            updateUIfactorCompleted();
        } else {
            updateUIfactorStillDoing();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (!isFactorIsDone()) {
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
        if (isFactorIsDone()) {
            return;
        }
        if(mBoundLocalService != null) {
            mResult = mBoundLocalService.getResult(mNumber);
            if(!isFactorIsDone()) {
                mCurrentProgress = mBoundLocalService.getCurrentProgress();
            } else {
                unbindFromService();
                Activity activity = getActivity();
                if (activity != null) {
                    setCancelListenerIsNeeded(activity);
                }
            }
        }
    }

    @Override
    public void onCancel() {
        ApproveExitDialogFragment.showDialog(getActivity().getFragmentManager(),
                FactorizeNumberFragment.this);
    }

    private class LocalServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            mBoundLocalService = ((PrimeFactorService.ServiceBinder)iBinder)
                    .getService();
            mBoundLocalService.setServiceListener(new ServiceListener(FactorizeNumberFragment.this));
            syncWithService();
            if (!isFactorIsDone()) {
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
                final Activity activity = localReferenceActivity.getActivity();
                if(activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            localReferenceActivity.mResult = result;
                            localReferenceActivity.mResult.add(0,"1");
                            localReferenceActivity.updateUI();
                            localReferenceActivity.unbindFromService();
                            if (activity instanceof FactoriztaionContainerActivity) {
                                ((FactoriztaionContainerActivity) activity).setOnFactorCancelListener(null);
                            }
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


    boolean isFactorIsDone() {
        return mResult != null;
    }

    private void initView(View view) {
        mPercentTextView = (TextView)view.findViewById(R.id.percent_tv);
        mStopButton = (Button) view.findViewById(R.id.stop_btn);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFactorIsDone()) {
                    onCancel();
                } else {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        });
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressbar);
        mInfoTextView = (TextView) view.findViewById(R.id.info_tv);
    }

}
