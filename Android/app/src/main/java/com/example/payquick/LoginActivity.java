package com.example.payquick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText pwEditText;
    private Button signinButton;
    private TextView signupTextView;
    private ServiceApi service;

    SharedPreferences storeToken;
    String loginToken;
    String nameToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        idEditText = (EditText) findViewById(R.id.login_id);
        pwEditText = (EditText) findViewById(R.id.login_pw);

        signinButton = (Button) findViewById(R.id.signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signupTextView = (TextView) findViewById(R.id.signup_intent);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 토큰
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);
        nameToken = storeToken.getString("name", null);
        if (loginToken != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private void attemptLogin() {
        String id = idEditText.getText().toString();
        String pw = pwEditText.getText().toString();

        if (id.isEmpty() || pw.isEmpty()) {
            Toast.makeText(getApplicationContext(), "모두 입력해주세요.", Toast.LENGTH_SHORT).show();

            /** 나중에 지울 것 **/
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);

        } else {
            startLogin(new LoginData(id, pw));
        }
    }

    private void startLogin(LoginData data) {
        service.cusLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();

                if (result.getCode() == 200) {
                    // 로그인 토큰
                    SharedPreferences.Editor editor = storeToken.edit();
                    editor.putString("token", result.getToken());
                    editor.putString("name", result.getCusName());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "로그인 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생(C)", t.getMessage());

                /** 나중에 지울 것 **/
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });

    }
}
