package com.example.payquick.data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("cus_id")
    String cus_id;

    @SerializedName("cus_pw")
    String cus_pw;

    @SerializedName("token")
    String token;

    // 로그인
    public LoginData(String cus_id, String cus_pw) {
        this.cus_id = cus_id;
        this.cus_pw = cus_pw;
    }

    // 회원정보 조회
    public LoginData(String token) {
        this.token = token;
    }
}
