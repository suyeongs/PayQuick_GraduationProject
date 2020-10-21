package com.example.payquick.ui.mypage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.payquick.R;
import com.example.payquick.data.ProductData;
import com.example.payquick.ui.pay.CounterViewModel;
import com.example.payquick.ui.product.ProductListAdapter;

import java.util.Arrays;
import java.util.List;

public class MyOrdersFragment extends Fragment {
    private MyOrdersViewModel MyOrdersViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyOrdersViewModel =
                ViewModelProviders.of(this).get(MyOrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);

        final TextView textView = root.findViewById(R.id.text_myorder);
        MyOrdersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = root.findViewById(R.id.myorderRecyclerView) ;
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ProductListAdapter();
        recyclerView.setAdapter(adapter);
        getData();

        return root;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라");
        List<String> listContent = Arrays.asList(
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다."
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.pq_app_img
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ProductData data = new ProductData();
            //data.setName(listTitle.get(i));
            data.setName("바지");
            data.setPrice("10000원");
            data.setColor("Black");
            data.setSize("M");
            // data.setPrice(listContent.get(i));
            //data.setColor(listContent.get(i));
            //data.setSize( listContent.get(i));
            //data.setResId(listResId.get(0));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}