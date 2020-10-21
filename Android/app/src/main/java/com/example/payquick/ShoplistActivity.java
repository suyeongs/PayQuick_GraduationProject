package com.example.payquick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.ui.product.OnProductItemClickListener;
import com.example.payquick.ui.product.ProductListAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoplistActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;

    private TextView countTextView;
    private TextView priceTextView;
    private Button payButton;
    private FloatingActionButton mainButtion;
    private ServiceApi service;
    private int price_intent;

    SharedPreferences storeToken;
    String loginToken;
    Boolean del;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        countTextView = (TextView) findViewById(R.id.cart_count);
        priceTextView = (TextView) findViewById(R.id.cart_price);
        payButton = (Button) findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price_intent == 0) {  // 장바구니에 담긴 상품이 없을 때
                    Toast.makeText(ShoplistActivity.this, "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sendIntent = new Intent(getApplicationContext(), PaymentCartActivity.class);
                    sendIntent.putExtra("price_sum", price_intent);
                    startActivity(sendIntent);
                }
            }
        });
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

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.shopRecyclerView) ;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter();
        recyclerView.setAdapter(adapter);
        //getData();

        // 토큰 세팅 (장바구니 확인하기 위해 회원 정보를 서버로 보낼 준비)
        storeToken = getSharedPreferences("store", Activity.MODE_PRIVATE);
        loginToken = storeToken.getString("token", null);


        /** 장바구니 항목 삭제 **/
        deleteCart();

        /** 장바구니 보기 **/
        checkCart(new CartData(loginToken));

    }

    private void deleteCart() {
        del = false;  // 상품 삭제 여부 세팅
        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductListAdapter.ItemViewHolder holder, View view, int position) {
                final ProductData item = adapter.getCodeId(position);
                //Toast.makeText(ShoplistActivity.this, "바코드 선택 : " + item.getCodeId(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ShoplistActivity.this);
                builder.setTitle("상품 삭제");
                builder.setMessage("확인을 누르시면 삭제됩니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        del = true;
                        checkCart(new CartData(loginToken, item.getCodeId(), del));
                        //Toast.makeText(getApplicationContext(), item.getCodeId(), Toast.LENGTH_SHORT).show();
                        del = false;

                        // 상품 삭제후 새로고침
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
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

    private void getData(CartResponse result) {
        String goods_name[] = result.getGoodsName();
        String color[] = result.getColor();
        String size[] = result.getSize();
        int priceInt[] = result.getPrice();
        String price[] = new String[priceInt.length];
        for (int i = 0; i < priceInt.length; i++) {
            price[i] = String.valueOf(priceInt[i]);
        }
        String goods_img[] = result.getGoodsImg();
        String code_id[] = result.getCodeId();

        List<String> listName = Arrays.asList(goods_name);
        List<String> listColor = Arrays.asList(color);
        List<String> listSize = Arrays.asList(size);
        List<String> listPrice = Arrays.asList(price);
        List<String> listResId = Arrays.asList(goods_img);
        List<String> listCodeId = Arrays.asList(code_id);

        for (int i = 0; i < listPrice.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ProductData data = new ProductData();

            data.setName(listName.get(i));
            data.setPrice("가격 | " + listPrice.get(i));
            data.setColor("색상 | " + listColor.get(i));
            data.setSize("사이즈 | " + listSize.get(i));
            data.setResId(listResId.get(i));
            data.setCodeId(listCodeId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    private void checkCart(CartData data) {
        service.cusCart(data).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                CartResponse result = response.body();

                if (result.getCode() == 200) {
                    getData(result);
                    price_intent = result.getSum();
                    countTextView.setText("  총 " + String.valueOf(result.getLen())  + "개");
                    priceTextView.setText(String.valueOf(result.getSum()) + "원  ");
                } else if (result.getCode() == 204) {
                    // 장바구니 비어있음
                    countTextView.setText("  총 " + "0개");
                    priceTextView.setText("0" + "원  ");

                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // 404 에러
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "장바구니 확인 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("장바구니 확인 에러 발생(C)", t.getMessage());
            }
        });
    }
}
