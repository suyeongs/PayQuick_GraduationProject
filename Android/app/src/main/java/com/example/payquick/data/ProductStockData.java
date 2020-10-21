package com.example.payquick.data;

public class ProductStockData {
    private String name;
    private String price;
    private String color;
    private String size;
    private String stock;
    private String resId;

    public String getStock() { return stock; }

    public void setStock(String stock) { this.stock = stock; }

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

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}
