package org.sic4change.nut4health.ui.main.contracts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.contract_detail.ContractDetailActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.ui.main.MainViewModelFactory;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractsMapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private MainViewModel mMainViewModel;
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 20;
    private LatLng currentPosition;

    private CardView cvContract;
    private TextView nChildName;
    private TextView nChildLocation;
    private CircleView nPercentage;
    private RelativeTimeTextView nDate;
    private RelativeTimeTextView nConfirmationDate;

    private String id;

    public ContractsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract_map, container, false);
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
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            showMyPosition();
        }
        return view;
    }


    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null) {
                mMainViewModel.getContracts(user.getEmail(), user.getRole());
            }
        });
        mMainViewModel.getContracts().observe(getActivity(), contracts -> showContracts(contracts));
        mMainViewModel.getIsFiltered().observe(getActivity(), filtered -> {
            if (filtered) {
                showContracts(mMainViewModel.getContracts().getValue());
            }
        });
    }

    private void showContracts(PagedList<Contract> contracts) {
        if (mMap != null) {
            mMap.clear();
            for (Contract contract : contracts) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(contract.getLatitude(), contract.getLongitude()));
                if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(contract);
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
            Contract contract = (Contract) marker.getTag();
            if (contract != null) {
                showContractInformation(contract);
                marker.setTitle(contract.getPercentage() + "%");
                id = contract.getId();
            }
            markMyPosition();
            return false;
        });
        mMap.setOnMapClickListener(latLng -> cvContract.setVisibility(View.GONE));
        mMap.setOnMapLongClickListener(latLng -> cvContract.setVisibility(View.GONE));
    }

    private void showContractInformation(Contract contract) {
        nChildName.setText(contract.getChildName() + " " + contract.getChildSurname());
        nChildLocation.setText(contract.getChildAddress());
        nPercentage.setTitleText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.ms_errorColor));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.GONE);
        } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.GONE);
        } else {
            nPercentage.setFillColor(getActivity().getResources().getColor(R.color.colorAccent));
            nPercentage.setStrokeColor(getActivity().getResources().getColor(R.color.colorAccent));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            try {
                Date date = formatter.parse(contract.getMedicalDate());
                nConfirmationDate.setReferenceTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nConfirmationDate.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(contract.getCreationDate());
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
                    Log.d("Location", "my location is " + location.toString());
                    currentPosition = new LatLng(location.latitude, location.longitude);
                    markMyPosition();
                    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, DEFAULT_ZOOM));
                    }
                });
    }

    private void markMyPosition() {
        try {
            if (currentPosition != null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentPosition);
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
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

}
