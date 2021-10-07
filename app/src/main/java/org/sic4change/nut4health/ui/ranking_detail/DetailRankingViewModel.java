package org.sic4change.nut4health.ui.ranking_detail;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Ranking;

public class DetailRankingViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<Ranking> mRanking = null;

    public DetailRankingViewModel(DataRepository repository, String username) {
        this.mRepository = repository;
        mRanking = this.mRepository.getUserRanking(username);
    }

    public LiveData<Ranking> getUserRanking() {
        return mRanking;
    }

    public void getUserRanking(String username) {
        mRepository.getUserRanking(username);
    }

    public DataRepository getRepository() {
        return mRepository;
    }

}
