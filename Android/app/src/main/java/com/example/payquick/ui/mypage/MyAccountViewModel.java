package com.example.payquick.ui.mypage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyAccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is My Account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
