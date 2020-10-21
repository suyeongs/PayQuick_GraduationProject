package com.example.payquick.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payquick.R;
import com.example.payquick.data.NewProductData;
import com.example.payquick.data.NewProductResponse;
import com.example.payquick.network.RetrofitClient;
import com.example.payquick.network.ServiceApi;
import com.example.payquick.data.ProductData;
import com.example.payquick.ui.product.ProductListAdapter;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;
    CarouselView carouselView;

    private ServiceApi service;
    private String goods_name = "베이직 카라티";

    int[] sampleImages = {R.drawable.pq_app_img,R.drawable.cart_black};
    String[] sampleNetworkImageURLs = {
            "http://[ Server IP ]/images/view01.png",
            "http://[ Server IP ]/images/view02.png",
            "http://[ Server IP ]/images/view03.png",
            "http://[ Server IP ]/images/view04.png"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        // 슬라이드 이미지
        carouselView = (CarouselView) root.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleNetworkImageURLs.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "Clicked item: "+ position, Toast.LENGTH_SHORT).show();
            }
        });

        startNewProduct(new NewProductData(goods_name));

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = root.findViewById(R.id.myRecyclerView) ;
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter();
        recyclerView.setAdapter(adapter);
        //getData();

        return root;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(sampleNetworkImageURLs[position]).into(imageView);
            //imageView.setImageResource(sampleImages[position]);
        }
    };

    private void getData(NewProductResponse result) {
        String goods_name[] = result.getGoodsName();
        String color[] = result.getColor();
        String size[] = result.getSize();
        String goods_img[] = result.getGoodsImg();
        int priceInt[] = result.getPrice();
        String price[] = new String[priceInt.length];
        for (int i = 0; i < priceInt.length; i++) {
            price[i] = String.valueOf(priceInt[i]);
        }

        List<String> listName = Arrays.asList(goods_name);
        List<String> listPrice = Arrays.asList(price);
        List<String> listColor = Arrays.asList(color);
        List<String> listSize = Arrays.asList(size);
        //List<Integer> listResId = Arrays.asList(R.drawable.pq_app_img);
        List<String> listResId = Arrays.asList(goods_img);

        for (int i = 0; i < listPrice.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ProductData data = new ProductData();

            data.setName(listName.get(i));
            data.setPrice(listPrice.get(i));
            data.setColor(listColor.get(i));
            data.setSize(listSize.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    private void startNewProduct(NewProductData data) {
        service.cusNewProduct(data).enqueue(new Callback<NewProductResponse>() {

            @Override
            public void onResponse(Call<NewProductResponse> call, Response<NewProductResponse> response) {
                NewProductResponse result = response.body();

                if (result.getCode() == 200) {
                    getData(result);
                } else {
                }
            }

            @Override
            public void onFailure(Call<NewProductResponse> call, Throwable t) {
                Log.e("신제품 로드 에러 발생(C)", t.getMessage());
            }
        });

    }
}
