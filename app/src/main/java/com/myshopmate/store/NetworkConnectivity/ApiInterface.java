package com.myshopmate.store.NetworkConnectivity;

import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.Select_Product_Model;
import com.myshopmate.store.Model.TransactionsResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BaseURL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("store_transaction")
    @FormUrlEncoded
    Call<TransactionsResponse> getTransactions(@Field("id") String storeID);

    @POST("save_products")
    @FormUrlEncoded
    Call<JSONObject> changeSelectedProducts(@Field("store_id") String storeID, @Field("products") String products);
}
