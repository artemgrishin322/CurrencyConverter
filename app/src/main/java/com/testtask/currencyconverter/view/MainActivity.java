package com.testtask.currencyconverter.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.testtask.currencyconverter.R;
import com.testtask.currencyconverter.common.ConnectionDialog;
import com.testtask.currencyconverter.common.CurrencyListDialog;
import com.testtask.currencyconverter.common.CurrencyNum;
import com.testtask.currencyconverter.common.TextChangedListener;
import com.testtask.currencyconverter.model.CurrencyModel;
import com.testtask.currencyconverter.presenter.CurrencyPresenter;

public class MainActivity extends AppCompatActivity {
    private CurrencyPresenter presenter;

    private EditText firstCurrencyValue;
    private EditText secondCurrencyValue;
    private TextView firstCurrencyCode;
    private TextView secondCurrencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkInternetConnection()) {
            init();
        }
    }

    private void init() {
        firstCurrencyValue = findViewById(R.id.firstCurrencyValue);
        secondCurrencyValue = findViewById(R.id.secondCurrencyValue);
        firstCurrencyCode = findViewById(R.id.firstCurrencyCode);
        secondCurrencyCode = findViewById(R.id.secondCurrencyCode);

        CurrencyModel model = new CurrencyModel();
        presenter = new CurrencyPresenter(model);
        presenter.attachView(this);

        presenter.viewIsReady(firstCurrencyCode, secondCurrencyCode);

        TextChangedListener<EditText> firstCurrencyChangeListener = new TextChangedListener<EditText>(secondCurrencyValue, CurrencyNum.SECOND_CURRENCY) {
            @Override
            public void recalculate(EditText target, Editable editable, CurrencyNum num) {
                getBlockingNeighbour().setBlock();
                presenter.recalculateRate(target, editable, num);
                getBlockingNeighbour().releaseBlock();
            }
        };

        TextChangedListener<EditText> secondCurrencyChangeListener = new TextChangedListener<EditText>(firstCurrencyValue, CurrencyNum.FIRST_CURRENCY) {
            @Override
            public void recalculate(EditText target, Editable editable, CurrencyNum num) {
                getBlockingNeighbour().setBlock();
                presenter.recalculateRate(target, editable, num);
                getBlockingNeighbour().releaseBlock();
            }
        };

        firstCurrencyChangeListener.attach(secondCurrencyChangeListener);
        secondCurrencyChangeListener.attach(firstCurrencyChangeListener);

        firstCurrencyValue.addTextChangedListener(firstCurrencyChangeListener);
        secondCurrencyValue.addTextChangedListener(secondCurrencyChangeListener);
    }

    private boolean checkInternetConnection() {
        if (!isConnected(this)) {
            FragmentManager manager = getSupportFragmentManager();
            ConnectionDialog dialog = new ConnectionDialog(this);
            dialog.show(manager, null);
            return false;
        } else return true;
    }

    private boolean isConnected(MainActivity activity) {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }

    public void onChangeFirstCurrencyClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        CurrencyListDialog dialog = new CurrencyListDialog(presenter, firstCurrencyCode, firstCurrencyValue,
                secondCurrencyValue, CurrencyNum.FIRST_CURRENCY);
        dialog.show(manager, null);
    }

    public void onChangeSecondCurrencyClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        CurrencyListDialog dialog = new CurrencyListDialog(presenter, secondCurrencyCode, secondCurrencyValue,
                firstCurrencyValue, CurrencyNum.SECOND_CURRENCY);
        dialog.show(manager, null);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
