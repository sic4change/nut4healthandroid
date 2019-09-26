package org.sic4change.nut4health.ui.near_detail;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Near;

public class DetailNearViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<Near> mNear = null;

    public DetailNearViewModel(DataRepository repository, String id) {
        this.mRepository = repository;
        mNear = this.mRepository.getNearContract(id);
    }

    public LiveData<Near> getNear() {
        return mNear;
    }

    public void getNear(String id) {
        mRepository.getNearContract(id);
    }

    public DataRepository getRepository() {
        return mRepository;
    }

}
