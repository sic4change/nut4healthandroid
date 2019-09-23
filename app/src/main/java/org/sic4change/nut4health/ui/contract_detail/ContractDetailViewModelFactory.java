package org.sic4change.nut4health.ui.contract_detail;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sic4change.nut4health.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class ContractDetailViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository mRepository;
    private final String mId;

    public static ContractDetailViewModelFactory createFactory(Activity activity, String id) {
        Context context = activity.getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }

        return new ContractDetailViewModelFactory(DataRepository.getInstance(context), id);
    }

    private ContractDetailViewModelFactory(DataRepository repository, String id) {
        mRepository = repository;
        mId = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(DataRepository.class, String.class).newInstance(mRepository, mId);
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }


}
