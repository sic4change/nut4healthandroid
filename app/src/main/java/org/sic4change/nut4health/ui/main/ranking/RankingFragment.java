package org.sic4change.nut4health.ui.main.ranking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Ranking;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.ui.main.MainViewModelFactory;
import org.sic4change.nut4health.ui.main.contracts.ContractsAdapter;


public class RankingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private RankingAdapter rankingAdapter;
    private RecyclerView rvRanking;
    private android.support.v4.widget.SwipeRefreshLayout swipe_container;


    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        swipe_container.setOnRefreshListener(this);
        rvRanking = view.findViewById(R.id.rvRanking);
        rankingAdapter = new RankingAdapter(getActivity().getApplicationContext(), new User());
        rvRanking.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRanking.setAdapter(rankingAdapter);
        initData();
        return view;
    }

    private void initData() {
        MainViewModelFactory mainViewModelFactory = MainViewModelFactory.createFactory(getActivity());
        mMainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                rankingAdapter.setUser(user);
                mMainViewModel.getRankingUser();
                mMainViewModel.getRanking().observe(getActivity(), rankings -> {
                    rankingAdapter.submitList(rankings);
                    if (swipe_container.isRefreshing()) {
                        swipe_container.setRefreshing(false);
                    }
                });
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


}
