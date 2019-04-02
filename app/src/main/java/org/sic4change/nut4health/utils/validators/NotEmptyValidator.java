package org.sic4change.nut4health.utils.validators;


import android.text.Editable;
import android.text.TextWatcher;


public class NotEmptyValidator implements TextWatcher {


    private boolean mIsValid = false;

    public boolean isValid() {
        return mIsValid;
    }

    /**
     * Method to check if text is empty
     * @param text
     * @return
     */
    public static boolean isValid(CharSequence text) {
        return text != null && !text.toString().isEmpty() && !text.toString().trim().isEmpty();
    }

    @Override
    final public void afterTextChanged(Editable editableText) {
        mIsValid = isValid(editableText);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}