package org.sic4change.nut4health.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;

public class LoginViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final MutableLiveData<Boolean> mLogued = new MutableLiveData<>();

    public LoginViewModel(DataRepository repository) {
        this.mRepository = repository;
    }

    public LiveData<Boolean> isLogued() {
        return mLogued;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mLogued.setValue(false);
        } else {
            mRepository.login(email, password);
        }
    }

}
