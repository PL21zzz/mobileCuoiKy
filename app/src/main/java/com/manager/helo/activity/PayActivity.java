package com.manager.helo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayActivity extends AppCompatActivity {
    Toolbar toolBarPay;
    TextView txtTotalCoin, txtSdt, txtEmailPay, txtUserPay;
    EditText edtAddress;
    Button btnPay;
    long totalCoin;
    APIsellPrd apiSellPrd;
    int totalQtt;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalQtt = 0;
        for (int i = 0; i < Utils.arrBuyCard.size(); i++) {
            totalQtt += Utils.arrBuyCard.get(i).getPrdQtt();
        }
    }

    private void initControl() {
        setSupportActionBar(toolBarPay);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBarPay.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalCoin  = getIntent().getLongExtra("total Coin", 0);
        txtTotalCoin.setText(decimalFormat.format(totalCoin));
        txtEmailPay.setText(Utils.userCurrent.getEmail());
        txtSdt.setText(Utils.userCurrent.getPhone_number());
        txtUserPay.setText(Utils.userCurrent.getUser_name());

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else {
                    String email = Utils.userCurrent.getEmail();
                    String sdt = Utils.userCurrent.getPhone_number();
                    int id_user = Utils.userCurrent.getId();

                    compositeDisposable.add(apiSellPrd.order(id_user, email, sdt, address, totalQtt, String.valueOf(totalCoin), new Gson().toJson(Utils.arrBuyCard))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Utils.arrBuyCard.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        //Log.d("loggg", throwable.getMessage());
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiSellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);

        toolBarPay = findViewById(R.id.toolBarPay);
        txtTotalCoin = findViewById(R.id.txtTotalCoin);
        txtSdt = findViewById(R.id.txtSdt);
        txtEmailPay = findViewById(R.id.txtEmailPay);
        txtUserPay = findViewById(R.id.txtUserPay);
        edtAddress = findViewById(R.id.edtAddress);
        btnPay = findViewById(R.id.btnPay);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}