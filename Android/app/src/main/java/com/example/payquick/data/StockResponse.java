package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class StockResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    @SerializedName("goods_img")
    String goods_img[];

    @SerializedName("goods_name")
    String goods_name;

    @SerializedName("color")
    String color[];

    @SerializedName("size")
    String size[];

    /*
    @SerializedName("stock")
    int stock;
     */

    @SerializedName("stock")
    String stock[];


    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String[] getGoodsImg() { return goods_img; }
    public String getGoodsName() { return goods_name; }
    public String[] getColor() { return color; }
    public String[] getSize() { return size; }
    //public int getStock() { return stock; }
    public String[] getStock() { return stock; }
}
