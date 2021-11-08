package com.game.TamilGuru.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.game.TamilGuru.data.api.model.UserData;


public class SharedPrefHelper {
    private static final String SHARED_PREFERENCE_USER_ID = "user_id";
    private static final String SHARED_PREFERENCE_DEFAULT_STRING = "";
    private static final String SHARED_PREFERENCE_USER_TOKEN = "user_token";
    private static final String SHARED_PREFERENCE_USER_NAME = "user_name";
    private static final String SHARED_PREFERENCE_USER_EMAIL = "user_email";
    private static final String SHARED_PREFERENCE_USER_PROFILE_PIC_URL = "user_profile_pic_url";
    private static final String SHARED_PREFERENCES_APP_VERSION_CODE = "user_app_version_code";
    private static final String SHARED_PREFERENCES_PROFILE_TYPE = "user_profile_type";
    private static final String SHARED_PREFERENCE_USER_REGISTRATION_CLASS_LIST = "user_registration_class_list";

    private static SharedPrefHelper sharedPrefHelper;
    private static Context applicationContext;
    private SharedPreferences sharedpreferences;
    public static final String SHARED_PREFERENCES_NAME = "tamilGuruSharedPrefs";
    public static final int SHARED_PREFERENCE_DEFAULT_INT = -1;
    public static synchronized SharedPrefHelper getInstance(Context context) {
        //using application context is recommended.
        if (sharedPrefHelper == null) {
            applicationContext = context;
            sharedPrefHelper = new SharedPrefHelper();
        }
        return sharedPrefHelper;
    }

    public SharedPrefHelper() {
        createSharedPref();
    }

    private void createSharedPref() {
        sharedpreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    public String getUserEmail() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_STRING;

        return sharedpreferences.getString(SHARED_PREFERENCE_USER_EMAIL, SHARED_PREFERENCE_DEFAULT_STRING);

    }

    public void setEmail(String email) {
        if (sharedpreferences == null) {
            createSharedPref();
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_EMAIL, email);
        editor.apply();
    }
    public String getUserName() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_STRING;

        return sharedpreferences.getString(SHARED_PREFERENCE_USER_NAME, SHARED_PREFERENCE_DEFAULT_STRING);

    }

    public void setUserName(String name) {

        if (sharedpreferences == null)
            createSharedPref();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(SHARED_PREFERENCE_USER_NAME, name);

        editor.apply();

    }
    public UserData getUser() {

        return new UserData(getUserId(),
                getUserName(),
                getUserEmail(),
                getUserToken(),
                getAppVersionCode(),
                getProfileType()
        );
    }
    public int getUserId() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_INT;

        return sharedpreferences.getInt(SHARED_PREFERENCE_USER_ID, SHARED_PREFERENCE_DEFAULT_INT);

    }

    public void setUserId(int userId) {

        if (sharedpreferences == null)
            createSharedPref();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(SHARED_PREFERENCE_USER_ID, userId);

        editor.apply();

    }

    public String getUserToken() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_STRING;

        return sharedpreferences.getString(SHARED_PREFERENCE_USER_TOKEN, SHARED_PREFERENCE_DEFAULT_STRING);

    }

    public void setUserToken(String token) {

        if (sharedpreferences == null)
            createSharedPref();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(SHARED_PREFERENCE_USER_TOKEN, token);

        editor.apply();

    }
    public int getAppVersionCode() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_INT;

        return sharedpreferences.getInt(SHARED_PREFERENCES_APP_VERSION_CODE, SHARED_PREFERENCE_DEFAULT_INT);

    }

    public void setAppVersionCode(int versionCode) {

        if (sharedpreferences == null)
            createSharedPref();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(SHARED_PREFERENCES_APP_VERSION_CODE, versionCode);

        editor.apply();
    }


    public int getProfileType() {
        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_INT;

        return sharedpreferences.getInt(SHARED_PREFERENCES_PROFILE_TYPE, SHARED_PREFERENCE_DEFAULT_INT);

    }
    public int getProfileLastUpdate() {
        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_INT;

        return sharedpreferences.getInt(SHARED_PREFERENCES_PROFILE_TYPE, SHARED_PREFERENCE_DEFAULT_INT);

    }

    public void setProfileType(int profileType) {

        if (sharedpreferences == null)
            createSharedPref();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(SHARED_PREFERENCES_PROFILE_TYPE, profileType);

        editor.apply();
    }
    public String getUserProfilePicUrl() {

        if (sharedpreferences == null)
            return SHARED_PREFERENCE_DEFAULT_STRING;

        return sharedpreferences.getString(SHARED_PREFERENCE_USER_PROFILE_PIC_URL, SHARED_PREFERENCE_DEFAULT_STRING);

    }

    public void setUserProfilePicUrl(String profilePicUrl) {
        if (sharedpreferences == null) {
            createSharedPref();
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SHARED_PREFERENCE_USER_PROFILE_PIC_URL, profilePicUrl);
        editor.apply();
    }
    public void setUser(UserData userData) {
        sharedPrefHelper.setUserId(userData.getId());

        sharedPrefHelper.setUserName(userData.getName());

        //sharedPrefHelper.setEmail(userData.getEmail());

        sharedPrefHelper.setUserToken(userData.getToken());

        sharedPrefHelper.setAppVersionCode(userData.getAppVersionCode());
        //sharedPrefHelper.setProfileType(userData.getProfileType());
    }
    public String[] getTeacherRegistrationSubjectArray() {
        String registrationSubjectsStr = sharedpreferences.getString(SHARED_PREFERENCE_USER_REGISTRATION_CLASS_LIST, "");
        return registrationSubjectsStr.split(Constants.COMMA_WITH_SPACE_DELIMITER);
    }
    public void clearPref() {
        sharedpreferences.edit().clear().apply();
    }
}
