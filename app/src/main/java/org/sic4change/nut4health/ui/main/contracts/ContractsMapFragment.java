package org.sic4change.nut4health.ui.main.contracts;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
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
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

import java.text.ParseException;
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
    private TextView nStatus;
    private TextView nChildName;
    private TextView nChildLocation;
    private CircleView nPercentage;
    private TextView nDate;
    private TextView nConfirmationDate;

    private TextView tvTotalCasesMap;

    private String id;

    private String role= "";

    public ContractsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract_map, container, false);
        cvContract = view.findViewById(R.id.cvContract);
        cvContract.setOnClickListener(v -> goToContractDetailActivity(id, role));
        nStatus = view.findViewById(R.id.tvStatus);
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
        tvTotalCasesMap = view.findViewById(R.id.tvTotalCasesMap);
        return view;
    }


    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        mMainViewModel.getContracts().observe(getActivity(), contracts -> {
            showContracts(contracts);
            showContractsNumber(contracts);
        });

        try {
            mMainViewModel.getIsFiltered().observe(getActivity(), filtered ->{
                mMainViewModel.getContracts().observe(getActivity(), contracts -> {
                    showContracts(contracts);
                    showContractsNumber(contracts);
                });
            });
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private void showContractsNumber(PagedList<Contract> contracts) {
        try {
            tvTotalCasesMap.setText(getString(R.string.showing) + " " + contracts.size() + " " + getString(R.string.diagnosis_show));
        } catch (Exception e) {
            System.out.println("null contracts");
        }
    }

    private void showContracts(PagedList<Contract> contracts) {
        if (mMap != null) {
            mMap.clear();
            for (Contract contract : contracts) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(contract.getLatitude(), contract.getLongitude()));
                if (contract.getPercentage() < 50) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else if (contract.getPercentage() == 50) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                if (contract.getStatus().equals(Contract.Status.PAID.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
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
                if (contract.getPercentage() < 50) {
                    marker.setTitle(getResources().getString(R.string.normopeso_abrev));
                } else if (contract.getPercentage() == 50) {
                    marker.setTitle(getResources().getString(R.string.moderate_acute_malnutrition_abrev));
                } else {
                    marker.setTitle(getResources().getString(R.string.severe_acute_malnutrition_abrev));
                }
                id = contract.getId();
            }
            markMyPosition();
            return false;
        });
        mMap.setOnMapClickListener(latLng -> cvContract.setVisibility(View.GONE));
        mMap.setOnMapLongClickListener(latLng -> cvContract.setVisibility(View.GONE));
        showContracts(mMainViewModel.getContracts().getValue());
    }

    private void showContractInformation(Contract contract) {
        nChildName.setText(contract.getChildName() + " " + contract.getChildSurname());
        nChildLocation.setText(contract.getChildAddress());
        if (contract.getPercentage() < 50) {
            nPercentage.setTitleText(getResources().getString(R.string.normopeso_abrev));
            nPercentage.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
            nStatus.setText(getResources().getString(R.string.normopeso));
            nStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else if (contract.getPercentage() == 50) {
            nPercentage.setTitleText(getResources().getString(R.string.moderate_acute_malnutrition_abrev));
            nPercentage.setFillColor(getResources().getColor(R.color.orange));
            nPercentage.setStrokeColor(getResources().getColor(R.color.orange));
            nStatus.setText(getResources().getString(R.string.moderate_acute_malnutrition));
            nStatus.setTextColor(getResources().getColor(R.color.orange));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else {
            nPercentage.setTitleText(getResources().getString(R.string.severe_acute_malnutrition_abrev));
            nPercentage.setFillColor(getResources().getColor(R.color.ms_errorColor));
            nPercentage.setStrokeColor(getResources().getColor(R.color.ms_errorColor));
            nStatus.setText(getResources().getString(R.string.severe_acute_malnutrition));
            nStatus.setTextColor(getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        }
        if (contract.getStatus().equals(Contract.Status.PAID.name())) {
            nPercentage.setFillColor(getResources().getColor(R.color.colorAccent));
            nPercentage.setStrokeColor(getResources().getColor(R.color.colorAccent));
            Date date = new Date(contract.getMedicalDate());
            Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            String text = TimeAgo.using(date.getTime(), messages);
            nConfirmationDate.setText(text);
            nStatus.setText(getResources().getString(R.string.paid));
            nStatus.setTextColor(getResources().getColor(R.color.colorAccent));
            nConfirmationDate.setVisibility(View.VISIBLE);
        } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
            nPercentage.setFillColor(getResources().getColor(R.color.violet));
            nPercentage.setStrokeColor(getResources().getColor(R.color.violet));
            try {
                Date date = new Date(contract.getMedicalDate());
                Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
                TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                String text = TimeAgo.using(date.getTime(), messages);
                nConfirmationDate.setText(text);
            } catch (Exception e) {
                nConfirmationDate.setText("");
            }
            nStatus.setText(getResources().getString(R.string.finished));
            nStatus.setTextColor(getResources().getColor(R.color.violet));
            nConfirmationDate.setVisibility(View.VISIBLE);
        }

        Date date = new Date(contract.getCreationDate());
        Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        String text = TimeAgo.using(date.getTime(), messages);
        nDate.setText(text);
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
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
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

    private void goToContractDetailActivity(String id, String role) {
        Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
        intent.putExtra("CONTRACT_ID", id);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

}
