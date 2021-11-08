package com.game.TamilGuru.ui.login;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.game.TamilGuru.R;
import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.LoginApiResponse;
import com.game.TamilGuru.databinding.FragmentLoginScreenBinding;
import com.game.TamilGuru.utils.Common;
import com.game.TamilGuru.utils.Constants;
import com.google.android.datatransport.backend.cct.BuildConfig;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


public  class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    private FragmentLoginScreenBinding fragmentLoginScreenBinding;
    private GoogleSignInClient mClient;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;

    private final ActivityResultLauncher<Intent> googleSignInResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        updateSignInData(task);

    });
    private LoginViewModel mViewModel;
    private ProgressDialog mProgressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //mViewModel.loginIntoAhaGuruServer().observe(this, this::loginResponse);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_screen, container, false);
        fragmentLoginScreenBinding = FragmentLoginScreenBinding.bind(v);
       // GoogleSignInHelper googleSignInHelper = GoogleSignInHelper.getInstance();
        //mClient = googleSignInHelper.getClient(requireContext());
        fragmentLoginScreenBinding = FragmentLoginScreenBinding.inflate(getLayoutInflater(), container, false);
        return fragmentLoginScreenBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder( getActivity())
                .enableAutoManage(getActivity(),0,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        fragmentLoginScreenBinding.BTLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });


    }
    private void loginResponse(Resource<LoginApiResponse> apiStatusResponseResource) {
        dismissProgressBar();
        if(apiStatusResponseResource == null){
            return;
        }
        switch (apiStatusResponseResource.status){
            case SUCCESS:
                LoginApiResponse data = apiStatusResponseResource.data;
                if(Common.isObjectNotNullOrEmpty(data)){
                    if (data.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        Common.putDebugLog("loginResponse:"+"Successfully Logged in");
                        switch (data.getStatus()) {
                            case Constants.PROFILE_TYPE_NOT_YET_LINKED:

                                //registration Activity
                                NavDirections toCreateProfileFragment = LoginFragmentDirections.actionLoginFragmentToCreateProfileFragment();
                                Navigation.findNavController(fragmentLoginScreenBinding.getRoot()).navigate(toCreateProfileFragment);
                                //Common.callRegistrationActivity(requireContext());
                                break;
                        }
                        requireActivity().finish();
                    } else {
                        Common.putErrorLog(data.getMessage());
                    }
                }
                break;

            case ERROR:
                data = apiStatusResponseResource.data;
                if (Common.isObjectNotNullOrEmpty(data)) {
                    if (data.getStatus() == Constants.STATUS_CODE_CONNECTIVITY_ISSUE) {
                        Common.showToast(requireContext(), getResources().getString(R.string.connection_error_msg));
                    } else if(data.getStatus() == Constants.STATUS_CODE_TIMEOUT){
                        Common.showToast(requireContext(), data.getMessage());
                    } else if(data.getStatus() == Constants.STATUS_CODE_RESPONSE_NOT_FOUND){
                      //  Common.callRegistrationActivity(requireContext());
                        requireActivity().finish();
                    } else {
                        if(Common.isObjectNotNullOrEmpty(data.getMessage())) {
                            Common.showToast(requireContext(), data.getMessage());
                            Common.putErrorLog(data.getMessage());
                        } else {
                            Common.showToast(requireContext(), getResources().getString(R.string.api_failure_msg));
                        }

                    }
                }
                break;
        }

    }

    private void updateSignInData(@NotNull Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount result = task.getResult(ApiException.class);
            mViewModel.setGoogleSignInAccountData(result);

            try {
                JSONObject props = new JSONObject();
                props.put("email", result.getEmail());
                props.put("name", result.getGivenName());
                props.put("version", BuildConfig.VERSION_CODE);
                Common.trackInMixpanel(getActivity(), props, "signin");
            } catch (JSONException jsonException) {
                Common.putDebugLog("Error adding properties for mixpanel");
            }
            showProgressDialog(getString(R.string.singing_in));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
    private void launchGoogleLogin(View v) {
        if (Common.isInternetConnected(requireActivity().getApplicationContext())) {
           // Intent signInIntent = mClient.getSignInIntent();
           // googleSignInResult.launch(signInIntent);
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,RC_SIGN_IN);
        } else {
            Common.showToast(requireActivity().getApplicationContext(), getResources().getString(R.string.connection_error_msg));
        }
    }
    private void dismissProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void showProgressDialog(String content) {
        mProgressDialog = new ProgressDialog(requireContext());
        mProgressDialog.setMessage(content);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getActivity(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        NavDirections toCreateProfileFragment = LoginFragmentDirections.actionLoginFragmentToCreateProfileFragment();
        Navigation.findNavController(fragmentLoginScreenBinding.getRoot()).navigate(toCreateProfileFragment);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}