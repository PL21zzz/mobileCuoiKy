package com.manager.helo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.adapter.PhoneAdapter;
import com.manager.helo.model.NewProduct;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LaptopActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    APIsellPrd apisellPrd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PhoneAdapter phoneAdapter;
    List<NewProduct> arrNewPrd;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int type, page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        apisellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        type = getIntent().getIntExtra("type", 2);

        mapping();
        actionToolbar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrNewPrd.size()-1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                arrNewPrd.add(null);
                phoneAdapter.notifyItemInserted(arrNewPrd.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                arrNewPrd.remove(arrNewPrd.size()-1);
                phoneAdapter.notifyItemRemoved(arrNewPrd.size());
                page += 1;
                getData(page);
                phoneAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(
                apisellPrd.getNewProduct1(page, type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                newProductModel -> {
                                    if (newProductModel.isSuccess()) {
                                        if (phoneAdapter == null) {
                                            arrNewPrd = newProductModel.getResult();
                                            phoneAdapter = new PhoneAdapter(getApplicationContext(), arrNewPrd);
                                            recyclerView.setAdapter(phoneAdapter);
                                        }else {
                                            int lc = arrNewPrd.size()-1;
                                            int qttAdd = newProductModel.getResult().size();
                                            for (int i = 0; i < qttAdd; i++) {
                                                arrNewPrd.add(newProductModel.getResult().get(i));
                                            }
                                            phoneAdapter.notifyItemRangeInserted(lc, qttAdd);
                                        }
                                    }else {
                                        isLoading = true;
                                    }
                                },
                                throwable -> {
//                                    Log.d("loggg", Objects.requireNonNull(throwable.getMessage()));
                                    Toast.makeText(getApplicationContext(), "Can not connect to server!!", Toast.LENGTH_LONG).show();
                                }
                        )
        );
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolBarIP);
        recyclerView = findViewById(R.id.recycleViewIP);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        arrNewPrd = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}