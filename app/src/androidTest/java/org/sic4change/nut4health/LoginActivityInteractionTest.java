package org.sic4change.nut4health;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sic4change.nut4health.ui.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginActivityInteractionTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void tapCreateAccountAndDisplayCreateAccountActivity() {
        onView(withId(R.id.lyCreateAccount)).perform(click());
        onView(withId(R.id.lyCreateAccount)).check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyEmailAndPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyEmailAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail1AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("name@email"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail2AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("name@email..com"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail3AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("@email.com"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail4AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("email.com"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail5AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("email"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadPasswordLengthAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("1234567"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.password_length))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithIncorrectPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepjkiepj"));
        onView(withId(R.id.btnLogin)).perform(click());
        pauseTestFor(500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.incorrect_user_or_password))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapForgotPasssowridWithEmptyEmailAndDisplayErrorInSnackbar() {
        onView(withId(R.id.tvResetPassword)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapForgotPasssowridWithInvalidEmailAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.taviogmail.com"));
        onView(withId(R.id.tvResetPassword)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapForgotPasssowridWithValiddEmailAndDisplayInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.tvResetPassword)).perform(click());
        pauseTestFor(500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.sent_instructions_to_change_password))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithCorrectEmailAndPasswordAndGoToMainActivity() {
        Intents.init();
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("laycotavio"));
        onView(withId(R.id.btnLogin)).perform(click());
        pauseTestFor(500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.sent_instructions_to_change_password))))
                .check(doesNotExist());
    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
