package org.sic4change.nut4health.ui.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome;

import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel mProfileViewModel;

    private TextView tvUsername;
    private TextView tvEmail;
    private CircleImageView ivUser;
    private Nut4HealthTextAwesome ivEditPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        ProfileViewModelFactory mainViewModelFactory = ProfileViewModelFactory.createFactory(this);
        mProfileViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(ProfileViewModel.class);
        mProfileViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
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
            }
        });
    }

    private void initView() {
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);
        ivUser = findViewById(R.id.ivProfileUser);
        ivEditPhoto = findViewById(R.id.ivEditPhoto);
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

}
