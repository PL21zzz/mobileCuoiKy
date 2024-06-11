package com.manager.helo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.model.Cart;
import com.manager.helo.model.NewProduct;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    TextView prdName, prdPrice, prdDescribe;
    Button btnAddCard;
    ImageView imgDetail;
    androidx.appcompat.widget.Toolbar toolbar;
    NewProduct newProduct;
    NotificationBadge badge;
    FrameLayout frameCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        actionToolbar();
        initData();
        initControl();
    }

    private void initControl() {
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
            }
        });
    }

    private void addCard() {
        if (Utils.arrCard.size() > 0) {
            int quantity = 1;
            boolean flag = false;
            for (int i = 0; i < Utils.arrCard.size(); i++) {
                if (Utils.arrCard.get(i).getPrdId() == newProduct.getId()) {
                    Utils.arrCard.get(i).setPrdQtt(quantity);
                    long price = Long.parseLong(newProduct.getProduct_price());
                    Utils.arrCard.get(i).setPrdPrice(price);
                    flag = true;
                }
            }
            if (!flag) {
                long price = Long.parseLong(newProduct.getProduct_price());
                Cart card = new Cart();
                card.setPrdQtt(quantity);
                card.setPrdPrice(price);
                card.setPrdId(newProduct.getId());
                card.setPrdName(newProduct.getProduct_name());
                card.setPrdImage(newProduct.getProduct_image());
                Utils.arrCard.add(card);
            }
        }else {
            int quantity = 1;
            long price = Long.parseLong(newProduct.getProduct_price()) * quantity;
            Cart card = new Cart();
            card.setPrdQtt(quantity);
            card.setPrdPrice(price);
            card.setPrdId(newProduct.getId());
            card.setPrdName(newProduct.getProduct_name());
            card.setPrdImage(newProduct.getProduct_image());
            Utils.arrCard.add(card);

        }
        int totalQtt = 0;
        for (int i = 0; i < Utils.arrCard.size(); i++) {
            totalQtt += Utils.arrCard.get(i).getPrdQtt();
        }
        badge.setText(String.valueOf(totalQtt));
    }

    private void initData() {
        newProduct = (NewProduct) getIntent().getSerializableExtra("detail");
        assert newProduct != null;
        prdName.setText(newProduct.getProduct_name());
        prdDescribe.setText(newProduct.getProduct_describe());
        String getPrdPrice = newProduct.getProduct_price() + "Đ";
        Glide.with(this).load(newProduct.getProduct_image()).into(imgDetail);
        prdPrice.setText(getPrdPrice);
    }

    private void initView() {
        prdName = findViewById(R.id.txtPrdNameDT);
        prdPrice = findViewById(R.id.txtPrdPriceDT);
        prdDescribe = findViewById(R.id.txtDescribeDetail);
        imgDetail = findViewById(R.id.imageDetail);
        btnAddCard = findViewById(R.id.btnAddCard);
        toolbar = findViewById(R.id.toolBarDetail);
        badge = findViewById(R.id.menu_qtt);
        frameCart = findViewById(R.id.frameCart);
        frameCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        if (Utils.arrCard != null) {
            int totalQtt = 0;
            for (int i = 0; i < Utils.arrCard.size(); i++) {
                totalQtt += Utils.arrCard.get(i).getPrdQtt();
            }
            badge.setText(String.valueOf(totalQtt));
        }
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

    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.arrCard != null) {
            int totalQtt = 0;
            for (int i = 0; i < Utils.arrCard.size(); i++) {
                totalQtt += Utils.arrCard.get(i).getPrdQtt();
            }
            badge.setText(String.valueOf(totalQtt));
        }
    }
}