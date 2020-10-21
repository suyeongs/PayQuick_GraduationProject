package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class StockData {
    @SerializedName("code_id")
    String code_id;

    public StockData(String code_id) {
        this.code_id = code_id;
    }
}
