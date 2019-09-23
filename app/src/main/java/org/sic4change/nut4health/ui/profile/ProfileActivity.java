package org.sic4change.nut4health.ui.profile;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import org.sic4change.country_selector.CountryPicker;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.login.LoginActivity;
import org.sic4change.nut4health.ui.splash.SplashActivity;
import org.sic4change.nut4health.utils.view.Nut4HealthSnackbar;
import org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome;

import de.hdodenhof.circleimageview.CircleImageView;

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
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setOutputCompressQuality(IMAGE_COMPRESS_QUALITY)
                .setAspectRatio(IMAGE_ASPECT_RATIO_X_Y, IMAGE_ASPECT_RATIO_X_Y)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(getApplicationContext())
                        .load(resultUri)
                        .into(ivUser);
                mProfileViewModel.changePhoto(tvEmail.getText().toString(), tvUsername.getText().toString(), resultUri.getPath());

            }
        }
    }

    public void showDialogEditName(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        input.setLayoutParams(lp);
        input.setHint(getString(R.string.edit_name));
        if (!tvName.getText().toString().equals(getString(R.string.edit_name))) {
            input.setText(tvName.getText().toString());
        }
        adb.setView(input);
        adb.setTitle(getString(R.string.edit_name));
        adb.setIcon(R.mipmap.icon);
        adb.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            if (input.getText() != null && !input.getText().toString().isEmpty()) {
                mProfileViewModel.updateName(tvEmail.getText().toString(), input.getText().toString());
            }
        });
        AlertDialog dialog = adb.show();
        input.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (input.getText() != null && !input.getText().toString().isEmpty()) {
                    mProfileViewModel.updateName(tvEmail.getText().toString(), input.getText().toString());
                }
                dialog.dismiss();
                return true;
            }
            return false;
        });
    }

    public void showDialogEditSurname(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        input.setLayoutParams(lp);
        input.setHint(getString(R.string.edit_surname));
        if (!tvSurname.getText().toString().equals(getString(R.string.edit_surname))) {
            input.setText(tvSurname.getText().toString());
        }
        adb.setView(input);
        adb.setTitle(getString(R.string.edit_surname));
        adb.setIcon(R.mipmap.icon);
        adb.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            if (input.getText() != null && !input.getText().toString().isEmpty()) {
                mProfileViewModel.updateSurname(tvEmail.getText().toString(), input.getText().toString());
            }
        });
        AlertDialog dialog = adb.show();
        input.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (input.getText() != null && !input.getText().toString().isEmpty()) {
                    mProfileViewModel.updateSurname(tvEmail.getText().toString(), input.getText().toString());
                }
                dialog.dismiss();
                return true;
            }
            return false;
        });
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
        mProfileViewModel.resetPassword(tvEmail.getText().toString());
        Nut4HealthSnackbar.showError(getApplicationContext(), findViewById(R.id.lyProfile), getResources().getString(R.string.sent_instructions_to_change_password));
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        customType(ProfileActivity.this,"right-to-left");
        finish();
    }

    public void logout() {
        mProfileViewModel.unsuscribeToNotification();
        mProfileViewModel.logout();
    }

    public void showDialogToLogout(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout_question)
                .setPositiveButton(R.string.ok, (dialog, which) -> logout())
                .setIcon(R.mipmap.icon)
                .show();
    }

    public void showDialogTermsAndConditions(View view) {
        String url = "https://www.sic4change.org/politica-de-privacidad";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void showDialogRemoveAccount(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_account_first_question)
                .setPositiveButton(R.string.ok, (dialog, which) -> showDialogRemoveAccountSecondQuestion())
                .setIcon(R.mipmap.icon)
                .show();
    }

    private void showDialogRemoveAccountSecondQuestion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_account_second_question)
                .setPositiveButton(R.string.ok, (dialog, which) -> removeAccount())
                .setIcon(R.mipmap.icon)
                .show();
    }

    public void removeAccount() {
        mProfileViewModel.unsuscribeToNotification();
        mProfileViewModel.removeAccount(tvEmail.getText().toString());
    }

}
