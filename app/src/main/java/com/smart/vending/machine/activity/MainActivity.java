package com.smart.vending.machine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.smart.vending.machine.R;
import com.smart.vending.machine.application.Application;
import com.smart.vending.machine.util.Constatns;
import com.smart.vending.machine.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText mInsertCoinInputEditText;
    private Button mInsertButton;
    private TextView mBalanceTextView, mSkipTextView;
    Application mApplication;
    CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mApplication = Application.mApplication;
        float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
        mBalanceTextView.setText(balance + " " + Constatns.CURRENCY_SYMBOL);
    }

    private void initViews() {
        mInsertCoinInputEditText = (TextInputEditText) findViewById(R.id.insert_coin);
        mInsertButton = (Button) findViewById(R.id.btn_insert);
        mInsertButton.setOnClickListener(this);
        mBalanceTextView = (TextView) findViewById(R.id.tview_balance);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_ordinator_layout);
        mSkipTextView = (TextView) findViewById(R.id.tview_skip);
        mSkipTextView.setPaintFlags(mSkipTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mSkipTextView.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBalance();
    }

    private void setBalance() {
        float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
        mBalanceTextView.setText(Constatns.AVAILABLE_BALANCE+balance + " " + Constatns.CURRENCY_SYMBOL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                hideSoftKeyboard();
                String insertedCoin = mInsertCoinInputEditText.getText().toString().toLowerCase();
                if (Utils.isValidCoin(insertedCoin)) {
                    float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
                    balance = balance + mApplication.mValidCoins.get(insertedCoin);
                    mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, balance);
                    mBalanceTextView.setText(balance + " " + Constatns.CURRENCY_SYMBOL);
                    showSnackbar("Coin is inserted and balance is updated . ");
                } else {
                    showSnackbar("Please insert valid coins (nickels, dimes, and quarters) ");
                }
                break;
            case R.id.tview_skip:
                mInsertCoinInputEditText.setText("");
                startActivity(new Intent(MainActivity.this, SelectProductActivity.class));
                break;

            default:
                break;
        }
    }

    private void showSnackbar(String pText) {
        View rootView = MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(rootView, pText, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(Color.parseColor("#125688"));
        snackbar.show();
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
