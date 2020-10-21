package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class PayData {
    @SerializedName("token")
    String token;

    @SerializedName("code_id")
    String code_id;

    @SerializedName("pay_time")
    String pay_time;


    // 바로 결제
    public PayData(String token, String code_id, String pay_time) {
        this.token = token;
        this.code_id = code_id;
        this.pay_time = pay_time;
    }

    // 장바구니 결제
    public PayData(String token, String pay_time) {
        this.token = token;
        this.pay_time = pay_time;
    }
}
