package org.sic4change.nut4health.ui.main.notifications;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.main.MainViewModel;


public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private NotificationAdapter notificationAdapter;
    private RecyclerView rvNotifications;
    private SwipeRefreshLayout swipe_container;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        swipe_container.setOnRefreshListener(this);
        rvNotifications = view.findViewById(R.id.rvNotification);
        notificationAdapter = new NotificationAdapter(getActivity().getApplicationContext(), "");
        rvNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotifications.setAdapter(notificationAdapter);
        notificationAdapter.setItemOnClickAction((id) -> {
            mMainViewModel.markAsReadNotification(id);
        });
        initData();
        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null) {
                notificationAdapter.setUserId(user.getId());
                mMainViewModel.getNotifications(user.getId(), user.getCreationDate());
            }
        });
        mMainViewModel.getNotifications().observe(getActivity(), notifications -> {
            notificationAdapter.submitList(notifications);
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
        notificationAdapter.notifyDataSetChanged();
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
