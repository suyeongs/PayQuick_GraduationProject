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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.data.OrdersData;
import com.example.payquick.data.OrdersResponse;
import com.example.payquick.data.ProductData;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.ui.product.OnProductItemClickListener;
import com.example.payquick.ui.product.ProductListAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;

    private TextView countTextView;
    private TextView priceTextView;
    private ServiceApi service;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        countTextView = (TextView) findViewById(R.id.order_count);
        priceTextView = (TextView) findViewById(R.id.order_price);

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

        // 토큰 세팅 (구매 내역 확인하기 위해 회원 정보를 서버로 보낼 준비)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);


        /** 구매 세부 내역으로 이동 **/
        moveOrdersDetail();
        /** 구매 내역 확인하기 **/
        checkOrders(new OrdersData(loginToken));
    }

    private void moveOrdersDetail() {
        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductListAdapter.ItemViewHolder holder, View view, int position) {
                final ProductData item = adapter.getName(position);
                //Toast.makeText(getApplicationContext(), "결제 일시 : " + item.getName(), Toast.LENGTH_SHORT).show();

                String pay_datetime = item.getName();
                Intent sendIntent = new Intent(getApplicationContext(), MyOrdersDetailActivity.class);
                sendIntent.putExtra("pay_datetime", pay_datetime);
                startActivity(sendIntent);
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

    private void getData(OrdersResponse result) {
        String goods_img[] = result.getGoodsImg();
        String goods_name[] = result.getGoodsName();
        int countInt[] = result.getCount();
        String count[] = new String[countInt.length];
        for (int i = 0; i < countInt.length; i++) {
            count[i] = String.valueOf(countInt[i] - 1);
        }
        String pay_time[] = result.getPayTime();
        int priceInt[] = result.getPrice();
        String price[] = new String[priceInt.length];
        for (int i = 0; i < priceInt.length; i++) {
            price[i] = String.valueOf(priceInt[i]);
        }

        List<String> listResId = Arrays.asList(goods_img);
        List<String> listName = Arrays.asList(goods_name);
        List<String> listCount = Arrays.asList(count);
        List<String> listPayTime = Arrays.asList(pay_time);
        List<String> listPrice = Arrays.asList(price);

        for (int i = 0; i < listName.size(); i++) {
            ProductData data = new ProductData();

            data.setResId(listResId.get(i));
            data.setName(listPayTime.get(i));
            if (listCount.get(i).equals("0")) {
                data.setColor("구매 상품 | " + listName.get(i));
            } else {
                data.setColor("구매 상품 | " + listName.get(i) + " 외 " + listCount.get(i) + "건");
            }
            data.setSize("구매 금액 | " + listPrice.get(i));

            /*
            data.setResId(listResId.get(i));
            if (listCount.get(i).equals("0")) {
                data.setName(listName.get(i));
            } else {
                data.setName(listName.get(i) + " 외 " + listCount.get(i) + "건");
            }
            data.setColor("결제 금액 | " + listPrice.get(i));
            data.setSize("결제 일시 | " + listPayTime.get(i));
             */
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

    private void checkOrders(OrdersData data) {
        service.cusOrders(data).enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                OrdersResponse result = response.body();

                if (result.getCode() == 200) {
                    getData(result);
                    countTextView.setText("  총 " + String.valueOf(result.getLen()) + "건");
                    priceTextView.setText(String.valueOf(result.getSum()) + "원  ");
                } else if (result.getCode() == 204) {
                    // 구매 내역 비어있음
                    countTextView.setText("  총 " + "0건");
                    priceTextView.setText("0" + "원  ");

                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // 404 에러
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "구매 내역 확인 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("구매 내역 확인 에러 발생(C)", t.getMessage());
            }
        });
    }
}