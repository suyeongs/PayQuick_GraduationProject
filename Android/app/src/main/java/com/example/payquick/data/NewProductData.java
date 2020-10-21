package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

// 임시로 보여주기
public class NewProductData {
    @SerializedName("goods_name")
    String goods_name;

    public NewProductData(String goods_name) {
        this.goods_name = goods_name;
    }
}
