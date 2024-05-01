package com.example.bytebills.ui.registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegistrationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("bytebills>ui>home>HomeViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}