package com.example.payquick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.data.LoginData;
import com.example.payquick.data.LoginResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button editButton;
    private ServiceApi service;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        idEditText = (EditText) findViewById(R.id.register_id);
        pwEditText = (EditText) findViewById(R.id.register_pw);
        phoneEditText = (EditText) findViewById(R.id.register_phone);
        nameEditText = (EditText) findViewById(R.id.register_name);

        editButton = (Button) findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_light);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 토큰 세팅 (회원정보 불러오기)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);

        /** 회원정보 조회 **/
        checkAccount(new LoginData(loginToken));
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

    private void checkAccount(LoginData data) {
        service.cusLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();

                if (result.getCode() == 200) {
                    idEditText.setText(result.getCusId());
                    pwEditText.setText("ex)password");
                    phoneEditText.setText(result.getCusPhone());
                    nameEditText.setText(result.getCusName());
                } else {
                    // 404 에러
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "회원정보 로드 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("회원정보 로드 에러 발생(C)", t.getMessage());
            }
        });
    }
}
