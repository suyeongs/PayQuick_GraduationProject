package com.example.payquick;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

// 스플래시 참고 : https://yoo-hyeok.tistory.com/31
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000);  // 1초 후에 hd handler 실행 (3000ms = 3초)
    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), LoginActivity.class));  //로딩이 끝난 후, ChoiceFunction 이동
            SplashActivity.this.finish();  // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        // 화면 넘어갈때 뒤로가기 비활성화
    }
}
