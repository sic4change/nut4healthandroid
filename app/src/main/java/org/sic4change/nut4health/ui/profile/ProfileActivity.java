package org.sic4change.nut4health.ui.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome;

import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel mProfileViewModel;

    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvSurname;
    private CircleImageView ivUser;
    private Nut4HealthTextAwesome ivEditPhoto;
    private CircleImageView ivProfileUser;

    public static final int IMAGE_COMPRESS_QUALITY = 100;
    public static final int IMAGE_ASPECT_RATIO_X_Y = 3;


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
            }
        });
    }

    private void showProfileData(User user) {
        tvEmail.setText(user.getEmail());
        tvUsername.setText(user.getUsername());
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
    }

    private void initView() {
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);
        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        ivUser = findViewById(R.id.ivProfileUser);
        ivEditPhoto = findViewById(R.id.ivEditPhoto);
        ivProfileUser = findViewById(R.id.ivProfileUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        customType(ProfileActivity.this,"right-to-left");
        finish();
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
        adb.setView(input);
        adb.setTitle(getString(R.string.edit_name));
        adb.setIcon(R.drawable.icon);
        adb.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mProfileViewModel.updateName(tvEmail.getText().toString(), input.getText().toString());
        });
        adb.show();
    }

    public void showDialogEditSurname(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        input.setLayoutParams(lp);
        input.setHint(getString(R.string.edit_surname));
        adb.setView(input);
        adb.setTitle(getString(R.string.edit_surname));
        adb.setIcon(R.drawable.icon);
        adb.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            mProfileViewModel.updateSurname(tvEmail.getText().toString(), input.getText().toString());
        });
        adb.show();
    }
}