package com.example.payquick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.data.OrdersData;
import com.example.payquick.data.OrdersResponse;
import com.example.payquick.data.ProductData;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.ui.product.ProductListAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;

    private TextView countTextView;
    private TextView priceTextView;
    private ServiceApi service;
    private String pay_datetime;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_detail);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        countTextView = (TextView) findViewById(R.id.order_detail_count);
        priceTextView = (TextView) findViewById(R.id.order_detail_price);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_light);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.ordersRecyclerView) ;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter();
        recyclerView.setAdapter(adapter);

        // 토큰 세팅 (구매 세부 내역 확인하기 위해 회원 정보를 서버로 보낼 준비)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);


        Intent receiveIntent = getIntent();
        pay_datetime = receiveIntent.getStringExtra("pay_datetime");


        /** 구매 내역 확인하기 **/
        checkOrdersDetail(new OrdersData(loginToken, pay_datetime));
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


    private void getData(OrdersResponse result) {
        String goods_name[] = result.getGoodsName();
        String color[] = result.getColor();
        String size[] = result.getSize();
        int priceInt[] = result.getPrice();
        String price[] = new String[priceInt.length];
        for (int i = 0; i < priceInt.length; i++) {
            price[i] = String.valueOf(priceInt[i]);
        }
        String goods_img[] = result.getGoodsImg();

        List<String> listName = Arrays.asList(goods_name);
        List<String> listColor = Arrays.asList(color);
        List<String> listSize = Arrays.asList(size);
        List<String> listPrice = Arrays.asList(price);
        List<String> listResId = Arrays.asList(goods_img);

        for (int i = 0; i < listPrice.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ProductData data = new ProductData();

            data.setName(listName.get(i));
            data.setPrice("가격 | " + listPrice.get(i));
            data.setColor("색상 | " + listColor.get(i));
            data.setSize("사이즈 | " + listSize.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    private void checkOrdersDetail(OrdersData data) {
        service.cusOrders(data).enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                OrdersResponse result = response.body();

                if (result.getCode() == 200) {
                    getData(result);
                    countTextView.setText("  총 " + String.valueOf(result.getLen())  + "개");
                    priceTextView.setText(String.valueOf(result.getSum()) + "원  ");
                } else {
                    // 404 에러
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "구매 세부 내역 확인 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("구매 세부 내역 확인 에러 발생(C)", t.getMessage());
            }
        });
    }
}