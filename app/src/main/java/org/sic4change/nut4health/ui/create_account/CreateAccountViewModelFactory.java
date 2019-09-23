package org.sic4change.nut4health.ui.create_account;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sic4change.nut4health.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class CreateAccountViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository mRepository;

    public static CreateAccountViewModelFactory createFactory(Activity activity) {
        Context context = activity.getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }

        return new CreateAccountViewModelFactory(DataRepository.getInstance(context));
    }

    private CreateAccountViewModelFactory(DataRepository repository) {
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
