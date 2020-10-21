package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class CartData {
    @SerializedName("token")
    String token;

    @SerializedName("code_id")
    String code_id;

    @SerializedName("del")
    Boolean del;

    // 항목 삭제
    public CartData(String token, String code_id, Boolean del) {
        this.token = token;
        this.code_id = code_id;
        this.del = del;
    }

    // 장바구니 담기
    public CartData(String token, String code_id) {
        this.token = token;
        this.code_id = code_id;
    }

    // 장바구니 보기
    public CartData(String token) {
        this.token = token;
    }
}
