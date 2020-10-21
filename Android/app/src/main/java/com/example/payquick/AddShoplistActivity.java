package com.example.payquick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.data.CartData;
import com.example.payquick.data.CartResponse;
import com.example.payquick.data.ProductData;
import com.example.payquick.data.SearchData;
import com.example.payquick.data.SearchResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.ui.product.ProductListAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShoplistActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;

    private FloatingActionButton mainButtion;
    private ServiceApi service;
    private String barcode_id;
    private int immed_price;
    private Boolean empty;

    SharedPreferences storeToken;
    String loginToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shoplist);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        mainButtion = (FloatingActionButton) findViewById(R.id.mainButton);
        mainButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_light);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent receiveIntent = getIntent();
        barcode_id = receiveIntent.getStringExtra("barcode_id");
        startSearch(new SearchData(barcode_id));  // 바코드 서버로 보내기

        // 바코드 확인용
        //TextView textView = findViewById(R.id.text);
        //textView.setText(barcode_id);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.addshopRecyclerView) ;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter();
        recyclerView.setAdapter(adapter);
        //getData();


        // 토큰 세팅 (장바구니에 담기 위해 회원 정보를 서버로 보낼 준비)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);

        empty = false;

        // 장바구니 담기
        Button cartButton = (Button) findViewById(R.id.cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empty) {
                    Toast.makeText(getApplicationContext(), "장바구니에 담을 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (loginToken != null)
                    addToCart(new CartData(loginToken, barcode_id));
                else {
                    Log.d("토큰 내용 : ", loginToken);
                }
            }
        });

        // 바로 결제하기
        Button payButton = (Button) findViewById(R.id.pay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empty)  {
                    Toast.makeText(getApplicationContext(), "결제할 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sendIntent = new Intent(getApplicationContext(), PaymentImmedActivity.class);
                    sendIntent.putExtra("barcode_id", barcode_id);
                    sendIntent.putExtra("immed_price", immed_price);
                    startActivity(sendIntent);
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

    private void getData(SearchResponse result) {
        String goods_name = result.getGoodsName();
        String color = result.getColor();
        String size = result.getSize();
        String goods_img = result.getGoodsImg();
        String price = Integer.toString(result.getPrice());

        List<String> listName = Arrays.asList(goods_name);
        List<String> listPrice = Arrays.asList(price);
        List<String> listColor = Arrays.asList(color);
        List<String> listSize = Arrays.asList(size);
        //List<Integer> listResId = Arrays.asList(R.drawable.pq_app_img);
        List<String> listResId = Arrays.asList(goods_img);

        for (int i = 0; i < listName.size(); i++) {
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

    private void startSearch(SearchData data) {
        service.cusSearch(data).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse result = response.body();

                if (result.getCode() == 200) {
                    // 리사이클러뷰에 나타내기
                    immed_price = result.getPrice();
                    getData(result);
                } else if (result.getCode() == 204){
                    // 존재하지않는 상품 표시하기
                    empty = true;
                } else {
                    // 404 에러
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "검색 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("검색 에러 발생", t.getMessage());
            }
        });
    }

    private void addToCart(CartData data) {
        service.cusCart(data).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                CartResponse result = response.body();

                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), ShoplistActivity.class);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "장바구니에 담았습니다. 장바구니로 이동하시겠습니까? (다이얼로그 띄우기)", Toast.LENGTH_SHORT).show();
                } else if (result.getCode() == 204) {
                    // 안내창 띄우기
                } else {
                    //Toast.makeText(getApplicationContext(), "장바구니에 담지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "장바구니 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("장바구니 에러 발생(C)", t.getMessage());
            }
        });
    }
}
