package org.sic4change.nut4health.ui.create_account;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.User;

public class CreateAccountViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;

    public CreateAccountViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getUser();
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public void createUser(String email, String username, String password, String role) {
        mRepository.createUser(email, username, password, role);
    }

}
