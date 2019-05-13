package org.sic4change.nut4health.ui.splash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.User;

public class SplashViewModel extends ViewModel {

    public static final String NAME_SHARED_PREFERENCES = "NUT4HealthPreferences";
    private static final String KEY_SELECTION = "selection";

    private Context mContext;
    private final DataRepository mRepository;
    private final LiveData<User> mUser;

    public SplashViewModel(Context context, DataRepository repository) {
        this.mContext = context;
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return mUser;
    }

    public void subscribeToTopic(String id) {
        this.mRepository.subscribeToNotificationTopic(id);
    }

    public void saveSelection(int selection) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SELECTION, selection);
        editor.apply();
    }

}
