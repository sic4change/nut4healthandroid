package org.sic4change.nut4health.ui.create_contract;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;

public class StepCreateContractFragment extends Fragment implements Step {

    private int position;

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
    private static final long EXIT_DELAY_MILISECONDS = 5000;
    private static final int LOCATION_REQUEST_CODE = 101;

    private CreateContractViewModel mCreateContractViewModel;



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
            Nut4HealthKeyboard.closeKeyboard(etChildLocation, getContext());
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
                            mCreateContractViewModel.getUser().observe(getActivity(), user -> {
                                if ((mCreateContractViewModel != null) && (user != null)) {
                                    mCreateContractViewModel.createContract(user.getEmail(),
                                            mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude,
                                            mCreateContractViewModel.getUriPhoto().getPath(), mCreateContractViewModel.getChildName(),
                                            mCreateContractViewModel.getChildSurname(), mCreateContractViewModel.getChildLocation(), Contract.Status.NO_DIAGNOSIS.name());
                                    mCreateContractViewModel = null;
                                }
                            });

                    } else {
                        clView.stopOk();
                            mCreateContractViewModel.getUser().observe(getActivity(), user -> {
                                if ((mCreateContractViewModel != null) && (user != null)) {
                                    mCreateContractViewModel.createContract(user.getEmail(),
                                            mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude,
                                            mCreateContractViewModel.getUriPhoto().getPath(), mCreateContractViewModel.getChildName(),
                                            mCreateContractViewModel.getChildSurname(), mCreateContractViewModel.getChildLocation(), Contract.Status.DIAGNOSIS.name());
                                    mCreateContractViewModel = null;
                                }
                            });
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
        CreateContractViewModelFactory createContractViewModelFactory = CreateContractViewModelFactory.createFactory(getActivity());
        mCreateContractViewModel = ViewModelProviders.of(getActivity(), createContractViewModelFactory).get(CreateContractViewModel.class);
        return v;
    }

    @Override
    public VerificationError verifyStep() {
        if (position == 0 && !mCreateContractViewModel.isImageSelected()) {
            return new VerificationError(getString(R.string.error_photo));
        } else if (position == 1) {
            if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                || (etChildName.getText() == null) || (etChildName.getText().toString() == null) || (etChildName.getText().toString().isEmpty())
                    || (etChildSurname.getText() == null) || (etChildSurname.getText().toString() == null) || (etChildSurname.getText().toString().isEmpty())) {
                System.out.println("Aqui");
                return new VerificationError(getString(R.string.error_child_data));
            }
            mCreateContractViewModel.setChildLocation(etChildLocation.getText().toString());
            mCreateContractViewModel.setChildName(etChildName.getText().toString());
            mCreateContractViewModel.setChildSurname(etChildSurname.getText().toString());
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
                mCreateContractViewModel.setUriPhoto(result.getUri());
                Glide.with(getActivity().getApplicationContext())
                        .load(mCreateContractViewModel.getUriPhoto())
                        .into(ivTakePhoto);
                ivTakePhoto.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mCreateContractViewModel.setImageSelected(true);
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
        Nut4HealthSingleShotLocationProvider.requestSingleUpdate(getActivity().getApplicationContext(), newLocation -> {
            mCreateContractViewModel.setLocation(newLocation);
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude, 1);
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
