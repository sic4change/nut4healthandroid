package org.sic4change.nut4health;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.sic4change.nut4health.utils.validators.PasswordValidator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PasswordValidatorTest {

    private Context context;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void getContext() {
       context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void passwordValidator_CorrectPasswordAndConfirmation_ReturnsTrue() {
        assertTrue(PasswordValidator.isValid(context,"TestPassword_1234", "TestPassword_1234"));
    }

    @Test
    public void passwordValidator_InCorrectPasswordAndConfirmation_ReturnsTrue() {
        assertFalse(PasswordValidator.isValid(context,"TestPassword_12345", "TestPassword_1234"));
    }

    @Test
    public void passwordValidator_InvalidLenght_ReturnsFalse() {
        assertFalse(PasswordValidator.isValid(context,"TestA_1", "TestA_1"));
    }

}
