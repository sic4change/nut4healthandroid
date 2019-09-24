package org.sic4change.nut4health.ui.create_contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.IOException;
import java.nio.ByteBuffer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.utils.fingerprint.AndroidBmpUtil;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

public class CreateContractViewModel extends ViewModel {

    private static final int BMP_WIDTH_OF_TIMES = 4;
    private static final int BYTE_PER_PIXEL = 3;

    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private final LiveData<Contract> mContract;

    private Uri uriPhoto;
    private int percentage;
    private Nut4HealthSingleShotLocationProvider.GPSCoordinates location;
    private String childName;
    private String childSurname;
    private String childLocation;
    private byte[] fingerPrint;
    private boolean imageSelected = false;

    public CreateContractViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();
        mContract = null;
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void createContract(String role, String screener, float latitude, float longitude, Uri photo, String childName,
                               String childSurname, String childAddress, String fingerprint, int percentage, int points) {
        mRepository.createContract(role, screener, latitude, longitude, photo, childName, childSurname,
                childAddress, fingerprint, percentage, points);
    }

    public void updatePointsUserLocal(String email) {
        mRepository.updatePointsUserLocal(email, mUser.getValue().getPoints() + 1);
    }


    public Uri getUriPhoto() {
        return uriPhoto;
    }

    public void setUriPhoto(Uri uriPhoto) {
        this.uriPhoto = uriPhoto;
    }

    public Nut4HealthSingleShotLocationProvider.GPSCoordinates getLocation() {
        return location;
    }

    public void setLocation(Nut4HealthSingleShotLocationProvider.GPSCoordinates location) {
        this.location = location;
    }

    public boolean isImageSelected() {
        return imageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        this.imageSelected = imageSelected;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildSurname() {
        return childSurname;
    }

    public void setChildSurname(String childSurname) {
        this.childSurname = childSurname;
    }

    public String getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(String childLocation) {
        this.childLocation = childLocation;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public byte[] getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(byte[] fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Bitmap getFingerPrintImage() {
        return BitmapFactory.decodeByteArray(fingerPrint, 0, fingerPrint.length);
    }

    public String getFingerPrintString() {
        return new String(this.fingerPrint);
    }
}
