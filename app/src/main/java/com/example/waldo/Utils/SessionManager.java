package com.example.waldo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    Context context;
    private static final String PREF_NAME = "waldo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String SELECTED_CAT = "cat";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setCategoryName(String category_name) {
        editor.putString(SELECTED_CAT, category_name);
        editor.commit();
    }

    public String getCategoryName() {
        return sharedPreferences.getString(SELECTED_CAT, "");
    }

}
