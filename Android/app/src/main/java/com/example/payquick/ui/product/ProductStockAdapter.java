package com.example.payquick.ui.product;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payquick.R;
import com.example.payquick.data.ProductStockData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductStockAdapter extends RecyclerView.Adapter<ProductStockAdapter.ItemViewHolder>{
    private ArrayList<ProductStockData> listData = new ArrayList<>();

    @NonNull
    @Override
    public ProductStockAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_item, parent, false);
        return new ProductStockAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductStockAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(ProductStockData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView p_name;
        private TextView p_stock;
        private TextView p_color;
        private TextView p_size;
        private ImageView p_image;

        ItemViewHolder(View itemView) {
            super(itemView);

            p_name = itemView.findViewById(R.id.p_name);
            p_stock = itemView.findViewById(R.id.p_price);
            p_color = itemView.findViewById(R.id.p_color);
            p_size = itemView.findViewById(R.id.p_size);
            p_image = itemView.findViewById(R.id.p_image);
        }

        void onBind(ProductStockData data) {
            p_name.setText(data.getName());
            p_stock.setText(data.getStock());
            p_color.setText(data.getColor());
            p_size.setText(data.getSize());
            //p_image.setImageResource(data.getResId());
            Picasso.get().load(data.getResId()).into(p_image);
        }
    }
}