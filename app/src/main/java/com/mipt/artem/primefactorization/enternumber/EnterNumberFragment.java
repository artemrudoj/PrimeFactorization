package com.mipt.artem.primefactorization.enternumber;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mipt.artem.primefactorization.R;
import com.mipt.artem.primefactorization.factorization.FactoriztaionContainerActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNumberFragment extends Fragment {

    private EditText mNumberEditText;
    private Button mStartFactorizationButton;


    public EnterNumberFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enter_number, container, false);
        initViews(view);
        return view;
    }


    void initViews(View view) {
        mNumberEditText = (EditText)view.findViewById(R.id.number_et);
        mStartFactorizationButton = (Button)view.findViewById(R.id.start_factorization_btn);
        mStartFactorizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNumberValid()) {
                    FactoriztaionContainerActivity.go(getActivity(), mNumberEditText.getText().toString());
                }
            }
        });
    }

    boolean isNumberValid() {
        return true;
    }

}
