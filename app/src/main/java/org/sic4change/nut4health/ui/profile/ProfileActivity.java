package org.sic4change.nut4health.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;


import org.sic4change.country_selector.CountryPicker;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;
import org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel mProfileViewModel;

    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvRole;
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvCountry;
    private CircleImageView ivUser;
    private Nut4HealthTextAwesome ivEditPhoto;
    private CircleImageView ivProfileUser;

    public static final int IMAGE_COMPRESS_QUALITY = 100;
    public static final int IMAGE_ASPECT_RATIO_X_Y = 3;

    private boolean created = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        ProfileViewModelFactory mainViewModelFactory = ProfileViewModelFactory.createFactory(this);
        mProfileViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(ProfileViewModel.class);
        mProfileViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                showProfileData(user);
                if (!created) {
                    mProfileViewModel.updateUser(user.getEmail());
                    created = true;
                }
            } else {
                goToLoginActivity();
            }
        });
    }

    private void showProfileData(User user) {
        tvEmail.setText(user.getEmail());
        tvUsername.setText(user.getUsername());
        tvRole.setText(user.getRole());
        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(user.getPhoto())
                    .into(ivUser);
            ivEditPhoto.setVisibility(View.GONE);
        } else {
            ivEditPhoto.setVisibility(View.VISIBLE);
        }
        if (user.getName() != null && !user.getName().isEmpty()) {
            tvName.setText(user.getName());
        }
        if (user.getSurname() != null && !user.getSurname().isEmpty()) {
            tvSurname.setText(user.getSurname());
        }
        if (user.getCountry() != null && !user.getCountry().isEmpty()
            && user.getCountryCode() != null && !user.getCountryCode().isEmpty()) {
            tvCountry.setText(user.getCountry() + ",(" +  user.getCountryCode() + ")");
        }
    }

    private void initView() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);
        tvRole = findViewById(R.id.tvRole);
        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        tvCountry = findViewById(R.id.tvCountry);
        ivUser = findViewById(R.id.ivProfileUser);
        ivEditPhoto = findViewById(R.id.ivEditPhoto);
        ivProfileUser = findViewById(R.id.ivProfileUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    private void goToMainActivity() {
        customType(ProfileActivity.this,"right-to-left");
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void changePhoto(View view) {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(ivUser);
            mProfileViewModel.changePhoto(tvEmail.getText().toString(), tvUsername.getText().toString(), uri.getPath());
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialogEditName(View view) {
        new AndExAlertDialog.Builder(this)
                .setMessage(getString(R.string.edit_name))
                .setPositiveBtnText(getString(R.string.ok))
                .setCancelableOnTouchOutside(true)
                .OnPositiveClicked(input -> {
                    if (input != null && !input.isEmpty()) {
                         mProfileViewModel.updateName(tvEmail.getText().toString(), input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase());
                    }
                })
                .setImage(R.mipmap.ic_launcher, 15)
                .setEditText(true, false, tvName.getText().toString(), ir.androidexception.andexalertdialog.InputType.TEXT_SINGLE_LINE)
                .setMessageTextColor(getResources().getColor(R.color.ms_black_38_opacity))
                .setButtonTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .build();
    }

    public void showDialogEditSurname(View view) {
        new AndExAlertDialog.Builder(this)
                .setMessage(getString(R.string.edit_surname))
                .setPositiveBtnText(getString(R.string.ok))
                .setCancelableOnTouchOutside(true)
                .OnPositiveClicked(input -> {
                    if (input != null && !input.isEmpty()) {
                        mProfileViewModel.updateSurname(tvEmail.getText().toString(), input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase());
                    }
                })
                .setImage(R.mipmap.ic_launcher, 15)
                .setEditText(true, false, tvSurname.getText().toString(), ir.androidexception.andexalertdialog.InputType.TEXT_SINGLE_LINE)
                .setMessageTextColor(getResources().getColor(R.color.ms_black_38_opacity))
                .setButtonTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .build();
    }

    public void showDialogEditCountry(View view) {
        CountryPicker picker = CountryPicker.getInstance("", (name, code) -> {
            tvCountry.setText(name + ", (" + code + ")");
            DialogFragment dialogFragment =
                    (DialogFragment) getSupportFragmentManager().findFragmentByTag("CountryPicker");
            mProfileViewModel.updateCountry(tvEmail.getText().toString(), name, code);
            dialogFragment.dismiss();
        });
        picker.show(getSupportFragmentManager(), "CountryPicker");

    }

    public void showToastChangePassword(View view) {
        new AwesomeInfoDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.sent_instructions_to_change_password))
                .setPositiveButtonText(getResources().getString(R.string.ok))
                .setPositiveButtonClick(() -> {
                    mProfileViewModel.resetPassword(tvEmail.getText().toString());
                    new AwesomeSuccessDialog(this)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(getResources().getString(R.string.sent_instructions_to_change_password_ok))
                            .setPositiveButtonText(getResources().getString(R.string.ok))
                            .setPositiveButtonClick(() -> {

                            })
                            .show();
                }).show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        customType(ProfileActivity.this,"right-to-left");
        finishAffinity();
    }

    public void logout() {
        mProfileViewModel.unsuscribeToNotification();
        mProfileViewModel.logout();
    }

    public void showDialogToLogout(View view) {
        new AwesomeWarningDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.logout_question))
                .setButtonText(getResources().getString(R.string.logout))
                .setWarningButtonClick(() -> {
                   logout();
                }).show();
    }

    public void showDialogTermsAndConditions(View view) {
        String url = "https://www.sic4change.org/politica-de-privacidad";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void showDialogRemoveAccount(View view) {
        new AwesomeErrorDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.delete_account_first_question))
                .setButtonText(getResources().getString(R.string.delete_account))
                .setErrorButtonClick(() -> {
                    showDialogRemoveAccountSecondQuestion();
                }).show();
    }

    private void showDialogRemoveAccountSecondQuestion() {
        new AwesomeErrorDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.delete_account_second_question))
                .setButtonText(getResources().getString(R.string.delete_account))
                .setErrorButtonClick(() -> {
                    removeAccount();
                }).show();
    }

    public void removeAccount() {
        mProfileViewModel.unsuscribeToNotification();
        mProfileViewModel.removeAccount(tvEmail.getText().toString());
    }

}
