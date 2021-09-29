package com.testtask.currencyconverter.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.testtask.currencyconverter.R;
import com.testtask.currencyconverter.presenter.CurrencyPresenter;
import com.testtask.currencyconverter.view.MainActivity;

public class CurrencyListDialog extends DialogFragment {
    private final CurrencyPresenter presenter;
    private final TextView codeView;
    private final EditText to;
    private final EditText from;
    private final CurrencyNum num;

    public CurrencyListDialog(CurrencyPresenter presenter, TextView codeView, EditText to, EditText from, CurrencyNum num) {
        this.presenter = presenter;
        this.codeView = codeView;
        this.to = to;
        this.from = from;
        this.num = num;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] values = presenter.getShownData().toArray(new String[0]);
        builder.setTitle(getResources().getString(R.string.change_currency))
                .setItems(values, (dialogInterface, i) -> presenter.changeCurrency(codeView, to, num, i))
                .setNegativeButton(getResources().getString(R.string.neg_button_name), (dialogInterface, i) -> {

                });
        return builder.create();
    }
}
