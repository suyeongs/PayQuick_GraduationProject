package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("cus_id")
    private String cus_id;

    @SerializedName("cus_pw")
    private String cus_pw;

    @SerializedName("cus_phone")
    private String cus_phone;

    @SerializedName("cus_name")
    private String cus_name;

    @SerializedName("token")
    private String token;


    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String getCusId() {
        return cus_id;
    }
    public String getCusPw() { return cus_pw; }
    public String getCusPhone() { return cus_phone; }
    public String getCusName() { return cus_name; }
    public String getToken() { return token; }
}
