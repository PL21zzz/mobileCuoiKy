package com.manager.helo.retrofit;

import com.manager.helo.model.MessageModel;
import com.manager.helo.model.NewProductModel;
import com.manager.helo.model.OrderModel;
import com.manager.helo.model.ProductTypeModel;
import com.manager.helo.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIsellPrd {
    //GET DATA
    @GET("getTypeProduct.php")
    Observable<ProductTypeModel> getProductType();

    @GET("getNewProduct.php")
    Observable<NewProductModel> getNewProduct();

    //POST DATA
    @POST("detailProduct.php")
    @FormUrlEncoded
    Observable<NewProductModel> getNewProduct1(
            @Field("page") int page,
            @Field("type") int type
    );

    @POST("register.php")
    @FormUrlEncoded
    Observable<UserModel> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("user_name") String user_name,
            @Field("phone_number") String phone_number
    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel> login(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("order.php")
    @FormUrlEncoded
    Observable<UserModel> order(
            @Field("id_user") int id_user,
            @Field("email") String email,
            @Field("phone_number") String phone_number,
            @Field("address") String address,
            @Field("quantity") int quantity,
            @Field("totalcoin") String totalcoin,
            @Field("detail") String detail
    );

    @POST("seeOrder.php")
    @FormUrlEncoded
    Observable<OrderModel> seeOrder(
            @Field("id_user") int id_user
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<NewProductModel> search(
            @Field("search") String search
    );

    @POST("delete.php")
    @FormUrlEncoded
    Observable<MessageModel> delete(
            @Field("id") int id
    );

    @POST("insertProduct.php")
    @FormUrlEncoded
    Observable<MessageModel> insertProduct(
            @Field("product_name") String product_name,
            @Field("product_type") int product_type,
            @Field("product_image") String product_image,
            @Field("product_price") String product_price,
            @Field("product_describe") String product_describe
    );

    @POST("updateProduct.php")
    @FormUrlEncoded
    Observable<MessageModel> updateProduct(
            @Field("product_name") String product_name,
            @Field("product_type") int product_type,
            @Field("product_image") String product_image,
            @Field("product_price") String product_price,
            @Field("product_describe") String product_describe,
            @Field("id") int id
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);

}
