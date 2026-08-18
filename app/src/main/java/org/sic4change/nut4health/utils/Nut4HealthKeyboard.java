package org.sic4change.nut4health.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nut4HealthKeyboard {

    /**
     * Method to close keyboard
     * @param editText
     * @param context
     */
    public static void closeKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * Method to open keyboard
     * @param editText
     * @param context
     */
    public static void openKeyboard(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Keeps {@code targetView} visible right above the on-screen keyboard while it has focus.
     * <p>
     * Apps targeting Android 15+ (API 35+) get edge-to-edge forced by the system, which means
     * the classic android:windowSoftInputMode="adjustResize" no longer reliably resizes the
     * window on its own: the IME height must be read from {@link WindowInsetsCompat} and applied
     * by hand as bottom padding, then the ScrollView is scrolled to the now-visible area.
     *
     * @param insetsRoot the view that receives the window insets (typically the fragment/activity root)
     * @param scrollView the ScrollView that contains targetView
     * @param targetView the field (e.g. the phone EditText) to keep visible above the keyboard
     */
    public static void keepViewAboveKeyboard(final View insetsRoot, final ScrollView scrollView, final View targetView) {
        final int initialPaddingBottom = insetsRoot.getPaddingBottom();
        final int extraPaddingPx = (int) (16 * insetsRoot.getResources().getDisplayMetrics().density);
        final int[] lastImeBottom = {0};

        ViewCompat.setOnApplyWindowInsetsListener(insetsRoot, (view, windowInsets) -> {
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
            Insets navigationInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            lastImeBottom[0] = imeInsets.bottom;
            int bottomPadding = Math.max(imeInsets.bottom, navigationInsets.bottom) + initialPaddingBottom;

            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottomPadding);

            if (imeInsets.bottom > 0 && targetView.hasFocus()) {
                scrollToKeepVisible(scrollView, targetView, extraPaddingPx);
            }

            return windowInsets;
        });

        // The insets callback above only fires when the keyboard height actually changes.
        // If the keyboard is already open and focus simply moves to targetView, catch that here.
        targetView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus && lastImeBottom[0] > 0) {
                scrollToKeepVisible(scrollView, targetView, extraPaddingPx);
            }
        });
    }

    private static void scrollToKeepVisible(final ScrollView scrollView, final View targetView, final int extraPaddingPx) {
        scrollView.post(() -> {
            int targetTop = relativeTopOf(targetView, scrollView);
            int scrollTarget = targetTop + targetView.getHeight() - scrollView.getHeight() + extraPaddingPx;
            scrollView.smoothScrollTo(0, Math.max(scrollTarget, 0));
        });
    }

    /**
     * Computes the top offset of {@code view} relative to {@code ancestor}, walking up the
     * view hierarchy and summing each intermediate view's top offset.
     */
    private static int relativeTopOf(View view, View ancestor) {
        int top = 0;
        View current = view;
        while (current != ancestor) {
            top += current.getTop();
            ViewParent parent = current.getParent();
            if (!(parent instanceof View)) {
                break;
            }
            current = (View) parent;
        }
        return top;
    }
}
