package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("cus_id")
    String cus_id;

    @SerializedName("cus_pw")
    String cus_pw;

    @SerializedName("cus_name")
    String cus_name;

    @SerializedName("cus_phone")
    String cus_phone;

    @SerializedName("token")
    String token;

    // 회원가입
    public RegisterData(String cus_id, String cus_pw, String cus_phone, String cus_name) {
        this.cus_id = cus_id;
        this.cus_pw = cus_pw;
        this.cus_phone = cus_phone;
        this.cus_name = cus_name;
    }

    // 회원정보 수정
    public RegisterData(String token, String cus_pw, String cus_phone) {
        this.token = token;
        this.cus_pw = cus_pw;
        this.cus_phone = cus_phone;
    }
}
