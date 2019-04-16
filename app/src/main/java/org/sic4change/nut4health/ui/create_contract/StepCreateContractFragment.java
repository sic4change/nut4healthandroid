package org.sic4change.nut4health.ui.create_contract;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.sic4change.nut4health.R;


import static android.app.Activity.RESULT_OK;

public class StepCreateContractFragment extends Fragment implements Step {

    private int position;
    private boolean imageSelected = false;

    private Button btnTakePhoto;
    private ImageView ivTakePhoto;

    public static final int REQUEST_IMAGE_CAPTURE = 99;
    public static final int IMAGE_COMPRESS_QUALITY = 100;
    public static final int IMAGE_ASPECT_RATIO_X_Y = 3;

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
        btnTakePhoto.setOnClickListener(v1 -> takePhoto());
        ivTakePhoto = v.findViewById(R.id.ivTakePhoto);
        return v;
    }

    @Override
    public VerificationError verifyStep() {
        if (position == 0 && !imageSelected) {
            return new VerificationError(getString(R.string.error_photo));
        }
        return null;
    }

    @Override
    public void onSelected() {
        if (getPosition() == 0) {
            btnTakePhoto.setVisibility(View.VISIBLE);
            ivTakePhoto.setVisibility(View.VISIBLE);
        } else {
            btnTakePhoto.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void cropImage(Uri uri) {
        CropImage.activity(uri)
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
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            cropImage(selectedImage);
        }
    }

}
