package org.sic4change.nut4health.ui.contract_detail;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.Point;

public class DetailContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<Contract> mContract = null;
    private LiveData<Point> mPoint = null;
    private String role = "";
    private double arm_circumference_medical = 0.0;

    public DetailContractViewModel(DataRepository repository, String id) {
        this.mRepository = repository;
        mContract = this.mRepository.getContract(id);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LiveData<Contract> getContract() {
        return mContract;
    }

    public void getContract(String id) {
        mRepository.getContract(id);
    }

    public LiveData<Point> getPoint() {
        return mPoint;
    }

    public void getPoint(String id) {
        mRepository.getPoint(id);
    }

    public double getArmCircumferenceMedical() {
        return arm_circumference_medical;
    }

    public void setArmCircumferenceMedical(double arm_circumference_medical) {
        this.arm_circumference_medical = arm_circumference_medical;
    }

    public DataRepository getRepository() {
        return mRepository;
    }

    public void validateDiagnosis(String id) {
        mRepository.validateDiagnosis(id, getArmCircumferenceMedical());
    }

}
