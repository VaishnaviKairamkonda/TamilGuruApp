package com.game.TamilGuru.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleSignInHelper {


    private static GoogleSignInHelper INSTANCE;
    private GoogleSignInClient client;


    public GoogleSignInHelper(){

    }

    public static synchronized GoogleSignInHelper getInstance() {

        if(INSTANCE == null){
            INSTANCE = new GoogleSignInHelper();
        }
        return INSTANCE;
    }


    public void initGoogleSignIn(Context context){
       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

//          String webClientId =context.getString(R.string.default_web_client_id);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(webClientId)
//                .build();

        client = GoogleSignIn.getClient(context, gso);



    }

    public GoogleSignInClient getClient(Context context) {

        if(client == null){
            initGoogleSignIn(context);
        }
        return client;
    }
}
