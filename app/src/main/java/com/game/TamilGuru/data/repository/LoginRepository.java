package com.game.TamilGuru.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.game.TamilGuru.R;
import com.game.TamilGuru.data.api.RetrofitClient;
import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.LoginApiResponse;
import com.game.TamilGuru.data.api.model.LoginData;
import com.game.TamilGuru.data.api.model.LoginDetails;
import com.game.TamilGuru.data.api.model.LoginUserData;
import com.game.TamilGuru.data.api.model.UserData;
import com.game.TamilGuru.data.database.TamilGuruDatabase;
import com.game.TamilGuru.data.database.daos.TgLoginDao;
import com.game.TamilGuru.data.database.entities.TgLoginData;
import com.game.TamilGuru.utils.Common;
import com.game.TamilGuru.utils.Constants;
import com.game.TamilGuru.utils.SharedPrefHelper;
import com.google.android.datatransport.backend.cct.BuildConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final Context applicationContext;
    private final TamilGuruDatabase tamilGuruDatabase;
    private final SharedPrefHelper mSharedPrefHelper;
    private final TgLoginDao tgLoginDao;


    public LoginRepository(Context applicationContext, TamilGuruDatabase tamilGuruDatabase) {
        this. applicationContext= applicationContext;
        mSharedPrefHelper = SharedPrefHelper.getInstance(applicationContext);
        tgLoginDao=tamilGuruDatabase.tgLoginDao();
        this.tamilGuruDatabase = tamilGuruDatabase;
    }
    public LiveData<Resource<LoginApiResponse>> loginIntoAhaGuruServer(GoogleSignInAccount googleSignInData) {
        //login api call
        MutableLiveData<Resource<LoginApiResponse>> apiResponse = new MutableLiveData<>();

        if (googleSignInData == null) {
            return apiResponse;
        }

        if (!Common.isInternetConnected(applicationContext)) {
            apiResponse.postValue(Resource.error("", new LoginApiResponse(Constants.STATUS_CODE_CONNECTIVITY_ISSUE, applicationContext.getResources().getString(R.string.connection_error_msg),-1)));
            return apiResponse;
        }

        LoginUserData loginUserData = new LoginUserData(googleSignInData.getGivenName(), BuildConfig.VERSION_CODE);

        Call<LoginDetails> login = RetrofitClient.getInstance(applicationContext).getLoginAndRegistrationApis().login(loginUserData);

        login.enqueue(new Callback<LoginDetails>() {
            @Override
            public void onResponse(@NotNull Call<LoginDetails> call, @NotNull Response<LoginDetails> response) {
                //LoginDetails body = LoginDetails.fromJson(Constants.TEACHER_DATA);
                LoginDetails body =response.body();
                body.getLoginData().setToken(response.body().getLoginData().getToken());
                if (body != null) {
                    if (body.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        apiResponse.postValue(updateLoginDetails(body.getLoginData(),googleSignInData));
                    } else if(body.getStatus() == Constants.STATUS_CODE_TOKEN_MISMATCH){
                        clearAllTables();
                        apiResponse.postValue(Resource.error("", new LoginApiResponse(body.getStatus(), body.getMessage(),-1)));
                    } else {
                        apiResponse.postValue(Resource.error(body.getMessage(), new LoginApiResponse(body.getStatus(), body.getMessage(),-1)));
                    }
                } else {
                    apiResponse.postValue(Resource.error(response.message(), new LoginApiResponse(response.code(), response.message(),-1)));
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginDetails> call, @NotNull Throwable t) {
                if (t instanceof RetrofitClient.NoConnectivityException) {
                    // No internet connection
                    apiResponse.postValue(Resource.error("", new LoginApiResponse(Constants.STATUS_CODE_CONNECTIVITY_ISSUE, applicationContext.getResources().getString(R.string.connection_error_msg),-1)));
                } else if (t instanceof SocketTimeoutException) {
                    apiResponse.postValue(Resource.error("", new LoginApiResponse(Constants.STATUS_CODE_TIMEOUT, applicationContext.getResources().getString(R.string.connection_timeout_msg),-1)));
                } else {
                    apiResponse.postValue(Resource.error("", new LoginApiResponse(-1, t.getMessage(),-1)));
                }
            }
        });

        return apiResponse;

    }
    private Resource<LoginApiResponse> updateLoginDetails(UserData userData, GoogleSignInAccount googleSignInData) {
        if(userData == null){
            return Resource.error("",new LoginApiResponse(Constants.DATA_NOT_FOUND, "data not available",-1));
        }
        userData.setName(googleSignInData.getGivenName());
        mSharedPrefHelper.setUser(userData);
        if (userData.getProfileType() == Constants.PROFILE_TYPE_LOGIN) {
            LoginData loginData = userData.getLoginData();
            if (loginData != null && loginData.getId() != 0) {
                TgLoginData tgLoginData = new TgLoginData(loginData.getId(), loginData.getName(), loginData.getClass_Name());

                TamilGuruDatabase.databaseWriteExecutor.execute(() -> TgLoginDao.insert(tgLoginData));

                return Resource.success(new LoginApiResponse(Constants.STATUS_CODE_SUCCESS, "success", -1));
            } else {
                // todo: take to profile selection page
                return Resource.error("", new LoginApiResponse(Constants.STATUS_CODE_RESPONSE_NOT_FOUND, "can't get Login data", -1));
            }
        }
        else{
            return Resource.error("", new LoginApiResponse(Constants.STATUS_CODE_RESPONSE_NOT_FOUND, "profile type mismatch", -1));
        }

    }


    public void clearAllTables(){
        TamilGuruDatabase.databaseWriteExecutor.execute(tamilGuruDatabase::clearAllTables);
    }

}
