package org.sic4change.nut4health.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.User;

public class ProfileViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;

    public ProfileViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return mUser;
    }

    public void changePhoto(String email, String username, String urlPhoto) {
        this.mRepository.changePhoto(email, username, urlPhoto);
    }


}
