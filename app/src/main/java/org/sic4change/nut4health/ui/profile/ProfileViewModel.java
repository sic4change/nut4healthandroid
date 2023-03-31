package org.sic4change.nut4health.ui.profile;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.User;

public class ProfileViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<User> mUser;

    public ProfileViewModel(DataRepository repository) {
        this.mRepository = repository;

    }

    public void init() {
        mUser = this.mRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return mUser;
    }

    public void updateUser(String email) {
        this.mRepository.updateUser(email);
    }

    public void changePhoto(String email, String username, String urlPhoto) {
        this.mRepository.changePhoto(email, username, urlPhoto);
    }

    public void updateName(String email, String name) {
        this.mRepository.changeName(email, name);
    }

    public void updateSurname(String email, String surname) {
        this.mRepository.changeSurname(email, surname);
    }

    public void updateCountry(String email, String country, String countryCode) {
        this.mRepository.changeCountry(email, country, countryCode);
    }

    public void resetPassword(String email) {
        this.mRepository.resetPassword(email);
    }

    public void logout() {
        this.mRepository.logout();
    }

    public void removeAccount(String email) {
        this.mRepository.removeAccount(email);
    }

    public void unsuscribeToNotification() {
        this.mRepository.unsubscribeToNotificationTopic(mUser.getValue().getId());
        this.mRepository.unsubscribeToNotificationTopic(mUser.getValue().getUsername());
        this.mRepository.unsubscribeToNotificationTopic(mUser.getValue().getCurrentCountry());
        this.mRepository.unsubscribeToNotificationTopic(mUser.getValue().getCurrentState());
        this.mRepository.unsubscribeToNotificationTopic(mUser.getValue().getCurrentCity());
    }
}
