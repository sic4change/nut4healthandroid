package org.sic4change.nut4health.utils.validators;


import android.content.Context;

import org.sic4change.nut4health.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static List<String> errorList = new ArrayList<String>();

    public static List<String> getErrors() {
        return errorList;
    }

    /**
     * Method to check if password is valid
     * @param context
     * @param password
     * @param passwordConfirmation
     * @return
     */
    public static boolean isValid(Context context, String password, String passwordConfirmation) {

        //Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern upperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        errorList.clear();

        boolean flag = true;

        if (!password.equals(passwordConfirmation)) {
            errorList.add(context.getResources().getString(R.string.password_matches));
            flag = false;
        }
        if (password.length() < 8) {
            errorList.add(context.getResources().getString(R.string.password_length));
            flag = false;
        }
        /*if (!specialCharPatten.matcher(password).find()) {
            errorList.add("Password must have atleast one specail character !!");
            flag = false;
        }*/
        /*if (!upperCasePatten.matcher(password).find()) {
            errorList.add(context.getResources().getString(R.string.password_uppercase));
            flag = false;
        }*/
        /*if (!lowerCasePatten.matcher(password).find()) {
            errorList.add(context.getResources().getString(R.string.password_lowercase));
            flag = false;
        }*/
        /*if (!digitCasePatten.matcher(password).find()) {
            errorList.add(context.getResources().getString(R.string.password_length));
            flag = false;
        }*/
        return flag;
    }

}
