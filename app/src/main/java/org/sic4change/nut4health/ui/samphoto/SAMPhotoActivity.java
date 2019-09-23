package org.sic4change.nut4health.ui.samphoto;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.sic4change.animation_check.AnimatedCircleLoadingView;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.ui.create_contract.CreateContractActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class SAMPhotoActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private final String tag = "VideoServer";

    public static String PHOTO_PATH = "photo_path";
    public static String PERCENTAGE = "percentage";

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera.PictureCallback rawCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera.PictureCallback jpegCallback;

    private int percentage = 0;

    private FloatingActionButton btnCapture;

    private AnimatedCircleLoadingView clView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samphoto);
        btnCapture = findViewById(R.id.btnCapture);
        clView = findViewById(R.id.clView);
        btnCapture.setVisibility(View.VISIBLE);
        btnCapture.setOnClickListener(v -> {
            btnCapture.setVisibility(View.GONE);
            captureImage();
        });
        clView.setVisibility(View.GONE);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        rawCallback = (data, camera) -> Log.d("Log", "onPictureTaken - raw");

        jpegCallback = (data, camera) -> {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("Error", "Error creating media file, check storage permissions");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile, false);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Error", "Error accessing file: " + e.getMessage());
            }

            stopCamera();


        };
    }

    private void startPercentMockThread(int random) {
        Runnable runnable = () -> {
            try {
                Thread.sleep(1500);
                for (int i = 0; i <= random; i++) {
                    Thread.sleep(65);
                    changePercent(i);
                }
                clView.stopOk();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(() -> clView.setPercent(percent));
    }

    private void captureImage() {
        btnCapture.setEnabled(false);
        btnCapture.setClickable(false);
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        clView.setVisibility(View.VISIBLE);
        clView.startDeterminate();
        final int min = 1;
        final int max = 100;
        final int random = new Random().nextInt((max - min) + 1) + min;
        percentage = random;
        startPercentMockThread(random);
        //clView.stopOk();
        clView.setAnimationListener(new AnimatedCircleLoadingView.AnimationListener() {
            @Override
            public void onAnimationEnd(boolean success) {

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        goToCreateContractActivityWithData();
                    }
                }.start();

            }
        });
    }

    private void startCamera() {
        try {
            camera = Camera.open();
        } catch(RuntimeException e){
            Log.e(tag, "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e(tag, "init_camera: " + e);
            return;
        }
    }

    private void stopCamera() {
        try {
            camera.stopPreview();
            camera.release();
        } catch (Exception e) {
            Log.e(tag, "close_camera: " + e);
        }
    }

    @Override
    public void onBackPressed() {
        stopCamera();
        goToCreateContractActivityWithoutData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stopCamera();
                goToCreateContractActivityWithoutData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NUT4Health");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("NUT4Health", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_SAMPhoto.jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    private void goToCreateContractActivityWithData() {
        Intent intent = new Intent(this, CreateContractActivity.class);
        intent.putExtra(PHOTO_PATH, getOutputMediaFileUri(MEDIA_TYPE_IMAGE).toString());
        intent.putExtra(PERCENTAGE, percentage);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void goToCreateContractActivityWithoutData() {
        Intent intent = new Intent(this, CreateContractActivity.class);
        finish();
    }

}