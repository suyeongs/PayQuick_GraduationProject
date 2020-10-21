package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class OrdersData {
    @SerializedName("token")
    String token;

    @SerializedName("pay_time")
    String pay_time;

    // 구매 내역 조회
    public OrdersData(String token) {
        this.token = token;
    }

    // 구매 세부 내역 조회
    public OrdersData(String token, String pay_time) {
        this.token = token;
        this.pay_time = pay_time;
    }
}
