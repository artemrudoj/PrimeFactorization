package com.mipt.artem.primefactorization.factorization;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mipt.artem.primefactorization.R;
import com.mipt.artem.primefactorization.base.BaseActivity;

public class FactoriztaionContainerActivity extends BaseActivity {
    public static final String EXTRA_NUMBER = "FactoriztaionContainerActivity.number";
    public interface OnFactorCancel{
        void onCancel();
    }

    OnFactorCancel mOnFactorCancelListener;

    public void setOnFactorCancelListener(OnFactorCancel onFactorCancelListener) {
        mOnFactorCancelListener = onFactorCancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_toolbar_activity);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_fl, FactorizeNumberFragment.newInstance(getNumber())).commit();
        }
    }

    String getNumber() {
        String number = getIntent().getStringExtra(EXTRA_NUMBER);
        if(number == null) {
            throw new IllegalArgumentException("can not found number");
        }
        return number;
    }

    public static void go(Context context, String number) {
        Intent intent = new Intent(context, FactoriztaionContainerActivity.class);
        intent.putExtra(FactoriztaionContainerActivity.EXTRA_NUMBER, number);
        context.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (mOnFactorCancelListener != null) {
            mOnFactorCancelListener.onCancel();
        } else {
            super.onBackPressed();
        }
    }
}
