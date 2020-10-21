package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class RestoreData {
    @SerializedName("request")
    Boolean request;

    public RestoreData(Boolean request) {
        this.request = request;
    }
}
