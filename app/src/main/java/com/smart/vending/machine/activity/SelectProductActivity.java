package com.smart.vending.machine.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.vending.machine.R;
import com.smart.vending.machine.application.Application;
import com.smart.vending.machine.util.Constatns;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by MUVVASR on 3/7/2018.
 */

public class SelectProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener {

    private Button mProceedButton;
    private TextView mBalanceTextView, mInsertCoinTextView, mReturnTextView, mChangeTextView;
    private Spinner mProductSpinner;
    Application mApplication;
    CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private String mProductList[];
    private String mSelectedProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecte_product_activity);
        initViews();
        mProductList = getResources().getStringArray(R.array.products);
        mApplication = Application.mApplication;
    }

    private void initViews() {
        mBalanceTextView = (TextView) findViewById(R.id.tview_balance);
        mProceedButton = (Button) findViewById(R.id.btn_proceed);
        mProductSpinner = (Spinner) findViewById(R.id.select_product_activity_product_spinner);
        mInsertCoinTextView = (TextView) findViewById(R.id.tview_insert_coin);
        mReturnTextView = (TextView) findViewById(R.id.tview_return);
        mChangeTextView = (TextView) findViewById(R.id.tview_change);
        mProductSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.products, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProductSpinner.setAdapter(adapter);
        mProceedButton.setOnClickListener(this);
        mInsertCoinTextView.setOnClickListener(this);
        mReturnTextView.setOnClickListener(this);
        mChangeTextView.setOnClickListener(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_select_cordinator_layout);
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
            case R.id.tview_change:
                float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
                displayAlertDialog("Remaining change is "+balance+Constatns.CURRENCY_SYMBOL,false);
                mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, 0.0f);
                setBalance();
                break;
            case R.id.tview_insert_coin:
                finish();
                break;
            case R.id.tview_return:
                 float returnBalance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
                 displayAlertDialog("Returned Balance is "+returnBalance+Constatns.CURRENCY_SYMBOL,true);
                 mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, 0.0f);
                 setBalance();
                break;
            case R.id.btn_proceed:
                processProductSelction();
                break;
        }

    }

    private void processProductSelction() {
        float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
        if (mApplication != null && mApplication.mProductCountMap != null) {
            if (mApplication.mProductCountMap.get(mSelectedProduct) <= 0) {
                displayAlertDialog(mSelectedProduct + " is SOLD OUT ", false);
            } else {
                float productPrice = mApplication.mProductPriceMap.get(mSelectedProduct);
                if (balance >= productPrice) {
                    float newBalance = balance - productPrice;
                    mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, newBalance);
                    int stackCount = mApplication.mProductCountMap.get(mSelectedProduct);
                    mApplication.mProductCountMap.put(mSelectedProduct, stackCount - 1);
                    setBalance();
                    showSnackbar("THANK YOU.");
                } else if (balance == 0) {
                    finish();
                } else if (balance < productPrice) {
                    displayAlertDialog("Product Price is " + productPrice + Constatns.CURRENCY_SYMBOL + "\n" + "Balance is " + balance + Constatns.CURRENCY_SYMBOL, false);
                }
            }
        }
    }

    private void showSnackbar(String pText) {
        View rootView = SelectProductActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(rootView, pText, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(Color.parseColor("#125688"));
        snackbar.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mSelectedProduct = mProductList[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void displayAlertDialog(String pMessage, boolean isInsertButtonRequired) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(pMessage);
        builder.setPositiveButton("OK", this);
        if (isInsertButtonRequired)
            builder.setNegativeButton("Insert Coin", this);
        builder.show();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case BUTTON_POSITIVE:
                if (dialogInterface != null)
                    dialogInterface.dismiss();
                break;
            case BUTTON_NEGATIVE:
                finish();
                break;
        }

    }
}
