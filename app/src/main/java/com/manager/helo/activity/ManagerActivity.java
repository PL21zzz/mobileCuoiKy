package com.manager.helo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.adapter.NewProductAdapter;
import com.manager.helo.model.NewProduct;
import com.manager.helo.model.eventBus.FixDelEvent;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphCardView;

public class ManagerActivity extends AppCompatActivity {

    ImageView imgAdd;
    RecyclerView recyclerViewAdd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIsellPrd apisellPrd;
    List<NewProduct> arrNewPrd;
    NewProductAdapter newProductAdapter;
    NewProduct newProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        apisellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        initView();
        initControl();
        getNewPrd();
    }

    private void initControl() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNewPrd() {
        compositeDisposable
                .add(apisellPrd.getNewProduct()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                newProductModel -> {
                                    if (newProductModel.isSuccess()) {
                                        arrNewPrd = newProductModel.getResult();
                                        newProductAdapter = new NewProductAdapter(getApplicationContext(), arrNewPrd);
                                        recyclerViewAdd.setAdapter(newProductAdapter);
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), "Not connect with server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        )
                );
    }

    private void initView() {
        imgAdd = findViewById(R.id.imgAdd);
        recyclerViewAdd = findViewById(R.id.recyclerViewAdd);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAdd.setHasFixedSize(true);
        recyclerViewAdd.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Sửa")) {
            FixProduct();
        }else if (item.getTitle().equals("Xóa")) {
            DelProduct();
        }

        return super.onContextItemSelected(item);
    }

    private void DelProduct() {
        compositeDisposable.add(apisellPrd.delete(newProduct.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                getNewPrd();
                            }else {
                                Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void FixProduct() {
        Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
        intent.putExtra("fix", newProduct);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventFixDel(FixDelEvent e) {
        newProduct = e.getNewProduct();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}