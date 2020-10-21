package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class CartResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    @SerializedName("code_id")
    private String code_id[];

    @SerializedName("goods_img")
    private String goods_img[];

    @SerializedName("goods_name")
    private String goods_name[];

    @SerializedName("color")
    private String color[];

    @SerializedName("size")
    private String size[];

    @SerializedName("price")
    private int price[];

    @SerializedName("len")
    private int len;

    @SerializedName("sum")
    private int sum;


    public int getCode() { return code; }
    public String getMessage() { return message; }
    public int getLen() { return len; }
    public int getSum() { return sum; }

    public String[] getCodeId() { return code_id; }
    public String[] getGoodsImg() { return goods_img; }
    public String[] getGoodsName() { return goods_name; }
    public String[] getColor() { return color; }
    public String[] getSize() { return size; }
    public int[] getPrice() { return price; }
}
