package com.manager.helo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.databinding.ActivityAddProductBinding;
import com.manager.helo.model.MessageModel;
import com.manager.helo.model.NewProduct;
import com.manager.helo.retrofit.APIsellPrd;
import com.manager.helo.retrofit.RetrofitClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddProductActivity extends AppCompatActivity {
    Spinner spinnerType;
    ActivityAddProductBinding binding;
    APIsellPrd apiSellPrd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int type=0;
    String mediaPath;
    NewProduct newProductFix;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiSellPrd = RetrofitClient.getInstance(Utils.baseURL).create(APIsellPrd.class);
        Intent intent = getIntent();
        newProductFix = (NewProduct) intent.getSerializableExtra("fix");
        if (newProductFix == null) {
            //add
            flag = false;
        }else {
            //fix
            flag = true;
            binding.btnAdd.setText("Sửa sản phẩm");
            //show data
            binding.productNameAdd.setText(newProductFix.getProduct_name());
            binding.priceAdd.setText(newProductFix.getProduct_price());
            binding.imageAdd.setText(newProductFix.getProduct_image());
            binding.describerAdd.setText(newProductFix.getProduct_describe());
            binding.spinnerType.setSelection(newProductFix.getProduct_type());
        }

        initView();
        initData();
    }

    private void initData() {
        List<String> typeList = new ArrayList<>();
        typeList.add("Điện thoại");
        typeList.add("Laptop");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    addProduct();
                }else {
                    fixProduct();
                }
            }
        });
        binding.cameraAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddProductActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    private void fixProduct() {
        String prdName = Objects.requireNonNull(binding.productNameAdd.getText()).toString().trim();
        String prdPrice = Objects.requireNonNull(binding.priceAdd.getText()).toString().trim();
        String prdDescribe = Objects.requireNonNull(binding.describerAdd.getText()).toString().trim();
        String prdImage = Objects.requireNonNull(binding.imageAdd.getText()).toString().trim();

        if (TextUtils.isEmpty(prdName) || TextUtils.isEmpty(prdPrice) || TextUtils.isEmpty(prdDescribe) || TextUtils.isEmpty(prdImage)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiSellPrd.updateProduct(prdName, (type+1), prdImage, prdPrice, prdDescribe, newProductFix.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult: " + mediaPath);
    }

    private void addProduct() {
        String prdName = Objects.requireNonNull(binding.productNameAdd.getText()).toString().trim();
        String prdPrice = Objects.requireNonNull(binding.priceAdd.getText()).toString().trim();
        String prdDescribe = Objects.requireNonNull(binding.describerAdd.getText()).toString().trim();
        String prdImage = Objects.requireNonNull(binding.imageAdd.getText()).toString().trim();

        if (TextUtils.isEmpty(prdName) || TextUtils.isEmpty(prdPrice) || TextUtils.isEmpty(prdDescribe) || TextUtils.isEmpty(prdImage)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiSellPrd.insertProduct(prdName, (type+1), prdImage, prdPrice, prdDescribe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        File file = new File(getPath(uri));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MessageModel> call = apiSellPrd.uploadFile(fileToUpload1);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Response<MessageModel> response, Retrofit retrofit) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.imageAdd.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("log", t.getMessage());
            }

        });
    }

    private void initView() {
        spinnerType = findViewById(R.id.spinnerType);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}