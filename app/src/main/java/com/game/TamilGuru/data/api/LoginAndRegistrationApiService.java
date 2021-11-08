package com.game.TamilGuru.data.api;





import com.game.TamilGuru.data.api.model.LoginDetails;
import com.game.TamilGuru.data.api.model.LoginUserData;
import com.game.TamilGuru.data.api.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginAndRegistrationApiService {
    @POST("login.json")
    Call<LoginDetails> login(@Body LoginUserData data);

    @POST("updateProfile.json")
    Call<LoginDetails> updateProfile(@Header("Authorization") String token, @Body UserData userData);
}
