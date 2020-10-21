package com.example.payquick.ui.mypage;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payquick.R;
import com.example.payquick.data.MyPageData;
import com.example.payquick.data.ProductData;

import java.util.ArrayList;

public class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<MyPageData> listData = new ArrayList<>();

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;

    public MyPageAdapter(OnListItemSelectedInterface listener){
        this.mListener = listener;
    }


    @NonNull
    @Override
    public MyPageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_item, parent, false);
        return new MyPageAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(MyPageData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.m_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                    Log.d("test", "position = "+ getAdapterPosition());

                }
            });
        }

        void onBind(MyPageData data) {
            this.title.setText(data.getTitle());
        }
    }
}