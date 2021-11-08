package com.game.TamilGuru.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.game.TamilGuru.R;
import com.game.TamilGuru.data.api.RetrofitClient;
import com.game.TamilGuru.data.api.errorHandling.ApiStatusResponse;
import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.LoginDetails;
import com.game.TamilGuru.data.api.model.UserData;
import com.game.TamilGuru.data.database.TamilGuruDatabase;
import com.game.TamilGuru.data.database.daos.TgLoginDao;
import com.game.TamilGuru.data.database.entities.TgLoginData;
import com.game.TamilGuru.utils.Common;
import com.game.TamilGuru.utils.Constants;
import com.game.TamilGuru.utils.SharedPrefHelper;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationRepository {
    private final Context applicationContext;
    private final SharedPrefHelper mSharedPref;
    private final TgLoginDao tgLoginDao;

    private final TamilGuruDatabase tamilGuruDatabase;

    public RegistrationRepository(Context applicationContext, TamilGuruDatabase tamilGuruDatabase) {
        this.applicationContext = applicationContext;
        this.tgLoginDao = tamilGuruDatabase.tgLoginDao();
        this.mSharedPref = SharedPrefHelper.getInstance(applicationContext);

        this.tamilGuruDatabase = tamilGuruDatabase;
    }
    public LiveData<Resource<ApiStatusResponse>> updateProfile(UserData userData) {

        MutableLiveData<Resource<ApiStatusResponse>> apiResponse = new MutableLiveData<>();

        if (!Common.isInternetConnected(applicationContext)) {
            apiResponse.postValue(Resource.error(applicationContext.getResources().getString(R.string.connection_error_msg), new ApiStatusResponse(Constants.STATUS_CODE_CONNECTIVITY_ISSUE, applicationContext.getResources().getString(R.string.connection_error_msg))));
            return apiResponse;
        }

        Call<LoginDetails> registerProfileCall = RetrofitClient.getInstance(applicationContext)
                .getLoginAndRegistrationApis()
                .updateProfile(Constants.BEARER_TOKEN_PREFIX + mSharedPref.getUserToken(), userData);

        registerProfileCall.enqueue(new Callback<LoginDetails>() {
            @Override
            public void onResponse(@NotNull Call<LoginDetails> call, @NotNull Response<LoginDetails> response) {
                LoginDetails body = response.body();
                if (body == null) {
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(response.code(), response.message())));
                    return;
                }

                if (body.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                    UserData responseProfileData = body.getLoginData();
                    if (Common.isObjectNotNullOrEmpty(responseProfileData)) {
                        responseProfileData.setId(userData.getId());
                        if (Common.isObjectNotNullOrEmpty(responseProfileData.getName())) {
                            TgLoginData agsLead = new TgLoginData(responseProfileData.getId(), responseProfileData.getName(), responseProfileData.getClassName());
                            insertLeadTable(agsLead);
                        }
                    }
                } else if (body.getStatus() == Constants.STATUS_CODE_TOKEN_MISMATCH) {
                    clearAllTables();
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(body.getStatus(), body.getMessage())));
                } else {
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(body.getStatus(), body.getMessage())));
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginDetails> call, @NotNull Throwable t) {
                if (t instanceof RetrofitClient.NoConnectivityException) {
                    // No internet connection
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(Constants.STATUS_CODE_CONNECTIVITY_ISSUE, applicationContext.getResources().getString(R.string.connection_error_msg))));
                } else if (t instanceof SocketTimeoutException) {
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(Constants.STATUS_CODE_TIMEOUT, applicationContext.getResources().getString(R.string.connection_timeout_msg))));
                } else {
                    apiResponse.postValue(Resource.error("", new ApiStatusResponse(-1, t.getMessage())));
                }
            }
        });

        return apiResponse;
    }
    public void clearAllTables(){
        TamilGuruDatabase.databaseWriteExecutor.execute(tamilGuruDatabase::clearAllTables);
    }
    private void insertLeadTable(TgLoginData agsLead) {
        TamilGuruDatabase.databaseWriteExecutor.execute(() -> TgLoginDao.insert(agsLead));
    }

}
