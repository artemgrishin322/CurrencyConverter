package com.testtask.currencyconverter.common;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextChangedListener<T> implements TextWatcher {
    private T target;
    private CurrencyNum targetCurrencyNum;
    private boolean isBlocked = false;
    private TextChangedListener<T> blockingNeighbour;

    public TextChangedListener(T target, CurrencyNum num) {
        this.target = target;
        this.targetCurrencyNum = num;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!isBlocked) {
            this.recalculate(target, editable, targetCurrencyNum);
        }
    }

    public abstract void recalculate(T target, Editable editable, CurrencyNum num);

    public void setBlock() {
        this.isBlocked = true;
    }

    public void releaseBlock() {
        this.isBlocked = false;
    }

    public void attach(TextChangedListener<T> blockingNeighbour) {
        this.blockingNeighbour = blockingNeighbour;
    }

    public TextChangedListener<T> getBlockingNeighbour() {
        return blockingNeighbour;
    }
}
