package org.sic4change.nut4health.ui.create_contract;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.events.MessageEvent;
import org.sic4change.nut4health.ui.fingerprint.ScanActivity;
import org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;
import org.sic4change.nut4health.utils.ruler_picker.RulerValuePicker;
import org.sic4change.nut4health.utils.ruler_picker.RulerValuePickerListener;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;
import static org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity.PERCENTAGE;
import static org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity.PHOTO_PATH;

public class StepCreateContractFragment extends Fragment implements Step{

    private int position;

    private Button btnTakePhoto;
    private RulerValuePicker rulerPicker;
    private ImageView ivTakePhoto;
    private TextView tvPercentage;
    private TextView tvCm;
    private CardView cvChild;
    private EditText etChildName;
    private EditText etChildSurname;
    private EditText etChildLocation;
    private EditText etChildContactPhone;
    private Button btnCheckMalnutrition;
    private AnimatedCircleLoadingView clView;
    private ImageView ivAddFingerprint;
    private org.sic4change.nut4health.utils.view.Nut4HealthTextAwesome ivNewContract;

    public static final int REQUEST_TAKE_PHOTO       = 1;
    public static final int REQUEST_TAKE_FINGERPRINT = 2;

    private static final long VERIFICATION_DELAY_MILISECONDS = 6000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    private CreateContractViewModel mCreateContractViewModel;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;

    private String eventResult;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_create_contract, container, false);
        eventResult = "";
        btnTakePhoto = v.findViewById(R.id.btnTakePhoto);
        rulerPicker = v.findViewById(R.id.ruler_picker);
        btnTakePhoto.setVisibility(View.GONE);
        btnCheckMalnutrition = v.findViewById(R.id.btnCheckMalnutrition);
        ivNewContract = v.findViewById(R.id.ivNewContract);
        btnCheckMalnutrition.setOnClickListener(v12 -> {
            Nut4HealthKeyboard.closeKeyboard(etChildLocation, getContext());
            clView.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            ivNewContract.setVisibility(View.GONE);
            clView.startDeterminate();
            mCreateContractViewModel.getUser().observe(getActivity(), user -> {
                if ((mCreateContractViewModel != null) && (user != null)) {
                    mCreateContractViewModel.createContract(user.getRole(), user.getEmail(),
                            mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude,
                            mCreateContractViewModel.getUriPhoto(), mCreateContractViewModel.getChildName(),
                            mCreateContractViewModel.getChildSurname(), mCreateContractViewModel.getChildLocation(),
                            mCreateContractViewModel.getChildPhoneContact(),
                            mCreateContractViewModel.getPercentage());
                    mCreateContractViewModel = null;
                }
            });
            clView.stopOk();
            new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    showDialogCreateContractResult(eventResult);
                }
            }.start();

        });
        btnTakePhoto.setOnClickListener(v14 -> {
            takePhoto();
            hideTakePhotoButton();
        });
        rulerPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                tvCm.setText(selectedValue + " cm");
                if (selectedValue < 12) {
                    rulerPicker.setBackgroundColor(getResources().getColor(R.color.error));
                    tvPercentage.setText("100%");
                    tvPercentage.setTextColor(getResources().getColor(R.color.error));
                    tvCm.setTextColor(getResources().getColor(R.color.error));
                    mCreateContractViewModel.setPercentage(100);
                } else if (selectedValue >=12 && selectedValue <= 13) {
                    rulerPicker.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvPercentage.setText("50%");
                    tvPercentage.setTextColor(getResources().getColor(R.color.orange));
                    tvCm.setTextColor(getResources().getColor(R.color.orange));
                    mCreateContractViewModel.setPercentage(50);
                } else {
                    rulerPicker.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    tvPercentage.setText("0%");
                    tvPercentage.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvCm.setTextColor(getResources().getColor(R.color.colorAccent));
                    mCreateContractViewModel.setPercentage(0);
                }
                mCreateContractViewModel.setImageSelected(true);
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. Application can utilize this value to display the current selected value.
            }
        });

        ivTakePhoto = v.findViewById(R.id.ivTakePhoto);
        ivTakePhoto.setOnClickListener(v1 -> {
            if (mCreateContractViewModel.isImageSelected()) {
                takePhoto();
            }
        });
        tvPercentage = v.findViewById(R.id.tvPercentage);
        tvCm = v.findViewById(R.id.tvCm);
        cvChild = v.findViewById(R.id.cvChild);
        etChildName = v.findViewById(R.id.etChildName);
        etChildSurname = v.findViewById(R.id.etChildSurname);
        etChildContactPhone = v.findViewById(R.id.etContactPhone);
        etChildLocation = v.findViewById(R.id.etChildLocation);
        ivAddFingerprint = v.findViewById(R.id.ivAddFingerprint);
        ivAddFingerprint.setOnClickListener(v13 -> {
            Intent fingerPrintIntent = new Intent(getActivity(), ScanActivity.class);
            startActivityForResult(fingerPrintIntent, REQUEST_TAKE_FINGERPRINT);
        });
        clView = v.findViewById(R.id.clView);
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                showMyPosition();
        }
        CreateContractViewModelFactory createContractViewModelFactory = CreateContractViewModelFactory.createFactory(getActivity());
        mCreateContractViewModel = ViewModelProviders.of(getActivity(), createContractViewModelFactory).get(CreateContractViewModel.class);
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
            if (mCreateContractViewModel.getLocation() == null) {
                openLocationSettingsDialog();
                return new VerificationError(getString(R.string.error_not_gps_position));
            } else {
                if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                        || (etChildName.getText() == null) || (etChildName.getText().toString() == null) || (etChildName.getText().toString().isEmpty())
                        || (etChildSurname.getText() == null) || (etChildSurname.getText().toString() == null) || (etChildSurname.getText().toString().isEmpty())
                        || (etChildContactPhone.getText() == null) || (etChildContactPhone.getText().toString() == null) || (etChildContactPhone.getText().toString().isEmpty())) {
                    return new VerificationError(getString(R.string.error_child_data));
                }
