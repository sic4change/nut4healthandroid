package org.sic4change.nut4health.ui.profile;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import org.sic4change.nut4health.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository mRepository;

    public static ProfileViewModelFactory createFactory(Activity activity) {
        Context context = activity.getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }

        return new ProfileViewModelFactory(DataRepository.getInstance(context));
    }

    private ProfileViewModelFactory(DataRepository repository) {
        mRepository = repository;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(DataRepository.class).newInstance(mRepository);
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }


}
