package com.example.payquick.ui.mypage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyOrdersViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myorder fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
