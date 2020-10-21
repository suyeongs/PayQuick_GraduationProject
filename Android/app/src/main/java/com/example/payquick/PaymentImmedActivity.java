package com.example.payquick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.payquick.network.ServiceApi;

public class PaymentImmedActivity extends AppCompatActivity {
    private TextView priceTextView;
    private Button payButton;

    private ServiceApi service;
    private String barcode_id;
    private int immed_price;
    private String datetime;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //service = RetrofitClient.getClient().create(ServiceApi.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_light);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent receiveIntent = getIntent();
        barcode_id = receiveIntent.getStringExtra("barcode_id");
        immed_price = receiveIntent.getIntExtra("immed_price", 0);

        // 토큰 세팅 (결제하기 위해 회원 정보를 서버로 보낼 준비)
        //storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        //loginToken = storeToken.getString("token", null);


        priceTextView = (TextView) findViewById(R.id.pay_price);
        priceTextView.setText(String.valueOf(immed_price) + "원");
        payButton = (Button) findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentImmedActivity.this);
                builder.setTitle("상품 결제");
                builder.setMessage("결제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // 2015-04-18 00:42:24
                        // 결제 시간
                        /*
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        datetime = sdfNow.format(date);

                        Toast.makeText(getApplicationContext(), datetime, Toast.LENGTH_SHORT).show();
                        sendPay(new PayData(loginToken, barcode_id, datetime));

                         */
                        Intent sendIntent = new Intent(getApplicationContext(), SimplePayImmedActivity.class);
                        sendIntent.putExtra("barcode_id", barcode_id);
                        sendIntent.putExtra("immed_price", immed_price);
                        startActivity(sendIntent);

                        //Intent intent = new Intent(getApplicationContext(), SimplePayActivity.class);
                        //startActivity(intent);

                    }
                });
                builder.setNegativeButton("취소",  null);
                builder.create().show();
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

    /*
    private void sendPay(PayData data) {
        service.cusPay(data).enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                PayResponse result = response.body();

                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), SimplePayActivity.class);
                    startActivity(intent);
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

     */
}
