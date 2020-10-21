package com.example.payquick.ui.mypage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyPageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mypage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
