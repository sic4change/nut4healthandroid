package org.sic4change.nut4health.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;

public class MainViewModel extends ViewModel {

    public static final String NAME_SHARED_PREFERENCES = "NUT4HealthPreferences";
    private static final String KEY_SELECTION = "selection";
    private static final String KEY_CONTRACT_SELECTION = "contract_selection";

    private Context mContext;
    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private LiveData<PagedList<Contract>> mContracts;
    private LiveData<PagedList<Ranking>> mRanking;

    public MainViewModel(Context context, DataRepository repository) {
        this.mContext = context;
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();
        mContracts = this.mRepository.getSortedContracts("DATE", "");
        mRanking = this.mRepository.getSortedRanking("POINTS");
    }

    public LiveData<User> getCurrentUser() {
        return mUser;
    }

    public void updateUser(String email) {
        this.mRepository.updateUser(email);
    }

    public void saveMainMenuSelection(int selection) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SELECTION, selection);
        editor.apply();
    }

    public int getMainMenuSelection(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_SELECTION, 1);
    }

    public void saveContractViewMenuSelection(int selection) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_CONTRACT_SELECTION, selection);
        editor.apply();
    }

    public int getContractViewMenuSelection(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CONTRACT_SELECTION, 1);
    }

    public void getContracts(String email) {
        this.mRepository.getContracts(email);
    }

    public void getSortedContracts(String sort, String status) {
        mContracts = this.mRepository.getSortedContracts(sort, status);
    }

    public LiveData<PagedList<Contract>> getContracts() {
        return mContracts;
    }

    public void getRankingUser() {
        this.mRepository.getRanking();
    }

    public LiveData<PagedList<Ranking>> getRanking() {
        return mRanking;
    }

}
