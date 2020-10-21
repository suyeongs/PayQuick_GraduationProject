package com.example.payquick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.payquick.data.CartData;
import com.example.payquick.data.RegisterData;
import com.example.payquick.data.RegisterResponse;
import com.example.payquick.data.RestoreData;
import com.example.payquick.data.RestoreResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button signupButton;
    private ImageView restoreImageView;
    private ServiceApi service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        idEditText = (EditText) findViewById(R.id.register_id);
        pwEditText = (EditText) findViewById(R.id.register_pw);
        phoneEditText = (EditText) findViewById(R.id.register_phone);
        nameEditText = (EditText) findViewById(R.id.register_name);

        signupButton = (Button) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        // 서비스 시연시, 구매한 상품 모두 재고 테이블로 되돌리기
        restoreImageView = (ImageView) findViewById(R.id.logo_white);
        restoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("DB 복구");
                builder.setMessage("구매한 상품 모두 되돌리기");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restoreDatabase(new RestoreData(true));
                    }
                });
                builder.setNegativeButton("취소",  null);
                builder.create().show();

            }
        });

        idEditText.requestFocus();
        idEditText.setCursorVisible(true);
    }

    private void attemptRegister() {
        String id = idEditText.getText().toString();
        String pw = pwEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (!id.contains("@")) {
            Toast.makeText(getApplicationContext(), "이메일 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            startRegister(new RegisterData(id, pw, phone, name));
        }
    }

    private void startRegister(RegisterData data) {
        service.cusRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();

                if (result.getCode() == 200) {
                    finish();
                } else {
                    // 404 에러
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "회원가입 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생(C)", t.getMessage());
            }
        });
    }

    private void restoreDatabase(RestoreData data) {
        service.cusRestore(data).enqueue(new Callback<RestoreResponse>() {
            @Override
            public void onResponse(Call<RestoreResponse> call, Response<RestoreResponse> response) {
                RestoreResponse result = response.body();

                if (result.getCode() == 200) {
                    // 데이터베이스 복구
                } else {
                    // 404 에러
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<RestoreResponse> call, Throwable t) {

            }
        });
    }
}
