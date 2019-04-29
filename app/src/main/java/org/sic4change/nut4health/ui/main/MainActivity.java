package org.sic4change.nut4health.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.main.contracts.ContractFragment;
import org.sic4change.nut4health.ui.main.contracts.ContractsListFragment;
import org.sic4change.nut4health.ui.main.contracts.ContractsMapFragment;
import org.sic4change.nut4health.ui.main.ranking.RankingFragment;
import org.sic4change.nut4health.ui.profile.ProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        EmptyFragment.OnFragmentInteractionListener, ContractsListFragment.OnFragmentInteractionListener,
        ContractsMapFragment.OnFragmentInteractionListener, RankingFragment.OnFragmentInteractionListener{

    private boolean doubleBackToExitPressedOnce = false;

    FragmentManager fragmentManager;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private LinearLayout lyHeader;
    private TextView tvDrawerUsername;
    private TextView tvDrawerEmail;
    private TextView tvDrawerRole;
    private TextView tvDrawerPoints;
    private CircleImageView ivUser;

    private MainViewModel mMainViewModel;

    private boolean created = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MainViewModelFactory mainViewModelFactory = MainViewModelFactory.createFactory(this);
        mMainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                tvDrawerEmail.setText(user.getEmail());
                tvDrawerUsername.setText(user.getUsername());
                tvDrawerRole.setText(user.getRole());
                tvDrawerPoints.setText(user.getPoints() + " " + getString(R.string.points));
                Glide.with(getApplicationContext())
                        .load(user.getPhoto())
                        .into(ivUser);
                if (!created) {
                    mMainViewModel.updateUser(user.getEmail());
                    created = true;
                }
            }
        });


        Fragment fragment = null;
        if (mMainViewModel.getSelection(getApplicationContext()) == 1) {
            fragment = new RankingFragment();
            setTitle(R.string.ranking);
        } else if (mMainViewModel.getSelection(getApplicationContext()) == 2) {
            fragment = new ContractFragment();
            setTitle(R.string.contracts);
        } else if (mMainViewModel.getSelection(getApplicationContext()) == 3) {
            fragment = new EmptyFragment();
        } else if (mMainViewModel.getSelection(getApplicationContext()) == 4) {
            fragment = new EmptyFragment();
        }   else if (mMainViewModel.getSelection(getApplicationContext()) == 5) {
            fragment = new EmptyFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.lyMainContent, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();

        this.navigationView.getMenu().getItem(mMainViewModel.getSelection(getApplicationContext())-1).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = this.getSupportFragmentManager();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lyHeader = navigationView.getHeaderView(0).findViewById(R.id.lyHeader);
        lyHeader.setOnClickListener(v -> {
            goToProfileActivity();
        });
        tvDrawerEmail = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerEmail);
        tvDrawerUsername = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerUsername);
        tvDrawerRole = navigationView.getHeaderView(0).findViewById(R.id.tvDrawerRole);
        ivUser = navigationView.getHeaderView(0).findViewById(R.id.ivUser);
        tvDrawerPoints = navigationView.getHeaderView(0).findViewById(R.id.tvPoints);
        this.navigationView.setCheckedItem(R.id.nav_ranking);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.close_question, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_ranking) {
            mMainViewModel.saveSelection(1);
            fragment = new RankingFragment();
            setTitle(R.string.ranking);
        } else if (id == R.id.nav_contracts) {
            mMainViewModel.saveSelection(2);
            fragment = new ContractFragment();
            setTitle(R.string.contracts);
        } else if (id == R.id.nav_diagnostics) {
            mMainViewModel.saveSelection(3);
            fragment = new EmptyFragment();
        } else if (id == R.id.nav_paids) {
            mMainViewModel.saveSelection(4);
            fragment = new EmptyFragment();
        }   else if (id == R.id.nav_help) {
            mMainViewModel.saveSelection(5);
            fragment = new EmptyFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.lyMainContent, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void goToProfileActivity() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        customType(MainActivity.this,"left-to-right");
        finish();
    }
}
