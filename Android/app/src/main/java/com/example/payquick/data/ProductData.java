package com.example.payquick.data;

public class ProductData {
    private String name;
    private String price;
    private String color;
    private String size;
    //private int resId;
    private String resId;

    private String code_id;
    public String getCodeId() {
        return code_id;
    }
    public void setCodeId(String code_id) {
        this.code_id = code_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

/*
    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

 */
    public String getResId() {
    return resId;
}

    public void setResId(String resId) {
        this.resId = resId;
    }
}