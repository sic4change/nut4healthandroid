package org.sic4change.nut4health.ui.create_contract;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;

public class StepCreateContractFragment extends Fragment implements Step {

    private int position;
    private boolean imageSelected = false;

    private Button btnTakePhoto;
    private ImageView ivTakePhoto;
    private CardView cvChild;
    private EditText etChildName;
    private EditText etChildSurname;
    private EditText etChildLocation;
    private Button btnCheckMalnutrition;
    private AnimatedCircleLoadingView clView;

    public static final int IMAGE_COMPRESS_QUALITY = 100;
    public static final int IMAGE_ASPECT_RATIO_X_Y = 3;

    private static final long VERIFICATION_DELAY_MILISECONDS = 5000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;
    private static final long EXIT_DELAY_MILISECONDS = 4000;
    private static final int LOCATION_REQUEST_CODE = 101;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_create_contract, container, false);
        btnTakePhoto = v.findViewById(R.id.btnTakePhoto);
        btnCheckMalnutrition = v.findViewById(R.id.btnCheckMalnutrition);
        btnCheckMalnutrition.setOnClickListener(v12 -> {
            clView.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setClickable(false);
            btnCheckMalnutrition.setEnabled(false);
            clView.startDeterminate();
            //TODO: change por SAMPHOTO call
            new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    final int min = 1;
                    final int max = 100;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    if (random > 50) {
                        clView.stopFailure();
                    } else {
                        clView.stopOk();
                    }
                    new CountDownTimer(EXIT_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            goToMainActivity();
                        }
                    }.start();
                }
            }.start();
        });
        btnTakePhoto.setOnClickListener(v1 -> takePhoto());
        ivTakePhoto = v.findViewById(R.id.ivTakePhoto);
        cvChild = v.findViewById(R.id.cvChild);
        etChildName = v.findViewById(R.id.etChildName);
        etChildSurname = v.findViewById(R.id.etChildSurname);
        etChildLocation = v.findViewById(R.id.etChildLocation);
        clView = v.findViewById(R.id.clView);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            showMyPosition();
        }
        return v;
    }

    @Override
    public VerificationError verifyStep() {
        if (position == 0 && !imageSelected) {
            return new VerificationError(getString(R.string.error_photo));
        } else if (position == 1) {
            if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                || (etChildName.getText() == null) || (etChildName.getText().toString() == null) || (etChildName.getText().toString().isEmpty())
                    || (etChildSurname.getText() == null) || (etChildSurname.getText().toString() == null) || (etChildSurname.getText().toString().isEmpty())) {
                return new VerificationError(getString(R.string.error_child_data));
            }
            return null;
        }
        return null;
    }

    @Override
    public void onSelected() {
        if (getPosition() == 0) {
            btnTakePhoto.setVisibility(View.VISIBLE);
            ivTakePhoto.setVisibility(View.VISIBLE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
        } else if (getPosition() == 1) {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            cvChild.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.VISIBLE);
            clView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void takePhoto() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setOutputCompressQuality(IMAGE_COMPRESS_QUALITY)
                .setAspectRatio(IMAGE_ASPECT_RATIO_X_Y, IMAGE_ASPECT_RATIO_X_Y)
                .start(getActivity().getApplicationContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(getActivity().getApplicationContext())
                        .load(resultUri)
                        .into(ivTakePhoto);
                ivTakePhoto.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                imageSelected = true;

            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                showMyPosition();
            } else {
                takePhoto();
            }
        }
    }

    private void showMyPosition() {
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity().getApplicationContext(),
                location -> {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        if (address != null) {
                            etChildLocation.setText(address);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        customType(getActivity(),"right-to-left");
        getActivity().finish();
    }

}
