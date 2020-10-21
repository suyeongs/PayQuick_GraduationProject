package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class SearchResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    @SerializedName("code_id")
    String code_id;

    @SerializedName("goods_img")
    String goods_img;

    @SerializedName("goods_name")
    String goods_name;

    @SerializedName("color")
    String color;

    @SerializedName("size")
    String size;

    @SerializedName("price")
    int price;


    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String getCodeId() { return code_id; }
    public String getGoodsImg() { return goods_img; }
    public String getGoodsName() { return goods_name; }
    public String getColor() { return color; }
    public String getSize() { return size; }
    public int getPrice() { return price; }
}
