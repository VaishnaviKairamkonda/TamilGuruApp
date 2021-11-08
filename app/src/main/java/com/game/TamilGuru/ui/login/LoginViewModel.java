package com.game.TamilGuru.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.LoginApiResponse;
import com.game.TamilGuru.data.repository.LoginRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


public class LoginViewModel extends ViewModel {

    private final LoginRepository loginRepository;
    private final MutableLiveData<GoogleSignInAccount> mGoogleSignInAccountData = new MutableLiveData<>();
    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<Resource<LoginApiResponse>> loginIntoAhaGuruServer() {
        return Transformations.switchMap(mGoogleSignInAccountData, loginRepository::loginIntoAhaGuruServer);
    }



    public void setGoogleSignInAccountData(GoogleSignInAccount result) {
        mGoogleSignInAccountData.setValue(result);
    }

}
