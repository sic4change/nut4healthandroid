package org.sic4change.nut4health;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sic4change.nut4health.ui.create_account.CreateAccountActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityInteractionTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(CreateAccountActivity.class);

    @Test
    public void tapCreateAccountAndDisplayCreateAccountActivity() {
        onView(withId(R.id.btnBack)).perform(click());
        onView(withId(R.id.lyLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyEmailAndPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyEmailAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyUsernameAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyPasswordndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmptyRepitPasswordndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.mandatory_field))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail1AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("name@email"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail2AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("name@email..com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail3AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("@email.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail4AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("email.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadEmail5AndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("email"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.invalid_email))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.password_length))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithInvalidadRepetedPasswordAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.password_matches))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithNotCheckedTermsAndConditionsAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepj"));
        Espresso.closeSoftKeyboard();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.btnCreateAccount)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.error_terms_and_conditios))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmailThatIsUsedRAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("pepe"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        onView(withId(R.id.btnCreateAccount)).perform(click());
        pauseTestFor(500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.user_exist))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tapLogInWithEmailAndPasswordCorrectRAndDisplayErrorInSnackbar() {
        onView(withId(R.id.etEmail)).perform(typeText("aaron.asencio.tavio@gmail.com"));
        onView(withId(R.id.etUsername)).perform(typeText("aaronat"));
        onView(withId(R.id.etPassword)).perform(typeText("kiepjkiepjkiepj"));
        onView(withId(R.id.etRepeatPassword)).perform(typeText("kiepjkiepjkiepj"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.cbTerms)).perform(click());
        onView(withId(R.id.btnCreateAccount)).perform(click());
        pauseTestFor(500);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.user_exist))))
                .check(doesNotExist());
    }

    private void pauseTestFor(long milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
