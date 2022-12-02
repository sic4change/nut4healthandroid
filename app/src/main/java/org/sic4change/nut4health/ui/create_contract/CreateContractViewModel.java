package org.sic4change.nut4health.ui.create_contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.machinezoo.sourceafis.FingerprintTemplate;

import org.apache.commons.collections4.Predicate;
import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Point;
import org.sic4change.nut4health.data.entities.User;
import org.sic4change.nut4health.data.names.DataPointNames;
import org.sic4change.nut4health.utils.fingerprint.AndroidBmpUtil;
import org.sic4change.nut4health.utils.location.Nut4HealthSingleShotLocationProvider;

public class CreateContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<User> mUser;
    private final LiveData<List<Point>> mPoints;
    private final LiveData<Contract> mContract;

    private Uri uriPhoto;
    private int percentage = -1;
    private double arm_circumference = 0.0;
    private Nut4HealthSingleShotLocationProvider.GPSCoordinates location =
            new Nut4HealthSingleShotLocationProvider.GPSCoordinates(0,0);
    private String childName;
    private String childSurname;
    private String childBrothers;
    private String sex = "H";
    private String childDNI;
    private String code;
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

    public LiveData<List<Point>> getPoints(String pointDefaultId) {
        if (pointDefaultId == null || pointDefaultId.isEmpty() || mPoints.getValue() == null) {
            return mPoints;
        } else {
            Point pointDefault = null;
            for (Point point : mPoints.getValue()) {
                if (point.getPointId().equals(pointDefaultId)) {
                    pointDefault = point;
                    break;
                }
            }
            List<PointFormatted> points = new ArrayList<PointFormatted>();
            for (Point i : mPoints.getValue()) {
                int count = 0;
                for (String j : i.getFullName().split(",")) {
                    if (pointDefault.getFullName().replace(" ", "").contains(j.replace(" ", ""))) {
                        count++;
                    }
                }
                points.add(new PointFormatted(i.getPointId(), i.getFullName(), count));
            }
            mPoints.getValue().clear();
            Collections.sort(points, Comparator.comparingInt(PointFormatted::getOrder).reversed());
            for (PointFormatted k : points) {
                mPoints.getValue().add(new Point(k.getPointId(), k.getFullName()));
            }
            return mPoints;
        }

    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void createContract(String id, String role, String screener, float latitude, float longitude,
                               Uri photo, String childName, String childSurname, String sex,
                               String childDNI, int childBrothers, String childTutor, String childAddress,
                               String childPhoneContact, String point, String pointFullName,
                               int percentage, double arm_circumference) {
        String code = childPhoneContact + "-" + childBrothers;
        /*if (fingerPrint != null) {
            FingerprintTemplate fingerprintTemplateContract = new FingerprintTemplate().dpi(500).create(fingerPrint);
            mRepository.createContract(id, role, screener, latitude, longitude, photo, childName, childSurname, sex, childDNI, childTutor,
                    childAddress, childPhoneContact, point, pointFullName, percentage, arm_circumference, fingerprintTemplateContract.serialize());
        } else {
            mRepository.createContract(id, role, screener, latitude, longitude, photo, childName,
                    childSurname, sex, childDNI, childTutor, childAddress, childPhoneContact, point,
                    pointFullName, percentage, arm_circumference, "");
        }*/
        mRepository.createContract(id, role, screener, latitude, longitude, photo, childName,
                childSurname, sex, childDNI, childBrothers, code, childTutor, childAddress, childPhoneContact, point,
                pointFullName, percentage, arm_circumference, "");

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

    public String getChildBrothers() {
        return childBrothers;
    }

    public void setChildBrothers(String childBrothers) {
        this.childBrothers = childBrothers;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex.equals("RIGHT")) {
            this.sex = "F";
        } else {
            this.sex = "M";
        }

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

    public double getArmCircumference() {
        return arm_circumference;
    }

    public void setArmCircumference(double arm_circumference) {
        this.arm_circumference = arm_circumference;
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



class PointFormatted {

    private String pointId;
    private String fullName;
    private int order;

    PointFormatted() {
        this("", "", 0);
    }

    PointFormatted(@NonNull String pointId, String fullName, int order) {
        this.pointId = pointId;
        this.fullName = fullName;
        this.order = order;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String toString() {
        return this.fullName;
    }

}