//                if ((mCreateContractViewModel.getFingerPrint() == null) || (mCreateContractViewModel.getFingerPrint().length < 1)) {
//                    return new VerificationError(getString(R.string.error_fingerprint_data));
//                }
                mCreateContractViewModel.setChildLocation(etChildLocation.getText().toString());
                mCreateContractViewModel.setChildName(etChildName.getText().toString());
                mCreateContractViewModel.setChildSurname(etChildSurname.getText().toString());
                mCreateContractViewModel.setChildPhoneContact(etChildContactPhone.getText().toString());
            }

            return null;
        }
        return null;
    }

    private void openLocationSettingsDialog() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> {

        })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                startIntentSenderForResult(rae.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.e("GPS", "Unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                    }
                })
                .addOnCanceledListener(() -> Log.e("GPS", "checkLocationSettings -> onCanceled"));
    }

    @Override
    public void onSelected() {
        if (getPosition() == 0) {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.VISIBLE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            ivNewContract.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
//            if (mCreateContractViewModel.getChildLocation() != null) {
//                Glide.with(getActivity().getApplicationContext())
//                        .load(new File(mCreateContractViewModel.getUriPhoto().getPath()))
//                        .apply(new RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true))
//                        .into(ivTakePhoto);
//                ivTakePhoto.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            }
//            if (mCreateContractViewModel.isImageSelected()) {
//                btnTakePhoto.setVisibility(View.GONE);
//            } else {
//                btnTakePhoto.setVisibility(View.VISIBLE);
//            }
        } else if (getPosition() == 1) {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            rulerPicker.setVisibility(View.GONE);
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
            if (mCreateContractViewModel.getChildPhoneContact() != null) {
                etChildContactPhone.setText(mCreateContractViewModel.getChildPhoneContact());
            }
            if (mCreateContractViewModel.getChildLocation() != null) {
                etChildLocation.setText(mCreateContractViewModel.getChildLocation());
            }
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            rulerPicker.setVisibility(View.GONE);
            tvCm.setVisibility(View.GONE);
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

    private void takePhoto() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        else {
           Intent takePictureIntent = new Intent(getActivity(), SAMPhotoActivity.class);
           startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void hideTakePhotoButton() {
        btnTakePhoto.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            mCreateContractViewModel.setUriPhoto(Uri.parse(data.getExtras().get(PHOTO_PATH).toString()));
            mCreateContractViewModel.setPercentage(data.getIntExtra(PERCENTAGE, 0));
            File file = new File(getRealPathFromURI(mCreateContractViewModel.getUriPhoto()));
            Glide.with(getActivity().getApplicationContext())
                    .load(file)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(ivTakePhoto);
            ivTakePhoto.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            tvPercentage.setText(mCreateContractViewModel.getPercentage() + " %");
            mCreateContractViewModel.setImageSelected(true);
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
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
                ivAddFingerprint.setImageBitmap(mCreateContractViewModel.getFingerPrintImage());
            } else {
                ivAddFingerprint.setImageResource(R.drawable.ic_finger_no_selected);
                mCreateContractViewModel.setFingerPrint(null);
            }
        }
        if (mCreateContractViewModel.isImageSelected()) {
            btnTakePhoto.setVisibility(View.GONE);
        } else {
            btnTakePhoto.setVisibility(View.VISIBLE);
        }
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                showMyPosition();
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }
            } else if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
                takePhoto();
            }
            else {
                takePhoto();
            }
        }
    }

    private void showMyPosition() {
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity().getApplicationContext(), newLocation -> {
            if (mCreateContractViewModel != null) {
                mCreateContractViewModel.setLocation(newLocation);
                etChildLocation.setText(newLocation.latitude + ", " + newLocation.longitude);
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null) {
                        mCreateContractViewModel.setChildLocation(address);
                        etChildLocation.setText(mCreateContractViewModel.getChildLocation());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void showDialogCreateContractResult(String text) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.diagnosis_finished))
                .setMessage(text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToMainActivity();
                    }
                })
                .setPositiveButton(R.string.ok, (dialog, which) -> getActivity().finish())
                .setIcon(R.mipmap.icon)
                .setCancelable(false)
                .show();
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        eventResult = event.getMessage();
    }


}