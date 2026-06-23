package com.stepstone.stepper.internal.widget.pagetransformer;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.stepstone.stepper.R;


/**
 * Creates a page transformer to be used by {@link com.stepstone.stepper.internal.widget.StepViewPager}.
 *
 * @author Piotr Zawadzki
 */
@RestrictTo(LIBRARY)
public final class StepPageTransformerFactory {

    private StepPageTransformerFactory() {
    }

    /**
     * Creates a {@link android.support.v4.view.ViewPager.PageTransformer}.
     * If layout direction is in RTL it returns {@link StepperRtlPageTransformer}, <i>null</i> otherwise.
     * @param context context
     * @return page transformer
     */
    @Nullable
    public static ViewPager.PageTransformer createPageTransformer(@NonNull Context context) {
        boolean rtlEnabled = context.getResources().getBoolean(R.bool.ms_rtlEnabled);
        return rtlEnabled ? new StepperRtlPageTransformer() : null;
    }

}
