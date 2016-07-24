package com.mipt.artem.primefactorization.factorization;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mipt.artem.primefactorization.R;

public class FactoriztaionContainerActivity extends AppCompatActivity {
    public static final String EXTRA_NUMBER = "FactoriztaionContainerActivity.number";

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
}
