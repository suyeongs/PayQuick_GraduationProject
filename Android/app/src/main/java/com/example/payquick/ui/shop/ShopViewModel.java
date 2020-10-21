package com.example.payquick.ui.shop;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ShopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shop fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}