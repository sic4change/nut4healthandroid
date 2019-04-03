package org.sic4change.nut4health.ui.splash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.User;

public class SplashViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;

    public SplashViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getUser();
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public void login(String email, String password) {
        mRepository.login(email, password);
    }

    public void resetPassword(String email) {
        mRepository.resetPassword(email);
    }
}
