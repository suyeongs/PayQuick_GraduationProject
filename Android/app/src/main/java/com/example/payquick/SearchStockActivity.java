package com.example.payquick;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.data.ProductData;
import com.example.payquick.data.ProductStockData;
import com.example.payquick.data.SearchData;
import com.example.payquick.data.SearchResponse;
import com.example.payquick.data.StockData;
import com.example.payquick.data.StockResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.ui.product.ProductListAdapter;
import com.example.payquick.ui.product.ProductStockAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchStockActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView resultRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager resultLayoutManager;
    ProductListAdapter adapter;  /**ProductStockAdapter**/
    ProductStockAdapter resultAdapter;

    private FloatingActionButton mainButtion;
    private ServiceApi service;
    private String barcode_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stock);

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

        /** 상품 검색 **/
        startSearch(new SearchData(barcode_id));
        /** 재고 검색 **/
        startStock(new StockData(barcode_id));

        // 바코드 확인
        //TextView textView = findViewById(R.id.text);
        //textView.setText(id);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.stockRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        resultLayoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setHasFixedSize(true);
        resultRecyclerView.setLayoutManager(resultLayoutManager);


        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter(); /**ProductStockAdapter**/
        recyclerView.setAdapter(adapter);

        resultAdapter = new ProductStockAdapter();
        resultRecyclerView.setAdapter(resultAdapter);
        //getData();
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
                    // 리사이클러뷰에나타내기
                    getData(result);
                } else {
                    // 존재하지않는 상품 표시하기
                }
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "검색 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("검색 에러 발생(C)", t.getMessage());
            }
        });
    }

    private void getResultData(StockResponse result) {
        String goods_name = result.getGoodsName();
        String color[] = result.getColor();
        String size[] = result.getSize();
        String goods_img[] = result.getGoodsImg();
        //int stockInt = result.getStock();
        //String stock = String.valueOf(stockInt);
        String stock[] = result.getStock();

        List<String> listName = Arrays.asList(goods_name);
        List<String> listStock = Arrays.asList(stock);
        List<String> listColor = Arrays.asList(color);
        List<String> listSize = Arrays.asList(size);
        //List<Integer> listResId = Arrays.asList(R.drawable.pq_app_img);
        List<String> listResId = Arrays.asList(goods_img);

        for (int i = 0; i < listColor.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ProductStockData data = new ProductStockData();

            data.setName(listName.get(0));
            data.setStock("남은 수량 | " + listStock.get(i));
            data.setColor("색상 | " + listColor.get(i));
            data.setSize("사이즈 | " + listSize.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            resultAdapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        resultAdapter.notifyDataSetChanged();
    }

    private void startStock(StockData data) {
        service.cusStock(data).enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                StockResponse result = response.body();

                if (result.getCode() == 200) {
                    // 리사이클러뷰에나타내기
                    getResultData(result);
                } else {
                    // 존재하지않는 상품 표시하기
                }
                //Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "검색 에러 발생(C)", Toast.LENGTH_SHORT).show();
                Log.e("검색 에러 발생(C)", t.getMessage());
            }
        });
    }
}
