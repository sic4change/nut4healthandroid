package org.sic4change.nut4health.data.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.sic4change.nut4health.data.entities.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserConverter {

    @TypeConverter
    public static ArrayList<User> fromString(String value) {
        Type listType = new TypeToken<ArrayList<User>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<User> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
