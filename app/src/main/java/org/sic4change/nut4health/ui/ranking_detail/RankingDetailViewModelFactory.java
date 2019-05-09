package org.sic4change.nut4health.ui.ranking_detail;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import org.sic4change.nut4health.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class RankingDetailViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository mRepository;
    private final String mUsername;

    public static RankingDetailViewModelFactory createFactory(Activity activity, String username) {
        Context context = activity.getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }

        return new RankingDetailViewModelFactory(DataRepository.getInstance(context), username);
    }

    private RankingDetailViewModelFactory(DataRepository repository, String username) {
        mRepository = repository;
        mUsername = username;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(DataRepository.class, String.class).newInstance(mRepository, mUsername);
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }


}
