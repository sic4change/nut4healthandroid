package org.sic4change.nut4health.ui.create_contract;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.main.MainActivity;
import org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static maes.tech.intentanim.CustomIntent.customType;
import static org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity.PERCENTAGE;
import static org.sic4change.nut4health.ui.samphoto.SAMPhotoActivity.PHOTO_PATH;

public class StepCreateContractFragment extends Fragment implements Step {

    private int position;

    private Button btnTakePhoto;
    private ImageView ivTakePhoto;
    private TextView tvPercentage;
    private CardView cvChild;
    private EditText etChildName;
    private EditText etChildSurname;
    private EditText etChildLocation;
    private Button btnCheckMalnutrition;
    private AnimatedCircleLoadingView clView;

    public static final int REQUEST_TAKE_PHOTO = 1;

    private static final long VERIFICATION_DELAY_MILISECONDS = 6000;
    private static final long VERIFICATION_TICK_MILISECONDS  = 1000;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 104;

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
            mCreateContractViewModel.getUser().observe(getActivity(), user -> {
                if ((mCreateContractViewModel != null) && (user != null)) {
                    mCreateContractViewModel.createContract(user.getEmail(),
                            mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude,
                            mCreateContractViewModel.getUriPhoto(), mCreateContractViewModel.getChildName(),
                            mCreateContractViewModel.getChildSurname(), mCreateContractViewModel.getChildLocation(),
                            mCreateContractViewModel.getPercentage());
                    mCreateContractViewModel = null;
                }
            });
            clView.stopOk();
            new CountDownTimer(VERIFICATION_DELAY_MILISECONDS, VERIFICATION_TICK_MILISECONDS) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    goToMainActivity();
                }
            }.start();

        });
        btnTakePhoto.setOnClickListener(v1 -> takePhoto());
        ivTakePhoto = v.findViewById(R.id.ivTakePhoto);
        tvPercentage = v.findViewById(R.id.tvPercentage);
        cvChild = v.findViewById(R.id.cvChild);
        etChildName = v.findViewById(R.id.etChildName);
        etChildSurname = v.findViewById(R.id.etChildSurname);
        etChildLocation = v.findViewById(R.id.etChildLocation);
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
    public VerificationError verifyStep() {
        if (position == 0 && !mCreateContractViewModel.isImageSelected()) {
            return new VerificationError(getString(R.string.error_photo));
        } else if (position == 1) {
            if ((etChildLocation.getText() == null) || (etChildLocation.getText().toString() == null) || (etChildLocation.getText().toString().isEmpty())
                    || (etChildName.getText() == null) || (etChildName.getText().toString() == null) || (etChildName.getText().toString().isEmpty())
                    || (etChildSurname.getText() == null) || (etChildSurname.getText().toString() == null) || (etChildSurname.getText().toString().isEmpty())) {
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
            tvPercentage.setVisibility(View.VISIBLE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
            if (mCreateContractViewModel.getChildLocation() != null) {
                Glide.with(getActivity().getApplicationContext())
                        .load(new File(mCreateContractViewModel.getUriPhoto().getPath()))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(ivTakePhoto);
                ivTakePhoto.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        } else if (getPosition() == 1) {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            cvChild.setVisibility(View.VISIBLE);
            btnCheckMalnutrition.setVisibility(View.GONE);
            clView.setVisibility(View.GONE);
            if (mCreateContractViewModel.getChildName() != null) {
                etChildName.setText(mCreateContractViewModel.getChildName());
            }
            if (mCreateContractViewModel.getChildSurname() != null) {
                etChildSurname.setText(mCreateContractViewModel.getChildSurname());
            }
            if (mCreateContractViewModel.getChildLocation() != null) {
                etChildLocation.setText(mCreateContractViewModel.getChildLocation());
            }
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
            tvPercentage.setVisibility(View.GONE);
            cvChild.setVisibility(View.GONE);
            btnCheckMalnutrition.setVisibility(View.VISIBLE);
            clView.setVisibility(View.GONE);
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
        }
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
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(mCreateContractViewModel.getLocation().latitude, mCreateContractViewModel.getLocation().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null) {
                        mCreateContractViewModel.setChildLocation(address);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToMainActivity() {
        mCreateContractViewModel = null;
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        customType(getActivity(),"right-to-left");
        getActivity().finish();
    }

}