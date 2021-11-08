package com.game.TamilGuru.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.game.TamilGuru.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class Common {

    public static void putDebugLog(String message) {
        if (Constants.DEVELOPMENT_MODE) {
            Log.d(Constants.DEBUG_TAG, message);
        }
    }

    public static void putErrorLog(String message) {
        if (Constants.DEVELOPMENT_MODE) {
            Log.e(Constants.DEBUG_TAG, message);
        }
    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (WindowManager.BadTokenException exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
        }

    }
    public static boolean isObjectNotNullOrEmpty(Object object) {
        if (null != object) {
            return (!object.toString().equals(""));
        } else {
            return false;
        }
    }
    public static void trackInMixpanel(Context context, JSONObject props, String eventName) {
        if (Constants.DEVELOPMENT_MODE) {
            return;
        }
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, Constants.MIXPANEL_TOKEN);
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(context);
        try {
            if (!isObjectNotNullOrEmpty(props)) {
                props = new JSONObject();
            }
            props.put("user_email", sharedPrefHelper.getUserEmail());
            props.put("user_name", sharedPrefHelper.getUserName());

            mixpanel.track(eventName, props);
        } catch (JSONException jsonException) {
            Common.putDebugLog("Mixpanel couldn't log properties");
        }
    }
    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static boolean isGivenStringNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    public static void editBoxTextFilter(CharSequence inputStr, EditText parentNameEditText, String allowedTextRegex) {
        String newStr = inputStr.toString();
        newStr = newStr.replaceAll(allowedTextRegex, "");
        if (!inputStr.toString().equals(newStr)) {
            parentNameEditText.setText(newStr);
            parentNameEditText.setSelection(newStr.length());
        }
    }
    public static void tokenMismatchHandle(Activity activity) {
        signOut(activity);
    }

    public static void signOut(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please wait..");
        progressDialog.show();
        GoogleSignInHelper googleSignInHelper = GoogleSignInHelper.getInstance();
        GoogleSignInClient mClient = googleSignInHelper.getClient(activity.getApplicationContext());
        mClient.signOut().addOnCompleteListener(activity, task -> {
            SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(activity.getApplicationContext());
            sharedPrefHelper.clearPref();
            Intent newIntent = new Intent(activity, LoginActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(newIntent);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }

}
