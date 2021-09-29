package com.testtask.currencyconverter.common;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.testtask.currencyconverter.R;
import com.testtask.currencyconverter.view.MainActivity;

public class ConnectionDialog extends DialogFragment {
    private final MainActivity context;

    public ConnectionDialog(MainActivity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.conn_warninig))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.pos_button_name), ((dialogInterface, i) ->
                {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    context.finishAndRemoveTask();
                }))
                .setNegativeButton(getResources().getString(R.string.neg_button_name), ((dialogInterface, i) -> {
                    context.finishAndRemoveTask();
                }));
        return builder.create();
    }
}
