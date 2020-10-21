package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class OrdersResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    @SerializedName("goods_img")
    private String goods_img[];

    @SerializedName("goods_name")
    private String goods_name[];

    @SerializedName("count")
    private int count[];

    @SerializedName("pay_time")
    private String pay_time[];

    @SerializedName("price")
    private int price[];

    @SerializedName("color")
    private String color[];

    @SerializedName("size")
    private String size[];

    @SerializedName("len")
    private int len;

    @SerializedName("sum")
    private int sum;


    public int getCode() { return code; }
    public String getMessage() { return message; }

    public int getLen() { return len; }
    public int getSum() { return sum; }

    public String[] getGoodsImg() { return goods_img; }
    public String[] getGoodsName() { return goods_name; }
    public int[] getCount() { return count; }
    public String[] getPayTime() { return pay_time; }
    public int[] getPrice() { return price; }
    public String[] getColor() { return color; }
    public String[] getSize() { return size; }
}
