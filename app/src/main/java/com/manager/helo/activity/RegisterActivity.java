package com.manager.helo.activity;

import static com.manager.helo.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    EditText emailRegister, passwordRegister, rePassRegister, phoneNumberRegister, userNameRegister;
    Button btnRegister;
    APIsellPrd apiSellPrd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_register);
        initView();
        initControl();
    }

    private void initControl() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String email = emailRegister.getText().toString().trim();
        String pass = passwordRegister.getText().toString().trim();
        String rePass = rePassRegister.getText().toString().trim();
        String phoneNum = phoneNumberRegister.getText().toString().trim();
        String userName = userNameRegister.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(rePass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Repass!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập sdt!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập userName!", Toast.LENGTH_SHORT).show();
        }else {
            if (pass.equals(rePass)){
                //post data
                compositeDisposable.add(apiSellPrd.register(email, pass, userName, phoneNum)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
//                                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                                        Utils.userCurrent.setEmail(email);
                                        Utils.userCurrent.setPassword(pass);
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(), "pass và Repass không giống nhau!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiSellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);

        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        rePassRegister = findViewById(R.id.rePassRegister);
        phoneNumberRegister = findViewById(R.id.phoneNumberRegister);
        userNameRegister = findViewById(R.id.userNameRegister);
        btnRegister = findViewById(R.id.btnRegister);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}