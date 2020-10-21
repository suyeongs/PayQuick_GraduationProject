package com.example.payquick.network;

import com.example.payquick.data.CartData;
import com.example.payquick.data.CartResponse;
import com.example.payquick.data.LoginData;
import com.example.payquick.data.LoginResponse;
import com.example.payquick.data.NewProductData;
import com.example.payquick.data.NewProductResponse;
import com.example.payquick.data.OrdersData;
import com.example.payquick.data.OrdersResponse;
import com.example.payquick.data.PayData;
import com.example.payquick.data.PayResponse;
import com.example.payquick.data.RegisterData;
import com.example.payquick.data.RegisterResponse;
import com.example.payquick.data.RestoreData;
import com.example.payquick.data.RestoreResponse;
import com.example.payquick.data.SearchData;
import com.example.payquick.data.SearchResponse;
import com.example.payquick.data.StockData;
import com.example.payquick.data.StockResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/customers/login")
    Call<LoginResponse> cusLogin(@Body LoginData data);

    @POST("/customers/register")
    Call<RegisterResponse> cusRegister(@Body RegisterData data);

    @POST("/goods/search")
    Call<SearchResponse> cusSearch(@Body SearchData data);

    @POST("/goods/stock")
    Call<StockResponse> cusStock(@Body StockData data);

    @POST("/goods/cart")
    Call<CartResponse> cusCart(@Body CartData data);

    @POST("/goods/pay")
    Call<PayResponse> cusPay(@Body PayData data);

    @POST("/goods/orders")
    Call<OrdersResponse> cusOrders(@Body OrdersData data);

    @POST("/goods/new")
    Call<NewProductResponse> cusNewProduct(@Body NewProductData data);

    @POST("/goods/restore")
    Call<RestoreResponse> cusRestore(@Body RestoreData data);
}