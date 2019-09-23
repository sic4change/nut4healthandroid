package org.sic4change.nut4health.ui.main.ranking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.ui.ranking_detail.RankingDetailActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;

import static maes.tech.intentanim.CustomIntent.customType;


public class RankingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private RankingAdapter rankingAdapter;
    private RecyclerView rvRanking;
    private SwipeRefreshLayout swipe_container;
    private FloatingActionButton btnSearchUser;
    private CardView lyFilter;
    private EditText etSurname;

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        swipe_container.setOnRefreshListener(this);
        rvRanking = view.findViewById(R.id.rvRanking);
        rankingAdapter = new RankingAdapter(getActivity().getApplicationContext(), new User());
        rvRanking.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRanking.setAdapter(rankingAdapter);
        rankingAdapter.setItemOnClickAction((username, position) -> goToUserRankingDetailActivity(username, position));
        btnSearchUser = view.findViewById(R.id.btnSearchUser);
        btnSearchUser.setOnClickListener(v -> {
            if (lyFilter.getVisibility() == View.VISIBLE) {
                lyFilter.setVisibility(View.GONE);
                Nut4HealthKeyboard.closeKeyboard(etSurname, getContext());
            } else {
                lyFilter.setVisibility(View.VISIBLE);
            }
        });
        lyFilter = view.findViewById(R.id.lyFilter);
        etSurname = view.findViewById(R.id.etSurname);
        etSurname.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etSurname.getRight() - etSurname.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    etSurname.setText("");
                    return true;
                }
            }
            return false;
        });
        etSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mMainViewModel.setUsernameRanking(s.toString());
                mMainViewModel.getSortedRanking("POINTS", mMainViewModel.getUsernameRanking());
                mMainViewModel.getRanking().observe(getActivity(), rankings -> {
                    rankingAdapter.submitList(rankings);
                });
            }
        });
        initData();
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                rankingAdapter.setUser(user);
                mMainViewModel.getRankingUser();
            }
        });
        mMainViewModel.getRanking().observe(getActivity(), rankings -> {
            rankingAdapter.submitList(rankings);
            if (swipe_container.isRefreshing()) {
                swipe_container.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        mMainViewModel = null;
        initData();
        rankingAdapter.notifyDataSetChanged();
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                swipe_container.setRefreshing(false);
            }

        }.start();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void goToUserRankingDetailActivity(String username, int position) {
        Intent intent = new Intent(getActivity(), RankingDetailActivity.class);
        intent.putExtra("RANKING_USERNAME", username);
        intent.putExtra("RANKING_POSITION", position);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }


}
