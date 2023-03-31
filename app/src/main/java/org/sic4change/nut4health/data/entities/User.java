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

    @ColumnInfo(name = DataUserNames.COL_CONFIGURATION)
    private String configuration;

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

    @ColumnInfo(name = DataUserNames.COL_POINT)
    private String point;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_POINTS)
    private int points;

    @NonNull
    @ColumnInfo(name = DataUserNames.COL_CREATION_DATE)
    private String creationDate;

    public static final String EMPTY_EMAIL = "empty@emtpy.com";

    public static final User userEmpty = new User(EMPTY_EMAIL, "", "", "", "", "", "", "", 0, "", "");

    public User() {
        this("", "","","","","","","", "",0, "", "", "", "", "");
    }

    public User(String email) {
        this(email, "", "", "", "", "", "", "", "", 0, "", "",   "", "", "");
    }

    public User(String email, String configuration, String role) {
        this(email, configuration, "", "", "", "", "", "", role, 0, "", "", "", "", "");
    }

    public User(String email, String configuration, String username, String role) {
        this(email, configuration, username, "", "", "", "", "", role, 0, "", "", "", "", "");
    }

    public User(String email, String configuration, String username, String name, String surname, String country, String countryCode,
                String role, int points, String point, String creationDate) {
        this(email, configuration, username, name, surname, country, countryCode, "", role, points, point, creationDate, "", "", "");
    }

    public User(String email, String configuration, String username, String name, String surname, String country, String countryCode,
                String photo, String role, int points, String point, String creationDate,
                String currentCountry, String currentState, String currentCity) {
        this.email = email;
        this.configuration = configuration;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.countryCode = countryCode;
        this.photo = photo;
        this.role = role;
        this.points = points;
        this.point = point;
        this.creationDate = creationDate;
        this.currentCountry = currentCountry;
        this.currentState = currentState;
        this.currentCity = currentCity;
    }

    public User(@NonNull String id, @NonNull String email, String configuration, String username, String name, String surname,
                String country, String countryCode, String photo, String role, int points, String point,
                String creationDate, String currentCountry, String currentState, String currentCity, boolean active) {
        this.id = id;
        this.email = email;
        this.configuration = configuration;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.countryCode = countryCode;
        this.photo = photo;
        this.role = role;
        this.points = points;
        this.point = point;
        this.creationDate = creationDate;
        this.currentCountry = currentCountry;
        this.currentState = currentState;
        this.currentCity = currentCity;
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

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(@NonNull String configuration) {
        this.configuration = configuration;
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


    @NonNull
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
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

}
