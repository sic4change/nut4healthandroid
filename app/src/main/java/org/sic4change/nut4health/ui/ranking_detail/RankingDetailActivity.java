package org.sic4change.nut4health.ui.ranking_detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Ranking;

import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class RankingDetailActivity extends AppCompatActivity {

    private DetailRankingViewModel mDetailRankingViewModel;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_detail);
        position = getIntent().getIntExtra("RANKING_POSITION", 0);
        RankingDetailViewModelFactory rankingDetailViewModelFactory = RankingDetailViewModelFactory.createFactory(this,
                getIntent().getStringExtra("RANKING_USERNAME"));
        mDetailRankingViewModel = ViewModelProviders.of(this, rankingDetailViewModelFactory).get(DetailRankingViewModel.class);
        mDetailRankingViewModel.getUserRanking().observe(this, new Observer<Ranking>() {
            @Override
            public void onChanged(@Nullable Ranking ranking) {
                showUserDetail(ranking);
            }
        });
    }

    private void showUserDetail(Ranking ranking) {
        CircleImageView ivUser = findViewById(R.id.ivUser);
        TextView tvUsername = findViewById(R.id.tvUsername);
        EditText etPosition = findViewById(R.id.etPosition);
        EditText etPoints = findViewById(R.id.etPoints);
        if ((ranking.getPhoto() != null) && (!ranking.getPhoto().isEmpty())) {
            Glide.with(getApplicationContext())
                    .load(ranking.getPhoto())
                    .into(ivUser);
        } else {
            Glide.with(getApplicationContext())
                    .load(getApplicationContext().getResources().getDrawable(R.mipmap.icon))
                    .into(ivUser);
        }
        tvUsername.setText(ranking.getUsername());
        etPoints.setText(ranking.getPoints() + "");
        etPosition.setText(position + "");
        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        customType(RankingDetailActivity.this,"right-to-left");
        getSupportFragmentManager().popBackStackImmediate();
    }

}
