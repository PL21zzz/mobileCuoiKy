package com.manager.helo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    Toolbar toolBarSearch;
    RecyclerView recycleViewSearch;
    EditText edtSearch;
    APIsellPrd apisellPrd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PhoneAdapter phoneAdapter;
    List<NewProduct> arrNewPrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        actionToolbar();
    }

    private void initView() {
        apisellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        arrNewPrd = new ArrayList<>();
        toolBarSearch = findViewById(R.id.toolBarSearch);
        recycleViewSearch = findViewById(R.id.recycleViewSearch);
        edtSearch = findViewById(R.id.edtSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleViewSearch.setLayoutManager(linearLayoutManager);
        recycleViewSearch.setHasFixedSize(true);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    arrNewPrd.clear();
                    phoneAdapter = new PhoneAdapter(getApplicationContext(), arrNewPrd);
                    recycleViewSearch.setAdapter(phoneAdapter);
                }else {
                    getDataSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getDataSearch(String search) {
        arrNewPrd.clear();
        compositeDisposable.add(apisellPrd.search(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if (newProductModel.isSuccess()){
                                arrNewPrd = newProductModel.getResult();
                                phoneAdapter = new PhoneAdapter(getApplicationContext(), arrNewPrd);
                                recycleViewSearch.setAdapter(phoneAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void actionToolbar() {
        setSupportActionBar(toolBarSearch);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}