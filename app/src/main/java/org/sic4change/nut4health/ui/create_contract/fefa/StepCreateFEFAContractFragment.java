package org.sic4change.nut4health.ui.create_contract.fefa;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;

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
import android.widget.Spinner;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepCreateFEFAContractFragment extends Fragment implements Step, SimpleRulerViewer.OnValueChangeListener{

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
    private EditText etChildTutor;
    private Spinner spTutorStatus;
    private EditText etTutorBirthdate;
    private EditText etTutorDNI;
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

    private CreateContractViewModel mCreateFEFAContractViewModel;

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
        View v = inflater.inflate(R.layout.step_create_contract_fefa, container, false);
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
                    mCreateFEFAContractViewModel.setHeight(Double.parseDouble(charSequence.toString()));
                    mCreateFEFAContractViewModel.getDesnutritionChildTable().observe(getActivity(), values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mCreateFEFAContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mCreateFEFAContractViewModel.getFEFAStatus();
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
                    mCreateFEFAContractViewModel.setWeight(Double.parseDouble(charSequence.toString()));
                    mCreateFEFAContractViewModel.getDesnutritionChildTable().observe(getActivity(), values -> {
                        Collections.sort(values, Comparator.comparingDouble(MalnutritionChildTable::getCm));
                        mCreateFEFAContractViewModel.checkMalnutritionByWeightAndHeight(values);
                        mCreateFEFAContractViewModel.getFEFAStatus();
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
        CreateContractViewModelFactory createFEFAContractViewModelFactory = CreateContractViewModelFactory.createFactory(getActivity());
        mCreateFEFAContractViewModel = ViewModelProviders.of(getActivity(), createFEFAContractViewModelFactory).get(CreateContractViewModel.class);
        mCreateFEFAContractViewModel.getUser().observe(getActivity(), user -> {
            try {
                mCreateFEFAContractViewModel.setRole(user.getRole());
                if (mCreateFEFAContractViewModel.getRole() != null && mCreateFEFAContractViewModel.getRole().equals("Servicio Salud")) {
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
                    mCreateFEFAContractViewModel.getPoints(user.getPoint()).observe(getActivity(), points -> {
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
                                    mCreateFEFAContractViewModel.setPoint(point.getPointId());
                                    mCreateFEFAContractViewModel.setPointFullName(point.getFullName());
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
            mCreateFEFAContractViewModel.getUser().observe(getActivity(), user -> {
                if ((mCreateFEFAContractViewModel != null) && (user != null)) {
                    mCreateFEFAContractViewModel.createContract(user.getId(),
                            user.getRole(),
                            user.getEmail(),
                            mCreateFEFAContractViewModel.getLocation().latitude,
                            mCreateFEFAContractViewModel.getLocation().longitude,
                            mCreateFEFAContractViewModel.getUriPhoto(),
                            mCreateFEFAContractViewModel.getChildName(),
                            mCreateFEFAContractViewModel.getChildSurname(),
                            mCreateFEFAContractViewModel.getSex(),
                            mCreateFEFAContractViewModel.getChildBirthdate(),
                            mCreateFEFAContractViewModel.getChildDNI(),
                            Integer.parseInt(mCreateFEFAContractViewModel.getChildBrothers()),
                            mCreateFEFAContractViewModel.getChildTutor(),
                            mCreateFEFAContractViewModel.getTutorStatus(),
                            mCreateFEFAContractViewModel.getTutorBirthdate(),
                            mCreateFEFAContractViewModel.getTutorDNI(),
                            mCreateFEFAContractViewModel.getChildLocation(),
                            mCreateFEFAContractViewModel.getChildPhoneContact(),
                            mCreateFEFAContractViewModel.getPoint(),
                            mCreateFEFAContractViewModel.getPointFullName(),
                            mCreateFEFAContractViewModel.getPercentage(),
                            mCreateFEFAContractViewModel.getArmCircumference(),
                            mCreateFEFAContractViewModel.getHeight(),
                            mCreateFEFAContractViewModel.getWeight()
                    );
                    mCreateFEFAContractViewModel.setFinishTimeCreateContract(new Date());
                    mCreateFEFAContractViewModel = null;

                }
            });
            clView.stopOk();

        });
        tvPercentage = v.findViewById(R.id.tvPercentage);
        tvCm = v.findViewById(R.id.tvCm);
        cvChild = v.findViewById(R.id.cvChild);
        etTutorBirthdate = v.findViewById(R.id.etTutorBirthdate);

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

        etChildTutor = v.findViewById(R.id.etChildTutor);
        spTutorStatus = v.findViewById(R.id.spStatus);
        etChildContactPhone = v.findViewById(R.id.etContactPhone);
        etTutorDNI = v.findViewById(R.id.etTutorDNI);
        cpp = v.findViewById(R.id.ccp);
        cbVerification = v.findViewById(R.id.cbVerification);

        etChildLocation = v.findViewById(R.id.etChildLocation);
        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                mCreateFEFAContractViewModel.setPhoneCode(selectedCountry.getPhoneCode());
            }
        });
        clView = v.findViewById(R.id.clView);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mCreateFEFAContractViewModel.setLocation(new Nut4HealthSingleShotLocationProvider.GPSCoordinates(location.getLatitude(), location.getLongitude()));
                        etChildLocation.setText(location.getLatitude() + "," + location.getLongitude());
                    }
                }
            });
            showMyPosition();
        }
        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);
        mCreateFEFAContractViewModel.setStartTimeCreateContract(new Date());
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
        if (position == 0 && !mCreateFEFAContractViewModel.isImageSelected()) {
            return new VerificationError(getString(R.string.error_photo));
        } else if (position == 1) {
            if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                    || (etChildTutor.getText() == null) || (etChildTutor.getText().toString() == null) || (etChildTutor.getText().toString().isEmpty())
                    || (etTutorBirthdate.getText().toString().isEmpty())
                    || (etChildContactPhone.getText() == null) || (etChildContactPhone.getText().toString() == null) || (etChildContactPhone.getText().toString().isEmpty())
                    || (!cbVerification.isChecked())) {
                return new VerificationError(getString(R.string.error_child_data));
            }
            mCreateFEFAContractViewModel.setChildLocation(etChildLocation.getText().toString());
            mCreateFEFAContractViewModel.setChildName("");
            mCreateFEFAContractViewModel.setChildSurname("");
            mCreateFEFAContractViewModel.setChildBrothers("99");
            mCreateFEFAContractViewModel.setSex("F");
            mCreateFEFAContractViewModel.setChildBirthdate("1/1/1970");
            mCreateFEFAContractViewModel.setChildDNI("");
            mCreateFEFAContractViewModel.setTutorDNI(etTutorDNI.getText().toString());
            mCreateFEFAContractViewModel.setChildTutor(etChildTutor.getText().toString());
            mCreateFEFAContractViewModel.setTutorStatus(spTutorStatus.getSelectedItem().toString());
            mCreateFEFAContractViewModel.setTutorBirthdate(etTutorBirthdate.getText().toString());
            mCreateFEFAContractViewModel.setChildPhoneContact(etChildContactPhone.getText().toString());
            mCreateFEFAContractViewModel.setChildVerification(cbVerification.isChecked());
            if (mCreateFEFAContractViewModel.getUser().getValue().getRole().equals("Agente Salud") &&
                    mCreateFEFAContractViewModel.getArmCircumference() < CreateContractViewModel.MINIUM_DESNUTRITION_VALUE_MUAC &&
            !mCreateFEFAContractViewModel.isDialerOpened()) {
                mCreateFEFAContractViewModel.setDialerOpened(true);
                new AwesomeInfoDialog(getActivity())
                        .setColoredCircle(R.color.colorPrimaryDark)
                        .setTitle("+" + mCreateFEFAContractViewModel.getPhoneCode() + mCreateFEFAContractViewModel.getChildPhoneContact())
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
                if (mCreateFEFAContractViewModel.getPercentage() == -1) {
                    tvPercentage.setText("");
                } else {
                    if (mCreateFEFAContractViewModel.getPercentage() < 50) {
                        tvPercentage.setText(getResources().getString(R.string.normopeso_full));
                    } else if (mCreateFEFAContractViewModel.getPercentage() == 50) {
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
            if (mCreateFEFAContractViewModel.getRole() != null && mCreateFEFAContractViewModel.getRole().equals("Servicio Salud")) {
                etHeight.setVisibility(View.VISIBLE);
                tvHeight.setVisibility(View.VISIBLE);
                etWeight.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
            }
        } else if (getPosition() == 1) {
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
            if (mCreateFEFAContractViewModel.getChildTutor() != null) {
                etChildTutor.setText(mCreateFEFAContractViewModel.getChildTutor());
            }
            if (mCreateFEFAContractViewModel.getTutorDNI() != null) {
                etTutorDNI.setText(mCreateFEFAContractViewModel.getTutorDNI());
            }
            if (mCreateFEFAContractViewModel.getTutorStatus() != null) {
                spTutorStatus.setSelection(((ArrayAdapter<String>) spTutorStatus.getAdapter()).getPosition(mCreateFEFAContractViewModel.getTutorStatus()));
            }
            if (mCreateFEFAContractViewModel.getChildPhoneContact() != null) {
                etChildContactPhone.setText(mCreateFEFAContractViewModel.getChildPhoneContact());
            }
            cbVerification.setChecked(mCreateFEFAContractViewModel.getVerification());
            if (mCreateFEFAContractViewModel.getChildLocation() != null) {
                etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
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
                mCreateFEFAContractViewModel.setFingerPrint(fingerprint);
            } else {
                mCreateFEFAContractViewModel.setFingerPrint(null);
            }
        } else if (requestCode == ADDRESS_PICKER_REQUEST && resultCode == RESULT_OK){
            double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
            double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

            mCreateFEFAContractViewModel.setLocation((new Nut4HealthSingleShotLocationProvider.GPSCoordinates(currentLatitude,currentLongitude)));
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mCreateFEFAContractViewModel.getLocation().latitude, mCreateFEFAContractViewModel.getLocation().longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                if (address != null) {
                    mCreateFEFAContractViewModel.setChildLocation(address);
                    etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
                } else {
                    mCreateFEFAContractViewModel.setChildLocation("" + currentLatitude + "," + currentLongitude);
                    etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
                }
            } catch (IOException e) {
                mCreateFEFAContractViewModel.setChildLocation("" + currentLatitude + "," + currentLongitude);
                etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
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
            mCreateFEFAContractViewModel.setArmCircumference(value);
            mCreateFEFAContractViewModel.setPercentage(value);
            mCreateFEFAContractViewModel.setImageSelected(true);
        }
        if (mCreateFEFAContractViewModel.isImageSelected()) {
            btnTakePhoto.setVisibility(View.GONE);
        } else {
            btnTakePhoto.setVisibility(View.VISIBLE);
        }
    }

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:+" + mCreateFEFAContractViewModel.getPhoneCode() + mCreateFEFAContractViewModel.getChildPhoneContact()));
        getActivity().startActivity(intent);
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }

    private void showMyPosition() {
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity().getApplicationContext(), newLocation -> {
            if (mCreateFEFAContractViewModel != null) {
                mCreateFEFAContractViewModel.setLocation(newLocation);
                etChildLocation.setText(newLocation.latitude + ", " + newLocation.longitude);
                List<Address> addresses;
                try {
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(mCreateFEFAContractViewModel.getLocation().latitude, mCreateFEFAContractViewModel.getLocation().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null) {
                        mCreateFEFAContractViewModel.setChildLocation(address);
                        etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
                    } else {
                        mCreateFEFAContractViewModel.setChildLocation("" + newLocation.latitude + "," + newLocation.longitude);
                        etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
                    }
                } catch (IOException e) {
                    mCreateFEFAContractViewModel.setChildLocation("" + newLocation.latitude + "," + newLocation.longitude);
                    etChildLocation.setText(mCreateFEFAContractViewModel.getChildLocation());
                }
            }
        });
    }

    private void paintStatusChanges() {
        if (mCreateFEFAContractViewModel.getFEFAStatus().equals("Aguda Severa")) {
            tvPercentage.setText(getResources().getString(R.string.severe_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.error));
        } else if (mCreateFEFAContractViewModel.getFEFAStatus().equals("Aguda Moderada")) {
            tvPercentage.setText(getResources().getString(R.string.moderate_acute_malnutrition_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.orange));
        } else {
            tvPercentage.setText(getResources().getString(R.string.normopeso_full));
            tvPercentage.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private void goToMainActivity() {
        try {
            mCreateFEFAContractViewModel = null;
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
        mCreateFEFAContractViewModel.setArmCircumference(value);
        if (value < 18.0) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.error));
            tvCm.setTextColor(getResources().getColor(R.color.error));
            mCreateFEFAContractViewModel.setPercentage(100);
        } else if (value >= 18.0 && value < 21.0) {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.orange));
            tvCm.setTextColor(getResources().getColor(R.color.orange));
            mCreateFEFAContractViewModel.setPercentage(50);
        } else {
            rulerBackground.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvCm.setTextColor(getResources().getColor(R.color.colorAccent));
            mCreateFEFAContractViewModel.setPercentage(0);
        }
        mCreateFEFAContractViewModel.getFEFAStatus();
        paintStatusChanges();
        mCreateFEFAContractViewModel.setImageSelected(true);
    }
}