package com.example.payquick.ui.product;

import android.view.View;

// 리사이클러뷰 아이템 클릭 이벤트 참고 : https://everyshare.tistory.com/68, https://recipes4dev.tistory.com/168
public interface OnProductItemClickListener {
    public void onItemClick(ProductListAdapter.ItemViewHolder holder, View view, int position);
}
