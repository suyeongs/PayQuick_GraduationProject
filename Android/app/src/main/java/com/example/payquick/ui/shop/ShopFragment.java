package com.example.payquick.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.example.payquick.R;

public class ShopFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        WebView mWebView = (WebView) view.findViewById(R.id.shop_web);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.elandmall.com/main/initMain.action");

        mWebView.setWebViewClient(new WebViewClient());  // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings();  // 세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true);  // 웹페이지 자바스크립트 허용 여부
        mWebSettings.setSupportMultipleWindows(false);  // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false);  // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true);  // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true);  // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false);  // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false);  // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);  // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(false);  // 로컬저장소 허용 여부

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    /*
    private ShopViewModel shopViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shopViewModel =
                ViewModelProviders.of(this).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        final TextView textView = root.findViewById(R.id.text_shop);
        shopViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
     */
}
