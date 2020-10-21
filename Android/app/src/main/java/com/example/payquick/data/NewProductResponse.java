package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class NewProductResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    @SerializedName("goods_img")
    String goods_img[];

    @SerializedName("goods_name")
    String goods_name[];

    @SerializedName("color")
    String color[];

    @SerializedName("size")
    String size[];

    @SerializedName("price")
    int price[];


    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String[] getGoodsImg() { return goods_img; }
    public String[] getGoodsName() { return goods_name; }
    public String[] getColor() { return color; }
    public String[] getSize() { return size; }
    public int[] getPrice() { return price; }
}
