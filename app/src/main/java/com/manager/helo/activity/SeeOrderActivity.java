package com.manager.helo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.adapter.OrderAdapter;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SeeOrderActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIsellPrd apiSellPrd;
    RecyclerView recycleViewOrder;
    Toolbar toolBarOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order);
        initView();
        actionToolBar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable
                .add(apiSellPrd.seeOrder(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), orderModel.getResult());
                            recycleViewOrder.setAdapter(orderAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void actionToolBar() {
        setSupportActionBar(toolBarOrder);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBarOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiSellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        recycleViewOrder = findViewById(R.id.recycleViewOrder);
        toolBarOrder = findViewById(R.id.toolBarOrder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleViewOrder.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}