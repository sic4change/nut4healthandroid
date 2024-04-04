package org.sic4change.nut4health.ui.create_contract;


import androidx.annotation.NonNull;

public class PointFormatted {

    private String pointId;
    private boolean active;
    private String fullName;
    private String phoneCode;
    private int order;

    PointFormatted() {
        this("", false,"", "", 0);
    }

    public PointFormatted(@NonNull String pointId, boolean active, String fullName, String phoneCode, int order) {
        this.pointId = pointId;
        this.active = active;
        this.fullName = fullName;
        this.phoneCode = phoneCode;
        this.order = order;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
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




