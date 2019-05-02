package org.sic4change.nut4health.ui.contract_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

public class DetailContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<Contract> mContract = null;

    public DetailContractViewModel(DataRepository repository, String id) {
        this.mRepository = repository;
        mContract = this.mRepository.getContract(id);
    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void getContract(String id) {
        mRepository.getContract(id);
    }

    public DataRepository getRepository() {
        return mRepository;
    }

}
