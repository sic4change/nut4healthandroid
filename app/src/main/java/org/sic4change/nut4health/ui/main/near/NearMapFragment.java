package org.sic4change.nut4health.ui.main.near;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.pavlospt.CircleView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Near;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.ui.near_detail.NearDetailActivity;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;


public class NearMapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 20;
    //private LatLng currentPosition;

    private CardView cvContract;
    private TextView nChildName;
    private TextView nChildLocation;
    private CircleView nPercentage;
    private RelativeTimeTextView nDate;
    private RelativeTimeTextView nConfirmationDate;


    private String id;

    public NearMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_map, container, false);
        cvContract = view.findViewById(R.id.cvContract);
        cvContract.setOnClickListener(v -> goToContractDetailActivity(id));
        nChildName = view.findViewById(R.id.tvNameItem);
        nChildLocation = view.findViewById(R.id.tvLocationItem);
        nPercentage = view.findViewById(R.id.tvPercentageItem);
        nDate = view.findViewById(R.id.tvDateItem);
        nConfirmationDate = view.findViewById(R.id.tvDateConfirmationItem);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initData();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            showMyPosition();
        }
        return view;
    }


    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getNearContracts().observe(getActivity(), nears -> showNearContracts(nears));
        mMainViewModel.getIsFiltered().observe(getActivity(), filtered -> {
            if (filtered) {
                showNearContracts(mMainViewModel.getNearContracts().getValue());
            }
        });
        mMainViewModel.removeAllNearContracts();
    }

    private void showNearContracts(PagedList<Near> nears) {
        if (mMap != null) {
            mMap.clear();
            for (Near near : nears) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(near.getLatitude(), near.getLongitude()));
                if (near.getStatus().equals(Near.Status.NO_DIAGNOSIS.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else if (near.getStatus().equals(Near.Status.DIAGNOSIS.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(near);
            }
        }
        cvContract.setVisibility(View.GONE);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(marker -> {
            Near near = (Near) marker.getTag();
            if (near != null) {
                showNearInformation(near);
                marker.setTitle(near.getPercentage() + "%");
                id = near.getId();
            }
            markMyPosition();
            return false;
        });
        mMap.setOnMapClickListener(latLng -> cvContract.setVisibility(View.GONE));
        mMap.setOnMapLongClickListener(latLng -> cvContract.setVisibility(View.GONE));
    }

    private void showNearInformation(Near near) {
        nChildName.setText(near.getChildName() + " " + near.getChildSurname());
        nChildLocation.setText(near.getChildAddress());
        nPercentage.setTitleText(near.getPercentage() + "%");
        if (near.getStatus().equals(Near.Status.DIAGNOSIS.name())) {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.ms_errorColor));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.GONE);
        } else if (near.getStatus().equals(Near.Status.NO_DIAGNOSIS.name())) {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.GONE);
        } else {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.colorAccent));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.colorAccent));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            try {
                Date date = formatter.parse(near.getMedicalDate());
                nConfirmationDate.setReferenceTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nConfirmationDate.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(near.getCreationDate());
            nDate.setReferenceTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cvContract.setVisibility(View.VISIBLE);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showMyPosition() {
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity(),
                location -> {
                    Log.d("Location", "my location is " + location.latitude + ", " + location.longitude);
                    mMainViewModel.setCurrentPosition(new LatLng(location.latitude, location.longitude));
                    markMyPosition();
                    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMainViewModel.getCurrentPosition(), DEFAULT_ZOOM));
                    }
                    mMainViewModel.retrieveNearContracts(location.latitude, location.longitude);
                });
    }

    private void markMyPosition() {
        try {
            if (mMainViewModel.getCurrentPosition() != null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mMainViewModel.getCurrentPosition());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                markerOptions.title(getString(R.string.your_position));
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
            }
        } catch (Exception e) {
            Log.d("Location", "can not show my position");
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showMyPosition();
        }
    }

    private void goToContractDetailActivity(String id) {
        Intent intent = new Intent(getActivity(), NearDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

}
