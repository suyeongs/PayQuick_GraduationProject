package com.example.payquick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.payquick.data.LoginData;
import com.example.payquick.data.LoginResponse;
import com.example.payquick.data.RegisterData;
import com.example.payquick.data.RegisterResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button completeButton;
    private ServiceApi service;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        idEditText = (EditText) findViewById(R.id.register_id);
        idEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "이메일 계정은 수정이 불가합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        pwEditText = (EditText) findViewById(R.id.modify_pw);
        phoneEditText = (EditText) findViewById(R.id.modify_phone);
        nameEditText = (EditText) findViewById(R.id.register_name);
        nameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "이름은 수정이 불가합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        completeButton = (Button) findViewById(R.id.complete);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("회원정보 수정");
                builder.setMessage("수정하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = pwEditText.getText().toString();
                        String phonenum = phoneEditText.getText().toString();

                        if (password.isEmpty() || phonenum.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            /** 회원정보 수정 **/
                            modifyAccount(new RegisterData(loginToken, password, phonenum));
                        }
                    }
                });
                builder.setNegativeButton("취소",  null);
                builder.create().show();
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

    private void modifyAccount(RegisterData data) {
        service.cusRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();

                if (result.getCode() == 200) {
                    //finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    // 404 에러
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "회원정보 수정 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("회원정보 수정 에러 발생(C)", t.getMessage());
            }
        });
    }

    private void checkAccount(LoginData data) {
        service.cusLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();

                if (result.getCode() == 200) {
                    idEditText.setText(result.getCusId());
                    pwEditText.setText(result.getCusPw());
                    phoneEditText.setText(result.getCusPhone());
                    nameEditText.setText(result.getCusName());
                } else {
                    // 404 에러
                }
                //Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "회원정보 로드 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("회원정보 로드 에러 발생(C)", t.getMessage());
            }
        });
    }

}
