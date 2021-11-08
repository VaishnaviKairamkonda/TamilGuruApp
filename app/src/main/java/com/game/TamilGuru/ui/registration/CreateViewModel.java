package com.game.TamilGuru.ui.registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.game.TamilGuru.data.api.errorHandling.ApiStatusResponse;
import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.UserData;
import com.game.TamilGuru.data.repository.RegistrationRepository;


public class CreateViewModel extends ViewModel {
    private final RegistrationRepository registrationRepository;
    private final MutableLiveData<UserData> profileDataMutableLiveData = new MutableLiveData<>();

    public CreateViewModel(RegistrationRepository registrationRepository ) {
        this.registrationRepository = registrationRepository;
        //this.supportedClassRepository = supportedClassRepository;
    }


    public LiveData<Resource<ApiStatusResponse>> updateProfile(){
        return Transformations.switchMap(profileDataMutableLiveData, registrationRepository::updateProfile);
    }
    public void setProfileDataMutableLiveData(UserData profileData) {
        profileDataMutableLiveData.setValue(profileData);
    }
}
