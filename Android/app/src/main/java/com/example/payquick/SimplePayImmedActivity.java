package com.example.payquick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.payquick.data.PayData;
import com.example.payquick.data.PayResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimplePayImmedActivity extends AppCompatActivity {
    private EditText pwEditText;
    private Button closeButton;

    private ServiceApi service;
    private String barcode_id;
    private int immed_price;
    private String datetime;

    SharedPreferences storeToken;
    String loginToken;
    String pw;
    String pw_check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pay);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Intent receiveIntent = getIntent();
        barcode_id = receiveIntent.getStringExtra("barcode_id");
        immed_price = receiveIntent.getIntExtra("immed_price", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_light);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 토큰 세팅 (결제하기 위해 회원 정보를 서버로 보낼 준비)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);

        pwEditText = (EditText) findViewById(R.id.simple_pw);

        closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw = pwEditText.getText().toString();
                pw_check = "456789";
                if (pw.equals(pw_check)) {  // 간편결제 비밀번호 일치시 결제 처리
                    // 결제 시간
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    datetime = sdfNow.format(date);

                    //Toast.makeText(getApplicationContext(), datetime, Toast.LENGTH_SHORT).show();
                    sendPay(new PayData(loginToken, barcode_id, datetime));

                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    // 툴바 뒤로가기
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendPay(PayData data) {
        service.cusPay(data).enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                PayResponse result = response.body();

                if (result.getCode() == 200) {
                    /*
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "구매 내역은 MyPage > Orders 에서 확인할 수 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    // 결제 오류 (이미 구매?)
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "결제 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("결제 에러 발생(C)", t.getMessage());
            }
        });
    }
}
