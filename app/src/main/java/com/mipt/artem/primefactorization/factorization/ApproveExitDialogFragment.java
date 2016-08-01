package com.mipt.artem.primefactorization.factorization;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.mipt.artem.primefactorization.R;


/**
 * Created by artem on 24.07.16.
 */
public class ApproveExitDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.text_in_dialog)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    static public void showDialog(FragmentManager fragmentManager, Fragment fragment) {
        ApproveExitDialogFragment dialog = new ApproveExitDialogFragment();
        dialog.setTargetFragment(fragment, FactorizeNumberFragment.CANCEL_ACTION);
        dialog.show(fragmentManager, null);
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}