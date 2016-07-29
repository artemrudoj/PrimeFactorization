package com.mipt.artem.primefactorization.enternumber;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mipt.artem.primefactorization.R;
import com.mipt.artem.primefactorization.base.BaseFragment;
import com.mipt.artem.primefactorization.factorization.FactoriztaionContainerActivity;
import com.mipt.artem.primefactorization.utils.ValidationTextWatcher;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterNumberFragment extends BaseFragment {

    private EditText mNumberEditText;
    private Button mStartFactorizationButton;
    private TextInputLayout mNumberTextInputLayout;


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
        mNumberEditText.addTextChangedListener(new ValidationTextWatcher() {
            @Override
            public void validate() {
                isNumberValid();
            }
        });
        mNumberTextInputLayout = (TextInputLayout)view.findViewById(R.id.number_til);
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
        if (mNumberEditText.getText().toString().matches("^[1-9][0-9]*")) {
            mNumberTextInputLayout.setError(null);
            return true;
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                mNumberTextInputLayout.setError(activity.getString(R.string.input_error));
            }
            return false;
        }
    }

}
