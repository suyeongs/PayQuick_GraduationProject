package com.example.payquick.ui.pay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CounterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pay fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
