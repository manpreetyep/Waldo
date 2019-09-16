package com.example.waldo.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.waldo.Splash;

public class SessionManager {


    Context context;
    private static final String PREF_NAME = "waldo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String SELECTED_CAT = "cat";
    private static final String NAME = "name";
    private static final String ID = "cid";
    private static final String EMAIL = "email";
    private static final String PHONE_NO = "phone_no";
    private static final String PROFILE_IMG = "profile_img";
    private static final String DOB = "dob";
    private static final String ADDRESS = "address";
    private static final String BANK_ACCOUNT = "bank_account";
    private static final String USER_TYPE = "user_type";
    private static final String PROPERTY_ID = "property_id";
    private static final String PROPERTY_Address = "property_address";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Splash.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public void setCategoryName(String category_name) {
        editor.putString(SELECTED_CAT, category_name);
        editor.commit();
    }

    public String getCategoryName() {
        return sharedPreferences.getString(SELECTED_CAT, "");
    }

    public void setName(String param) {
        editor.putString(NAME, param);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(NAME, "");
    }

    public void setId(String param) {
        editor.putString(ID, param);
        editor.commit();
    }

    public String getId() {
        return sharedPreferences.getString(ID, "");
    }

    public void setEmail(String param) {
        editor.putString(EMAIL, param);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void setPhoneNo(String param) {
        editor.putString(PHONE_NO, param);
        editor.commit();
    }

    public String getPhoneNo() {
        return sharedPreferences.getString(PHONE_NO, "");
    }

    public void setProfileImg(String param) {
        editor.putString(PROFILE_IMG, param);
        editor.commit();
    }

    public String getProfileImg() {
        return sharedPreferences.getString(PROFILE_IMG, "");
    }

    public void setDob(String param) {
        editor.putString(DOB, param);
        editor.commit();
    }

    public String getDob() {
        return sharedPreferences.getString(DOB, "");
    }

    public void setAddress(String param) {
        editor.putString(ADDRESS, param);
        editor.commit();
    }

    public String getAddress() {
        return sharedPreferences.getString(ADDRESS, "");
    }

    public void setBankAccount(String param) {
        editor.putString(BANK_ACCOUNT, param);
        editor.commit();
    }

    public String getBankAccount() {
        return sharedPreferences.getString(BANK_ACCOUNT, "");
    }

    public void setUserType(String param) {
        editor.putString(USER_TYPE, param);
        editor.commit();
    }

    public String getUserType() {
        return sharedPreferences.getString(USER_TYPE, "");
    }
    public void setPropertyId(String param) {
        editor.putString(PROPERTY_ID, param);
        editor.commit();
    }

    public String getPropertyId() {
        return sharedPreferences.getString(PROPERTY_ID, "");
    }

    public void setPropertyAddress(String param) {
        editor.putString(PROPERTY_Address, param);
        editor.commit();
    }

    public String getPropertyAddress() {
        return sharedPreferences.getString(PROPERTY_Address, "");
    }

}
