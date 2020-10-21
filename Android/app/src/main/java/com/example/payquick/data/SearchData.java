package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class SearchData {
    @SerializedName("code_id")
    String code_id;

    public SearchData(String code_id) {
        this.code_id = code_id;
    }
}
