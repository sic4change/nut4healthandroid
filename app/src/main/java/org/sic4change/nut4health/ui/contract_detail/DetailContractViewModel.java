package org.sic4change.nut4health.ui.contract_detail;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.sic4change.nut4health.data.DataRepository;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.data.entities.MalnutritionChildTable;
import org.sic4change.nut4health.data.entities.Point;

import java.util.List;

public class DetailContractViewModel extends ViewModel {

    private final DataRepository mRepository;
    private LiveData<Contract> mContract = null;
    private LiveData<Point> mPoint = null;
    private String role = "";
    private double arm_circumference_medical = 28.0;
    private double height = 0;
    private double weight = 0.0;
    private double imc = 0.0;

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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public DataRepository getRepository() {
        return mRepository;
    }

    public LiveData<List<MalnutritionChildTable>> getDesnutritionChildTable() {
        if (this.weight != 0 && this.height != 0.0) {
            return mRepository.getMalNutritionChildTable();
        }
        return null;
    }

    public String getStatus() {
        if (imc != 0) {
            if (arm_circumference_medical < 11.5) {
                return "Aguda Severa";
            } else if ((arm_circumference_medical >= 11.5 && arm_circumference_medical <= 12.5)) {
                if ((imc == -3.0)) {
                    return "Aguda Severa";
                } else {
                    return "Aguda Moderada";
                }
            } else {
                if ((imc == -3.0)) {
                    return "Aguda Severa";
                } else if (imc == -2.0) {
                    return "Aguda Moderada";
                } else {
                    return "Normopeso";
                }
            }
        } else {
            if (arm_circumference_medical < 11.5) {
                return "Aguda Severa";
            } else if (arm_circumference_medical >= 11.5 && arm_circumference_medical <= 12.5) {
                return "Aguda Moderada";
            } else {
                return "Normopeso";
            }
        }
    }

    public double checkMalnutritionByWeightAndHeight(List<MalnutritionChildTable> table) {
        if (table != null) {
            for (MalnutritionChildTable value : table) {
                if (value.getCm() >= (height - 0.1)) {
                    try {
                        if (weight < value.getMinusthree()) {
                            imc = -3.0;
                        } else if (weight >= value.getMinusthree() && weight < value.getMinustwo()) {
                            imc = -2;
                        } else {
                            imc = -1.5;
                        }
                    } catch (Exception e) {
                        if (height == 100) {
                            MalnutritionChildTable malNutritionChldTable = new MalnutritionChildTable(
                                    "X3fX5g2Fd9lpy0OVYkgA", 100, 14.2, 13.6,
                                    12.1, 13.1, 15.4
                            );
                            if (weight < value.getMinusthree()) {
                                imc = -3.0;
                            } else if (weight >= value.getMinusthree() && weight < value.getMinustwo()) {
                                imc = -2;
                            } else {
                                imc = -1.5;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return imc;
    }

    public void validateDiagnosis(String id) {
        mRepository.validateDiagnosis(id, getArmCircumferenceMedical(), getHeight(), getWeight());
    }

}
