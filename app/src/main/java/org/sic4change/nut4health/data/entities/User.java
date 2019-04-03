package org.sic4change.nut4health.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.sic4change.nut4health.data.names.DataUserNames;

@Entity(tableName = "user")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataUserNames.COL_EMAIL)
    private String email;

    @ColumnInfo(name = DataUserNames.COL_USERNAME)
    private  String username;

    @ColumnInfo(name = DataUserNames.COL_NAME)
    private String name;

    @ColumnInfo(name = DataUserNames.COL_SURNAME)
    private String surname;

    @ColumnInfo(name = DataUserNames.COL_COUNTRY)
    private String country;

    @ColumnInfo(name = DataUserNames.COL_COUNTRYCODE)
    private String countryCode;

    @ColumnInfo(name = DataUserNames.COL_PHOTO)
    private String photo;

    @ColumnInfo(name = DataUserNames.COL_EMPTY_USER)
    private boolean emptyUser;

    public static final String EMPTY_EMAIL = "empty@emtpy.com";

    public static final User userEmpty = new User(EMPTY_EMAIL);

    public User() {
        this("","","","","","","", false);
    }

    public User(String email) {
        this(email, "", "", "", "", "", "", false);
    }

    public User(String email, String username) {
        this(email, username, "", "", "", "", "", false);
    }

    public User(String email, String username, String name, String surname, String country, String countryCode, boolean emptyUser) {
        this(email, username, name, surname, country, countryCode, "", emptyUser);
    }

    public User(String email, String username, String name, String surname, String country, String countryCode, String photo, boolean emptyUser) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.countryCode = countryCode;
        this.photo = photo;
        this.emptyUser = emptyUser;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isEmptyUser() {
        return emptyUser;
    }

    public void setEmptyUser(boolean emptyUser) {
        this.emptyUser = emptyUser;
    }
}
