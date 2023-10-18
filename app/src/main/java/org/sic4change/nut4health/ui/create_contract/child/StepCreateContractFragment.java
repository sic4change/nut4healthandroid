package org.sic4change.nut4health.ui.create_contract.child;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.shivtechs.maplocationpicker.MapUtility;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.events.MessageEvent;
import org.sic4change.nut4health.ui.create_contract.CreateContractViewModel;
import org.sic4change.nut4health.ui.create_contract.CreateContractViewModelFactory;
import org.sic4change.nut4health.ui.fingerprint.ScanActivity;
import org.sic4change.nut4health.ui.serchablespinner.SearchableSpinner;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;
import org.sic4change.nut4health.utils.ruler_picker.SimpleRulerViewer;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class StepCreateContractFragment extends Fragment implements Step, SimpleRulerViewer.OnValueChangeListener{

    private int position;

    private int LAUNCH_SAMPhoto = 10;

    private Button btnTakePhoto;
    private View rulerBackground;
    private SimpleRulerViewer ruler;
    private TextView tvHeight;
    private EditText etHeight;
    private TextView tvWeight;
    private EditText etWeight;
    private TextView tvPercentage;
    private TextView tvCm;
    private CardView cvChild;
    private EditText etChildName;
    private EditText etChildSurname;
    private EditText etChildBrothers;
    private EditText etChildBirthdate;
    private StickySwitch ssSex;
    private TextView tvChildDNI;
    private EditText etChildDNI;
    private EditText etChildTutor;
    private EditText etTutorBirthdate;
    private EditText etChildLocation;
    private EditText etChildContactPhone;
    private CountryCodePicker cpp;
    private CheckBox cbVerification;
    private SearchableSpinner spPoint;
    private Button btnCheckMalnutrition;
    private AnimatedCircleLoadingView clView;
    private ImageView ivNewContract;

    public static final int REQUEST_TAKE_FINGERPRINT = 2;

    private static final long VERIFICATION_DELAY_MILISECONDS = 3000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;
    private static final int LOCATION_REQUEST_CODE = 101;

    private CreateContractViewModel mCreateContractViewModel;

    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private static final int ADDRESS_PICKER_REQUEST = 721;

    private Calendar calendar = Calendar.getInstance();

    private String eventResult;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_create_contract, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        eventResult = "";
        btnTakePhoto = v.findViewById(R.id.btnTakePhoto);
        rulerBackground = v.findViewById(R.id.rulerBackground);
        ruler = v.findViewById(R.id.ruler);
        ruler.setOnValueChangeListener(this);
        ruler.setSelectedValue(28.0f);
        tvHeight = v.findViewById(R.id.tvHeight);
        etHeight = v.findViewById(R.id.etHeight);
        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mCreateContractViewModel.setHeight(Double.parseDouble(charSequence.toString()));
                    mCreateContractViewModel.getDesnutritionChildTable().observe(getActivity(), values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mCreateContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mCreateContractViewModel.getStatus();
                        paintStatusChanges();
                    });
                } catch (Exception e) {
                    System.out.println("empty o null value");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvWeight = v.findViewById(R.id.tvWeight);
        etWeight = v.findViewById(R.id.etWeight);
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mCreateContractViewModel.setWeight(Double.parseDouble(charSequence.toString()));
                    mCreateContractViewModel.getDesnutritionChildTable().observe(getActivity(), values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mCreateContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mCreateContractViewModel.getStatus();
                        paintStatusChanges();
                    });
                } catch (Exception e) {
                    System.out.println("empty o null value");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnCheckMalnutrition = v.findViewById(R.id.btnCheckMalnutrition);
        ivNewContract = v.findViewById(R.id.ivNewContract);
        spPoint = v.findViewById(R.id.spPoint);
        CreateContractViewModelFactory createContractViewModelFactory = CreateContractViewModelFactory.createFactory(getActivity());
        mCreateContractViewModel = ViewModelProviders.of(getActivity(), createContractViewModelFactory).get(CreateContractViewModel.class);
        mCreateContractViewModel.getUser().observe(getActivity(), user -> {
            try {
                mCreateContractViewModel.setRole(user.getRole());
                if (mCreateContractViewModel.getRole() != null && mCreateContractViewModel.getRole().equals("Servicio Salud")) {
                    etHeight.setVisibility(View.VISIBLE);
                    tvHeight.setVisibility(View.VISIBLE);
                    etWeight.setVisibility(View.VISIBLE);
                    tvWeight.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                System.out.println("error crate contract related with user role");
            }
            if (user.getPoint() != null) {
                try {
                    mCreateContractViewModel.getPoints(user.getPoint()).observe(getActivity(), points -> {
                        if (points != null) {
                            spPoint.setTitle(getString(R.string.select_medical));
                            spPoint.setPositiveButton(getString(R.string.ok));

                            ArrayAdapter<Point> adp1 = new ArrayAdapter<Point>(getActivity(),
                                    android.R.layout.simple_list_item_1, points);
                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spPoint.setAdapter(adp1);
                            spPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Point point = (Point) parent.getSelectedItem();
                                    mCreateContractViewModel.setPoint(point.getPointId());
                                    mCreateContractViewModel.setPointFullName(point.getFullName());
                                    cpp.setCountryForPhoneCode(Integer.parseInt(point.getPhoneCode()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    System.out.println("error refresh points");
                }

            }
        });

        btnCheckMalnutrition.setOnClickListener(v12 -> {
            Nut4HealthKeyboard.closeKeyboard(etChildLocation, getContext());
            clView.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            ivNewContract.setVisibility(View.GONE);
            clView.startDeterminate();
            mCreateContractViewModel.getUser().observe(getActivity(), user -> {
                if ((mCreateContractViewModel != null) && (user != null)) {
                    mCreateContractViewModel.createContract(user.getId(),
                            user.getRole(),
                            user.getEmail(),
                            mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude,
                            mCreateContractViewModel.getUriPhoto(), mCreateContractViewModel.getChildName(),
                            mCreateContractViewModel.getChildSurname(),
                            mCreateContractViewModel.getSex(),
                            mCreateContractViewModel.getChildBirthdate(),
                            mCreateContractViewModel.getChildDNI(),
                            Integer.parseInt(mCreateContractViewModel.getChildBrothers()),
                            mCreateContractViewModel.getChildTutor(),
                            mCreateContractViewModel.getTutorStatus(),
                            mCreateContractViewModel.getTutorBirthdate(),
                            mCreateContractViewModel.getChildLocation(),
                            mCreateContractViewModel.getChildPhoneContact(),
                            mCreateContractViewModel.getPoint(),
                            mCreateContractViewModel.getPointFullName(),
                            mCreateContractViewModel.getPercentage(),
                            mCreateContractViewModel.getArmCircumference(),
                            mCreateContractViewModel.getHeight(),
                            mCreateContractViewModel.getWeight()
                    );
                    mCreateContractViewModel.setFinishTimeCreateContract(new Date());
                    mCreateContractViewModel = null;

                }
            });
            clView.stopOk();

        });
        tvPercentage = v.findViewById(R.id.tvPercentage);
        tvCm = v.findViewById(R.id.tvCm);
        cvChild = v.findViewById(R.id.cvChild);
        etChildName = v.findViewById(R.id.etChildName);
        etChildSurname = v.findViewById(R.id.etChildSurname);
        etChildBrothers = v.findViewById(R.id.etBrothers);
        etChildBirthdate = v.findViewById(R.id.etChildBirthdate);
        etTutorBirthdate = v.findViewById(R.id.etTutorBirthdate);

        etChildBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                                String selectedDate = day + "/" + (month + 1) + "/" + year;
                                etChildBirthdate.setText(selectedDate);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        etTutorBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                                String selectedDate = day + "/" + (month + 1) + "/" + year;
                                etTutorBirthdate.setText(selectedDate);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        etChildBrothers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals("")) {
                    try {
                        int massValue = Integer.parseInt(charSequence.toString());
                        if (massValue > 21) {
                            etChildBrothers.setText("21");
                        }
                    } catch (Exception e) {
                        System.out.println("invalid brothers");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ssSex = v.findViewById(R.id.sexSelector);
        etChildDNI = v.findViewById(R.id.etChildDNI);
        tvChildDNI = v.findViewById(R.id.tvChildDNI);
        etChildTutor = v.findViewById(R.id.etChildTutor);
        etChildContactPhone = v.findViewById(R.id.etContactPhone);
        cpp = v.findViewById(R.id.ccp);
        cbVerification = v.findViewById(R.id.cbVerification);

        ssSex.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                mCreateContractViewModel.setSex(direction.name());
            }
        });

        etChildLocation = v.findViewById(R.id.etChildLocation);
        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                mCreateContractViewModel.setPhoneCode(selectedCountry.getPhoneCode());
            }
        });
        clView = v.findViewById(R.id.clView);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mCreateContractViewModel.setLocation(new Nut4HealthSingleShotLocationProvider.GPSCoordinates(location.getLatitude(), location.getLongitude()));
                        etChildLocation.setText(location.getLatitude() + "," + location.getLongitude());
                    }
                }
            });
            showMyPosition();
        }
        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);
        mCreateContractViewModel.setStartTimeCreateContract(new Date());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public VerificationError verifyStep() {
        if (position == 0 && !mCreateContractViewModel.isImageSelected()) {
            return new VerificationError(getString(R.string.error_photo));
        } else if (position == 1) {
            if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                    || (etChildName.getText() == null) || (etChildName.getText().toString() == null) || (etChildName.getText().toString().isEmpty())
                    || (etChildSurname.getText() == null) || (etChildSurname.getText().toString() == null) || (etChildSurname.getText().toString().isEmpty())
                    || (etChildBrothers.getText() == null) || (etChildBrothers.getText().toString() == null) || (etChildBrothers.getText().toString().isEmpty())
                    || (etChildTutor.getText() == null) || (etChildTutor.getText().toString() == null) || (etChildTutor.getText().toString().isEmpty())
                    || (etChildBirthdate.getText().toString().isEmpty()) || (etTutorBirthdate.getText().toString().isEmpty())
                    || (etChildContactPhone.getText() == null) || (etChildContactPhone.getText().toString() == null) || (etChildContactPhone.getText().toString().isEmpty())
                    || (!cbVerification.isChecked())) {
                return new VerificationError(getString(R.string.error_child_data));
            }
            mCreateContractViewModel.setChildLocation(etChildLocation.getText().toString());
            mCreateContractViewModel.setChildName(etChildName.getText().toString());
            mCreateContractViewModel.setChildSurname(etChildSurname.getText().toString());
            mCreateContractViewModel.setChildBrothers(etChildBrothers.getText().toString());
            mCreateContractViewModel.setSex(ssSex.getDirection().name());
            mCreateContractViewModel.setChildBirthdate(etChildBirthdate.getText().toString());
            mCreateContractViewModel.setChildDNI(etChildDNI.getText().toString());
            mCreateContractViewModel.setChildTutor(etChildTutor.getText().toString());
            mCreateContractViewModel.setTutorStatus("");
            mCreateContractViewModel.setTutorBirthdate(etTutorBirthdate.getText().toString());
            mCreateContractViewModel.setChildPhoneContact(etChildContactPhone.getText().toString());
            mCreateContractViewModel.setChildVerification(cbVerification.isChecked());
            if (mCreateContractViewModel.getUser().getValue().getRole().equals("Agente Salud") &&
                    mCreateContractViewModel.getArmCircumference() < CreateContractViewModel.MINIUM_DESNUTRITION_VALUE_MUAC &&
            !mCreateContractViewModel.isDialerOpened()) {
                mCreateContractViewModel.setDialerOpened(true);
                new AwesomeInfoDialog(getActivity())
                        .setColoredCircle(R.color.colorPrimaryDark)
                        .setTitle("+" + mCreateContractViewModel.getPhoneCode() + mCreateContractViewModel.getChildPhoneContact())
                        .setMessage(getString(R.string.check_phone_number))
                        .setPositiveButtonbackgroundColor(R.color.colorPrimaryDark)
                        .setPositiveButtonText(getResources().getString(R.string.ok))
                        .setPositiveButtonClick(() -> {

                        })
                        .setNegativeButtonbackgroundColor(R.color.colorAccent)
                        .setNegativeButtonTextColor(R.color.white)
                        .setNegativeButtonText(getResources().getString(R.string.call))
                        .setNegativeButtonClick(() -> {
                            openDialer();
                        })
                        .show();
                return new VerificationError("");

            }
            return null;
        }
        return null;
    }

    @Override
    public void onSelected() {
        if (getPosition() == 0) {
            tvPercentage.setVisibility(View.VISIBLE);
            try {
                if (mCreateContractViewModel.getPercentage() == -1) {
                    tvPercentage.setText("");
                } else {
                    if (mCreateContractViewModel.getPercentage() < 50) {
                        tvPercentage.setText(getResources().getString(R.string.normopeso_full));
                    } else if (mCreateContractViewModel.getPercentage() == 50) {
                        tvPercentage.setText(getResources().getString(R.string.moderate_acute_malnutrition_full));
                    } else {
                        tvPercentage.setText(getResources().getString(R.string.severe_acute_malnutrition_full));
                    }
                }

            } catch (Exception e) {
                tvPercentage.setText("");
            }
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            ivNewContract.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
            btnTakePhoto.setVisibility(View.GONE);
            ruler.setVisibility(View.VISIBLE);
            rulerBackground.setVisibility(View.VISIBLE);
            tvCm.setVisibility(View.VISIBLE);
            if (mCreateContractViewModel.getRole() != null && mCreateContractViewModel.getRole().equals("Servicio Salud")) {
                etHeight.setVisibility(View.VISIBLE);
                tvHeight.setVisibility(View.VISIBLE);
                etWeight.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
            }
        } else if (getPosition() == 1) {
            if (mCreateContractViewModel.getPercentage() > 49) {
                etChildDNI.setVisibility(View.VISIBLE);
                tvChildDNI.setVisibility(View.VISIBLE);
            } else {
                etChildDNI.setVisibility(View.GONE);
                tvChildDNI.setVisibility(View.GONE);
            }
            btnTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            ruler.setVisibility(View.GONE);
            rulerBackground.setVisibility(View.GONE);
            etHeight.setVisibility(View.GONE);
            tvHeight.setVisibility(View.GONE);
            etWeight.setVisibility(View.GONE);
            tvWeight.setVisibility(View.GONE);
            tvCm.setVisibility(View.GONE);
            cvChild.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            ivNewContract.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
            if (mCreateContractViewModel.getChildName() != null) {
                etChildName.setText(mCreateContractViewModel.getChildName());
            }
            if (mCreateContractViewModel.getChildSurname() != null) {
                etChildSurname.setText(mCreateContractViewModel.getChildSurname());
            }
            if (mCreateContractViewModel.getChildBrothers() != null) {
                etChildBrothers.setText(mCreateContractViewModel.getChildBrothers());
            }
            if (mCreateContractViewModel.getSex() != null) {
                if (mCreateContractViewModel.getSex().equals("F")) {
                    ssSex.setDirection(StickySwitch.Direction.RIGHT);
                } else {
                    ssSex.setDirection(StickySwitch.Direction.LEFT);
                }
            }
            if (mCreateContractViewModel.getChildDNI() != null) {
                etChildDNI.setText(mCreateContractViewModel.getChildDNI());
            }
            if (mCreateContractViewModel.getChildTutor() != null) {
                etChildTutor.setText(mCreateContractViewModel.getChildTutor());
            }
            if (mCreateContractViewModel.getChildPhoneContact() != null) {
                etChildContactPhone.setText(mCreateContractViewModel.getChildPhoneContact());
            }
            cbVerification.setChecked(mCreateContractViewModel.getVerification());
            if (mCreateContractViewModel.getChildLocation() != null) {
                etChildLocation.setText(mCreateContractViewModel.getChildLocation());
            }
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            ruler.setVisibility(View.GONE);
            rulerBackground.setVisibility(View.GONE);
            tvCm.setVisibility(View.GONE);
            etHeight.setVisibility(View.GONE);
            tvHeight.setVisibility(View.GONE);
            etWeight.setVisibility(View.GONE);
            tvWeight.setVisibility(View.GONE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.VISIBLE);
            ivNewContract.setVisibility(View.VISIBLE);
            clView.setVisibility(View.GONE);
            Nut4HealthKeyboard.closeKeyboard(etChildLocation, getContext());
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            showMyPosition();
                        }
                    }.start();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS","User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGpsEnabled) {
                openGpsEnableSetting();
            }
        } else if (requestCode == REQUEST_TAKE_FINGERPRINT && resultCode == RESULT_OK){
            byte[] fingerprint = data.getByteArrayExtra(ScanActivity.FINGERPRINT);
            if ((fingerprint != null) && (fingerprint.length > 0)) {
                mCreateContractViewModel.setFingerPrint(fingerprint);
            } else {
                mCreateContractViewModel.setFingerPrint(null);
            }
        } else if (requestCode == ADDRESS_PICKER_REQUEST && resultCode == RESULT_OK){
            double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
            double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

            mCreateContractViewModel.setLocation((new Nut4HealthSingleShotLocationProvider.GPSCoordinates(currentLatitude,currentLongitude)));
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                if (address != null) {
                    mCreateContractViewModel.setChildLocation(address);
                    etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                } else {
                    mCreateContractViewModel.setChildLocation("" + currentLatitude + "," + currentLongitude);
                    etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                }
            } catch (IOException e) {
                mCreateContractViewModel.setChildLocation("" + currentLatitude + "," + currentLongitude);
                etChildLocation.setText(mCreateContractViewModel.getChildLocation());
            }
        } else if (requestCode ==  LAUNCH_SAMPhoto && resultCode == RESULT_OK){
            String requiredValue = data.getStringExtra("RESULT");
            System.out.println(requiredValue);
            Integer value = requiredValue.equals("NOR") ? 0 : 100;

            if (value < 50) {
                tvPercentage.setText(getResources().getString(R.string.normopeso_full));
                tvPercentage.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (value == 50) {
                tvPercentage.setText(getResources().getString(R.string.moderate_acute_malnutrition_full));
                tvPercentage.setTextColor(getResources().getColor(R.color.orange));
            } else {
                tvPercentage.setText(getResources().getString(R.string.severe_acute_malnutrition_full));
                tvPercentage.setTextColor(getResources().getColor(R.color.error));
            }
            mCreateContractViewModel.setArmCircumference(value);
            mCreateContractViewModel.setPercentage(value);
            mCreateContractViewModel.setImageSelected(true);
        }
        if (mCreateContractViewModel.isImageSelected()) {
            btnTakePhoto.setVisibility(View.GONE);
        } else {
            btnTakePhoto.setVisibility(View.VISIBLE);
        }
    }

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:+" + mCreateContractViewModel.getPhoneCode() + mCreateContractViewModel.getChildPhoneContact()));
        getActivity().startActivity(intent);
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }

    private void showMyPosition() {
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity().getApplicationContext(), newLocation -> {
            if (mCreateContractViewModel != null) {
                mCreateContractViewModel.setLocation(newLocation);
                etChildLocation.setText(newLocation.latitude + ", " + newLocation.longitude);
                List<Address> addresses;
                try {
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null) {
                        mCreateContractViewModel.setChildLocation(address);
                        etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                    } else {
                        mCreateContractViewModel.setChildLocation("" + newLocation.latitude + "," + newLocation.longitude);
                        etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                    }
                } catch (IOException e) {
                    mCreateContractViewModel.setChildLocation("" + newLocation.latitude + "," + newLocation.longitude);
                    etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                }
            }
        });
    }

    private void paintStatusChanges() {
        if (mCreateContractViewModel.getStatus().equals("Aguda Severa")) {
            tvPercentage.setText(getResources().getString(R.string.severe_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.error));
        } else if (mCreateContractViewModel.getStatus().equals("Aguda Moderada")) {
            tvPercentage.setText(getResources().getString(R.string.moderate_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.orange));
        } else {
            tvPercentage.setText(getResources().getString(R.string.normopeso_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private void goToMainActivity() {
        try {
            mCreateContractViewModel = null;
            customType(getActivity(),"right-to-left");
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        eventResult = event.getMessage();
        new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (eventResult.equals(getString(R.string.diagnosis_duplicated_30))) {
                    new AwesomeWarningDialog(getActivity())
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(eventResult)
                            .setButtonText(getResources().getString(R.string.ok))
                            .setWarningButtonClick(() -> {
                                goToMainActivity();
                            })
                            .show();
                } else if (eventResult.equals(getString(R.string.diagnosis_duplicated_7))) {
                    new AwesomeWarningDialog(getActivity())
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(eventResult)
                            .setButtonText(getResources().getString(R.string.ok))
                            .setWarningButtonClick(() -> {
                                goToMainActivity();
                            })
                            .show();
                } else {
                    new AwesomeSuccessDialog(getActivity())
                            .setTitle(getResources().getString(R.string.app_name))
                            .setMessage(eventResult)
                            .setPositiveButtonText(getResources().getString(R.string.ok))
                            .setPositiveButtonClick(() -> {
                                goToMainActivity();
                            })
                            .show();
                }

            }
        }.start();

    }


    @Override
    public void onChange(SimpleRulerViewer view, int position, float value) {
        DecimalFormat df = new DecimalFormat("#.0");
        tvCm.setText(df.format(value) + " cm");
        mCreateContractViewModel.setArmCircumference(value);
        if (value < 11.5) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.error));
            tvCm.setTextColor(getResources().getColor(R.color.error));
            mCreateContractViewModel.setPercentage(100);
        } else if (value >= 11.5 && value < 12.5) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.orange));
            tvCm.setTextColor(getResources().getColor(R.color.orange));
            mCreateContractViewModel.setPercentage(50);
        } else {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvCm.setTextColor(getResources().getColor(R.color.colorAccent));
            mCreateContractViewModel.setPercentage(0);
        }
        mCreateContractViewModel.getStatus();
        paintStatusChanges();
        mCreateContractViewModel.setImageSelected(true);
    }
}