package com.testtask.currencyconverter.presenter;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.testtask.currencyconverter.common.Currency;
import com.testtask.currencyconverter.common.CurrencyNum;
import com.testtask.currencyconverter.model.CurrencyModel;
import com.testtask.currencyconverter.view.MainActivity;

import java.util.List;
import java.util.Random;

public class CurrencyPresenter {

    private MainActivity view;
    private final CurrencyModel model;
    CurrentCurrenciesState state;

    public CurrencyPresenter(CurrencyModel model) {
        this.model = model;
        state = new CurrentCurrenciesState();
    }

    public void viewIsReady(TextView code1, TextView code2) {
        model.loadCurrencies(new CurrencyModel.LoadCurrenciesCallback() {
            @Override
            public void onLoad(List<Currency> currencies) {
                Currency first = currencies.get(0);
                Currency second = currencies.get(1);
                code1.setText(first.getCharCode());
                code2.setText(second.getCharCode());
                state.setFirstCurrency(first);
                state.setSecondCurrency(second);
            }
        });
    }

    public void attachView(MainActivity mainActivity) {
        view = mainActivity;
    }

    public void detachView() {
        view = null;
    }


    public void changeCurrency(TextView view, EditText to, CurrencyNum num, int index) {
        List<Currency> data = model.getData();
        Currency newCurrency = data.get(index);

        switch (num) {
            case FIRST_CURRENCY:
                state.setFirstCurrency(newCurrency);
                break;
            case SECOND_CURRENCY:
                state.setSecondCurrency(newCurrency);
                break;
        }
        to.setText("0");
        view.setText(newCurrency.getCharCode());
    }

    public void recalculateRate(EditText target, Editable text, CurrencyNum num) {
        String value = text.toString();
        double dVal = 0f;
        if (!value.isEmpty()) {
            dVal = Double.parseDouble(value);
        }
        target.setText(String.format("%.2f", state.getRate(dVal, num)));
    }

    public List<String> getShownData() {
        return model.getShownData();
    }

    private class CurrentCurrenciesState {
        private Currency firstCurrency;
        private Currency secondCurrency;

        private void setFirstCurrency(Currency firstCurrency) {
            this.firstCurrency = firstCurrency;
        }

        private void setSecondCurrency(Currency secondCurrency) {
            this.secondCurrency = secondCurrency;
        }

        private double getRate(double value, CurrencyNum num) {
            switch (num) {
                case FIRST_CURRENCY:
                    return value * (secondCurrency.getRubValue() / secondCurrency.getNominal())
                            / (firstCurrency.getRubValue() / firstCurrency.getNominal());
                case SECOND_CURRENCY:
                    return value * (firstCurrency.getRubValue() / firstCurrency.getNominal())
                        / (secondCurrency.getRubValue() / secondCurrency.getNominal());
                default:
                    return -1;
            }
        }
    }
}

