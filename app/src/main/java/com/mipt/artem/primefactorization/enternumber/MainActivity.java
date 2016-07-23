package com.mipt.artem.primefactorization.enternumber;


import android.os.Bundle;

import com.mipt.artem.primefactorization.R;
import com.mipt.artem.primefactorization.base.BackButtonActivity;


public class MainActivity extends BackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_toolbar_activity);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fl, new EnterNumberFragment()).commit();
        }
    }
}
