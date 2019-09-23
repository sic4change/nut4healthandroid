package org.sic4change.nut4health.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sic4change.nut4health.data.DataRepository;

import java.lang.reflect.InvocationTargetException;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final DataRepository mRepository;
    private final Context mContext;

    public static SplashViewModelFactory createFactory(Activity activity) {
        Context context = activity.getApplicationContext();
        if (context == null) {
            throw new IllegalStateException("Not yet attached to Application");
        }

        return new SplashViewModelFactory(context, DataRepository.getInstance(context));
    }

    private SplashViewModelFactory(Context context, DataRepository repository) {
        mContext = context;
        mRepository = repository;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(Context.class, DataRepository.class).newInstance(mContext, mRepository);
        } catch (InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }


}
