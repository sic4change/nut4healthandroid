package org.sic4change.nut4health.data.entities;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.sic4change.nut4health.data.names.DataUserNames;

@Entity(tableName = DataUserNames.TABLE_NAME)
public class User {

    @ColumnInfo(name = DataUserNames.COL_USER_ID)
    private String id;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DataUserNames.COL_EMAIL)
    private String email;

    @ColumnInfo(name = DataUserNames.COL_USERNAME)
    private String username;

    @ColumnInfo(name = DataUserNames.COL_NAME)
    private String name;

    @ColumnInfo(name = DataUserNames.COL_SURNAME)
    private String surname;

    @ColumnInfo(name = DataUserNames.COL_COUNTRY)
    private String country;

    @ColumnInfo(name = DataUserNames.COL_CURRENT_COUNTRY)
    private String currentCountry;

    @ColumnInfo(name = DataUserNames.COL_CURRENT_STATE)
    private String currentState;

    @ColumnInfo(name = DataUserNames.COL_CURRENT_CITY)
    private String currentCity;

    @ColumnInfo(name = DataUserNames.COL_COUNTRYCODE)
    private String countryCode;

    @ColumnInfo(name = DataUserNames.COL_PHOTO)
    private String photo;

    @ColumnInfo(name = DataUserNames.COL_ROLE)
    private String role;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_POINTS)
    private int points;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_CREATION_DATE)
    private String creationDate;

    @ColumnInfo(name = DataUserNames.COL_EMPTY_USER)
    private boolean emptyUser;

    @ColumnInfo(name = DataUserNames.COL_ACTIVE)
    private boolean active;

    public static final String EMPTY_EMAIL = "empty@emtpy.com";

    public static final User userEmpty = new User(EMPTY_EMAIL, "", "", "", "", "", "", 0, "", true, false);

    public User() {
        this("","","","","","","", "",0, "", false, "", "", "", true);
    }

    public User(String email) {
        this(email, "", "", "", "", "", "", "", 0, "",  false, "", "", "", true);
    }

    public User(String email, String username, String role) {
        this(email, username, "", "", "", "", "", role, 0, "", false, "", "", "", true);
    }

    public User(String email, String username, String name, String surname, String country, String countryCode,
                String role, int points, String creationDate, boolean emptyUser, boolean active) {
        this(email, username, name, surname, country, countryCode, "", role, points, creationDate, emptyUser, "", "", "", active);
    }

    public User(String email, String username, String name, String surname, String country, String countryCode,
                String photo, String role, int points, String creationDate, boolean emptyUser,
                String currentCountry, String currentState, String currentCity, boolean active) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.countryCode = countryCode;
        this.photo = photo;
        this.role = role;
        this.points = points;
        this.creationDate = creationDate;
        this.emptyUser = emptyUser;
        this.currentCountry = currentCountry;
        this.currentState = currentState;
        this.currentCity = currentCity;
        this.active = active;
    }

    public User(@NonNull String id, @NonNull String email, String username, String name, String surname,
                String country, String countryCode, String photo, String role, int points,
                String creationDate, String currentCountry, String currentState, String currentCity, boolean active) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.countryCode = countryCode;
        this.photo = photo;
        this.role = role;
        this.points = points;
        this.creationDate = creationDate;
        this.currentCountry = currentCountry;
        this.currentState = currentState;
        this.currentCity = currentCity;
        this.active = active;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEmptyUser() {
        return emptyUser;
    }

    public void setEmptyUser(boolean emptyUser) {
        this.emptyUser = emptyUser;
    }

    @NonNull
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @NonNull
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NonNull String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
