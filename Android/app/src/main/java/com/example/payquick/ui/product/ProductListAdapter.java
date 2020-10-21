package com.example.payquick.ui.product;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.payquick.R;
import com.example.payquick.data.ProductData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ItemViewHolder> implements OnProductItemClickListener {
    // adapter에 들어갈 list 입니다.
    private ArrayList<ProductData> listData = new ArrayList<>();

    OnProductItemClickListener listener; //

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(ProductData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // 리사이클러뷰 항목 클릭
    public void setOnItemClickListener(OnProductItemClickListener listener) {
        this.listener = listener;
    }

    // 리사이클러뷰 항목 클릭
    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView p_name;
        private TextView p_price;
        private TextView p_color;
        private TextView p_size;
        private ImageView p_image;

        private String p_code_id;
        private LinearLayout p_layout;

        ItemViewHolder(View itemView) {
            super(itemView);

            p_name = itemView.findViewById(R.id.p_name);
            p_price = itemView.findViewById(R.id.p_price);
            p_color = itemView.findViewById(R.id.p_color);
            p_size = itemView.findViewById(R.id.p_size);
            p_image = itemView.findViewById(R.id.p_image);

            p_layout = itemView.findViewById(R.id.p_layout);

            // 리사이클러뷰 항목 클릭
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemClick(ItemViewHolder.this, v, position);
                            //p_layout.setBackgroundColor(Color.RED);
                        }
                    }
                }
            });

        }

        void onBind(final ProductData data) {
            p_name.setText(data.getName());
            p_price.setText(data.getPrice());
            p_color.setText(data.getColor());
            p_size.setText(data.getSize());
            //p_image.setImageResource(data.getResId());
            Picasso.get().load(data.getResId()).into(p_image);

            p_code_id = data.getCodeId();
        }
    }

    /*
    public ProductData getColor(int position) {
        return listData.get(position);
    }
     */

    // 구매 내역 상세보기
    public ProductData getName(int position) { return listData.get(position); }

    //public ProductData getSize(int position) { return  listData.get(position); }

    // 장바구니 삭제
    public ProductData getCodeId(int position) {
        return listData.get(position);
    }
}