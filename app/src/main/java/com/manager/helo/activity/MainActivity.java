package com.manager.helo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.helo.R;
import com.manager.helo.adapter.NewProductAdapter;
import com.manager.helo.adapter.ProductTypeAdapter;
import com.manager.helo.model.NewProduct;
import com.manager.helo.model.ProductType;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;
import com.manager.helo.Utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;

    ProductTypeAdapter productTypeAdapter;
    NewProductAdapter newProductAdapter;
    List<ProductType> arrPrdType;
    List<NewProduct> arrNewPrd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIsellPrd apisellPrd;
    NotificationBadge badge;
    FrameLayout frameCart;
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apisellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            Utils.userCurrent = Paper.book().read("user");
        }

        mapping();
        actionBar();

        if (isConnected(this)) {

            actionViewFlipper();
            getPrdType();
            getNewPrd();
            getEventClicked();
        }else {
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClicked() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        break;
                    case 1:
                        Intent phone = new Intent(getApplicationContext(), PhoneActivity.class);
                        phone.putExtra("type", 1);
                        startActivity(phone);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("type", 2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent order = new Intent(getApplicationContext(), SeeOrderActivity.class);
                        startActivity(order);
                        break;
                    case 6:
                        Intent manager = new Intent(getApplicationContext(), ManagerActivity.class);
                        startActivity(manager);
                        break;
                    case 7:
                        Paper.book().delete("user");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
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
                                        recyclerView.setAdapter(newProductAdapter);
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), "Not connect with server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        )
                );
    }

    private void getPrdType() {
        compositeDisposable
                .add(apisellPrd.getProductType()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                productTypeModel -> {
                                    if (productTypeModel.isSuccess()) {
                                        arrPrdType = productTypeModel.getResult();
                                        arrPrdType.add(new ProductType("Quản lý", ""));
                                        arrPrdType.add(new ProductType("Logout", ""));
                                        //initialization Adapter
                                        productTypeAdapter = new ProductTypeAdapter(getApplicationContext(), arrPrdType);
                                        listView.setAdapter(productTypeAdapter);
                                    }
                                }
                        ));
    }

    private void actionBar() {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationIcon(R.drawable.menu_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void mapping() {
        toolBar = findViewById(R.id.toolBar);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationView);
        listView = findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.menu_qtt);
        frameCart = findViewById(R.id.frameCart);
        imgSearch = findViewById(R.id.imgSearch);

        //initialization List product type
        arrPrdType = new ArrayList<>();
        arrNewPrd = new ArrayList<>();
        if (Utils.arrCard == null) {
            Utils.arrCard = new ArrayList<>();
        }else {
            int totalQtt = 0;
            for (int i = 0; i < Utils.arrCard.size(); i++) {
                totalQtt += Utils.arrCard.get(i).getPrdQtt();
            }
            badge.setText(String.valueOf(totalQtt));
        }
        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalQtt = 0;
        for (int i = 0; i < Utils.arrCard.size(); i++) {
            totalQtt += Utils.arrCard.get(i).getPrdQtt();
        }
        badge.setText(String.valueOf(totalQtt));
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    private void actionViewFlipper() {
        List<String> banner = new ArrayList<>();
        banner.add("https://marketingtoancau.com/files/product/thiet-ke-banner-chuyen-nghiep-cho-cua-hang-dien-thoai-nhat-nam-mobile-dqovvmz5.jpg");
        banner.add("https://baotinmobile.vn/uploads/2023/03/sl-iphone-14-promax.jpg.webp");
        banner.add("https://laptopbaominh.com/wp-content/uploads/2019/11/banner-web-2.jpg");

        for(int i = 0; i < banner.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(banner.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slideIn);
        viewFlipper.setOutAnimation(slideOut);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}