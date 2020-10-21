package com.example.payquick.ui.stock;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class StockViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is stock fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}