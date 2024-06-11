package com.manager.helo.activity;

import static com.manager.helo.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.adapter.CartAdapter;
import com.manager.helo.model.eventBus.TotalEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    TextView txtCardNull, txtTotalPrice;
    Toolbar toolBarCard;
    RecyclerView recycleViewCard;
    Button btnBuy;
    CartAdapter cartAdapter;
    long totalCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_cart);
        initView();
        initControl();
        calTotalPrice();
    }
    @SuppressLint("SetTextI18n")
    private void calTotalPrice() {
        totalCoin = 0;
        for (int i = 0; i < Utils.arrBuyCard.size(); i++) {
            totalCoin += Utils.arrBuyCard.get(i).getPrdPrice() * Utils.arrBuyCard.get(i).getPrdQtt();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotalPrice.setText(decimalFormat.format(totalCoin) + "Đ");
    }

    private void initControl() {
        setSupportActionBar(toolBarCard);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBarCard.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycleViewCard.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleViewCard.setLayoutManager(layoutManager);
        if (Utils.arrCard.size() == 0) {
            txtCardNull.setVisibility(View.VISIBLE);
        }else {
            cartAdapter = new CartAdapter(getApplicationContext(), Utils.arrCard);
            recycleViewCard.setAdapter(cartAdapter);
        }
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                intent.putExtra("total Coin", totalCoin);
                Utils.arrCard.clear();
                startActivity(intent);
            }
        });

    }

    private void initView() {
        txtCardNull = findViewById(id.txtCardNull);
        txtTotalPrice = findViewById(id.txtTotalPrice);
        toolBarCard = findViewById(id.toolBarCard);
        recycleViewCard = findViewById(id.recycleViewCard);
        btnBuy = findViewById(id.btnBuy);
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void totalEvent(TotalEvent event) {
        if (event != null) {
            calTotalPrice();
        }
    }
}