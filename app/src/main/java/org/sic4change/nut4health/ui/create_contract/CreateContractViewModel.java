package org.sic4change.nut4health.ui.create_contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.machinezoo.sourceafis.FingerprintTemplate;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.utils.fingerprint.AndroidBmpUtil;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

public class CreateContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private final LiveData<List<Point>> mPoints;
    private final LiveData<Contract> mContract;

    private Uri uriPhoto;
    private int percentage = -1;
    private Nut4HealthSingleShotLocationProvider.GPSCoordinates location = new Nut4HealthSingleShotLocationProvider.GPSCoordinates(0,0);
    private String childName;
    private String childSurname;
    private String childDNI;
    private String childTutor;
    private String childLocation;
    private String childPhoneContact;
    private boolean verification = false;
    private String point;
    private String pointFullName;
    private byte[] fingerPrint;
    private boolean imageSelected = false;

    public CreateContractViewModel(DataRepository repository) {
        this.mRepository = repository;
        mUser = this.mRepository.getCurrentUser();
        this.mRepository.getPoints();
        mPoints = mRepository.getSortedPoints();
        mContract = null;
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public LiveData<List<Point>> getPoints() {
        return mPoints;
    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void createContract(String id, String role, String screener, float latitude, float longitude, Uri photo,
                               String childName, String childSurname, String childDNI, String childTutor, String childAddress,
                               String childPhoneContact, String point, String pointFullName,
                               int percentage) {
        if (fingerPrint != null) {
            FingerprintTemplate fingerprintTemplateContract = new FingerprintTemplate().dpi(500).create(fingerPrint);
            mRepository.createContract(id, role, screener, latitude, longitude, photo, childName, childSurname, childDNI, childTutor,
                    childAddress, childPhoneContact, point, pointFullName, percentage, fingerprintTemplateContract.serialize());
        } else {
            mRepository.createContract(id, role, screener, latitude, longitude, photo, childName, childSurname, childDNI, childTutor,
                    childAddress, childPhoneContact, point, pointFullName, percentage, "");
        }

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

    public String getChildDNI() {
        return childDNI;
    }

    public void setChildDNI(String childDNI) {
        this.childDNI = childDNI;
    }

    public String getChildTutor() {
        return childTutor;
    }

    public void setChildTutor(String childTutor) {
        this.childTutor = childTutor;
    }

    public String getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(String childLocation) {
        this.childLocation = childLocation;
    }

    public String getChildPhoneContact() {
        return childPhoneContact;
    }

    public void setChildPhoneContact(String childPhoneContact) {
        this.childPhoneContact = childPhoneContact;
    }

    public boolean getVerification() {
        return verification;
    }

    public void setChildVerification(boolean verification) {
        this.verification = verification;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointFullName() {
        return pointFullName;
    }

    public void setPointFullName(String pointFullName) {
        this.pointFullName = pointFullName;
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
        String fname = Environment.getExternalStorageDirectory().toString() + "/req_images/Nut4HealthFingerPrint" +".jpg";
        byte[] image = AndroidBmpUtil.getByte(fname);
        return new String(image);
    }
}
