package com.game.TamilGuru.ui.registration;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.game.TamilGuru.R;
import com.game.TamilGuru.data.api.errorHandling.ApiStatusResponse;
import com.game.TamilGuru.data.api.errorHandling.Resource;
import com.game.TamilGuru.data.api.model.LoginUserData;
import com.game.TamilGuru.databinding.FragmentCreateProfileBinding;


import com.game.TamilGuru.utils.Common;
import com.game.TamilGuru.utils.Constants;
import com.game.TamilGuru.utils.SharedPrefHelper;
import com.game.TamilGuru.utils.textWatchers.EmptyCheckTextWatcher;
import com.game.TamilGuru.utils.textWatchers.EmptyCheckTextWatcherWithTextFilter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class CreateProfile extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private CreateViewModel mViewModel;
    private FragmentCreateProfileBinding mbinding;
    private SharedPrefHelper mSharedPref;
    private Bitmap photo;
   // private ProgressDialog mProgressDialog;
   private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    public CreateProfile newInstance(){
        return new CreateProfile();
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPref = SharedPrefHelper.getInstance(requireContext().getApplicationContext());
      //  mViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
      //  mViewModel.registerProfile().observe(this, this::registerProfileApiResponse);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_profile, container, false);
        mbinding = FragmentCreateProfileBinding.bind(v);
            return mbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachTextWatchers();


        mbinding.ETClass.setOnClickListener(this::showAvailableClassesAlertDialog);
     //   mbinding.tilSelectedSubjects.setEndIconOnClickListener(this::showAvailableClassesAlertDialog);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),1,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mbinding.BTLogin.setOnClickListener(this::launchRewardPageFragment);
        mbinding.SelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePicture();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            mbinding.ETName.setText(account.getDisplayName());
          //  mbinding.ETClass.setText(account.getEmail());
            //userId.setText(account.getId());
            try{
                Glide.with(getActivity()).load(account.getPhotoUrl()).into(mbinding.profilePic);
            }catch (NullPointerException e){
                Toast.makeText(getActivity(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoMainActivity();
        }
    }
    private void gotoMainActivity(){
        Toast.makeText(getActivity(),"Signin cancel",Toast.LENGTH_LONG).show();


    }
    private void attachTextWatchers() {
        mbinding.ETName.addTextChangedListener(new EmptyCheckTextWatcherWithTextFilter(mbinding.ETName, Constants.REGEX_WITH_TEXT_SPACE_DOT));
        mbinding.ETClass.addTextChangedListener(new EmptyCheckTextWatcher(mbinding.ETClass));

    }
    private void registerProfileApiResponse(Resource<ApiStatusResponse> apiStatusResponseResource) {
        //dismissProgressBar();
        if (apiStatusResponseResource == null) {
            return;
        }

        switch (apiStatusResponseResource.status) {
            case SUCCESS:
                ApiStatusResponse data = apiStatusResponseResource.data;
                if (Common.isObjectNotNullOrEmpty(data)) {
                    if (data.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        Common.putDebugLog("registerProfileApiResponse:"+data.getMessage());
                        mSharedPref.setProfileType(Constants.PROFILE_TYPE_NOT_YET_LINKED);
                        NavDirections toCreateProfileFragment = CreateProfileDirections.actionCreateProfileFragmentToRewardPageFragment();

                        Navigation.findNavController(mbinding.getRoot()).navigate(toCreateProfileFragment);
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
                    } else if (data.getStatus() == Constants.STATUS_CODE_TIMEOUT) {
                        Common.showToast(requireContext(), data.getMessage());
                    } else if(data.getStatus() == Constants.STATUS_CODE_TOKEN_MISMATCH){
                        Common.showToast(requireContext(), getResources().getString(R.string.login_session_expired));
                        Common.tokenMismatchHandle(requireActivity());
                    }
                    else {
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
    private void selectProfilePicture(){
        final CharSequence[] items={"Camera","Gallery","Cancel"};

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera"))
                {

                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,Constants.REQUEST_CAMERA);
                }
                else if(items[which].equals("Gallery"))
                {
                    Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select File"),Constants.SELECT_FILE);
                }
                else if(items[which].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }

        });
        builder.show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==Constants.MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
            {
                photo = (Bitmap) data.getExtras().get("data");
                mbinding.profilePic.setImageBitmap(photo);
            }
            else if(requestCode == Constants.SELECT_FILE)
            {
                Uri SelectedImageUri=data.getData();
                mbinding.profilePic.setImageURI(SelectedImageUri);
            }
        }
    }

    private void launchRewardPageFragment(View v) {
        boolean isAllFieldsValid = checkAllRegistrationField();
        Common.hideKeyboard(mbinding.getRoot(), requireContext());
        if (isAllFieldsValid) {
            //showProgressDialog(getString(R.string.please_wait));
            LoginUserData loginUserData = generateLoginData();
           // ProfileData profileData = new ProfileData(Constants.DEFAULT_LEAD_ID,Constants.PROFILE_TYPE_STUDENT, null, student);
            //mViewModel.setProfileDataMutableLiveData(profileData);

            // RewardPageFragment rewardPageFragment=new RewardPageFragment();
           // rewardPageFragment.show(requireActivity().getSupportFragmentManager(), "My Fragment");
            NavDirections toCreateProfileFragment = CreateProfileDirections.actionCreateProfileFragmentToRewardPageFragment();

            Navigation.findNavController(mbinding.getRoot()).navigate(toCreateProfileFragment);

        }
    }
    private LoginUserData generateLoginData() {
        return new LoginUserData(getUserName());
    }

  /*  private void dismissProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog(String content) {
        mProgressDialog = new ProgressDialog(requireContext());
        mProgressDialog.setMessage(content);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }*/
    private boolean checkAllRegistrationField() {

        boolean result = true;

        if (Common.isGivenStringNullOrEmpty(getUserName())) {
            //mbinding.ETName.setErrorEnabled(true);
            mbinding.ETName.setError(getString(R.string.field_cant_be_empty));
            result = false;
        }

        if (Common.isGivenStringNullOrEmpty(getUserClass())) {
            //mbinding.ETClass.setErrorEnabled(true);
            mbinding.ETClass.setError(getString(R.string.field_cant_be_empty));
            result = false;
        }

        return result;
    }
    private String getUserName() {

        Editable text = mbinding.ETName.getText();
        if (Common.isObjectNotNullOrEmpty(text)) {
            return text.toString().trim();
        } else {
            return "";
        }
    }
    private String getUserClass() {
        Editable text = mbinding.ETClass.getText();
        if (Common.isObjectNotNullOrEmpty(text)) {
            return text.toString().trim();
        } else {
            return "";
        }

    }
    public void showAvailableClassesAlertDialog(View view) {

        Common.hideKeyboard(mbinding.getRoot(), requireContext());

        String[] availableSubjects = {"LKG","UKG","1","2","3","4","5","6"};
        List<String> selectedSubjects = Arrays.asList(getUserClass().split(Constants.COMMA_WITH_SPACE_DELIMITER));
        List<String> selectedSubjectsList = new ArrayList<>();
        boolean[] checkedItems = getCheckedItems(availableSubjects, selectedSubjects, selectedSubjectsList);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.SubjectListDialog);
        builder.setTitle(R.string.select_class);
        builder.setCancelable(true);

        builder.setMultiChoiceItems(availableSubjects, checkedItems, (DialogInterface.OnMultiChoiceClickListener) (dialogInterface, i, b) -> {
            if (b) {
                selectedSubjectsList.set(i, availableSubjects[i]);
            } else {
                selectedSubjectsList.set(i, "");
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedSubjectsList.removeAll(Arrays.asList("",null));
                String selectedSubjectsStr = TextUtils.join(Constants.COMMA_WITH_SPACE_DELIMITER, selectedSubjectsList);
                mbinding.ETClass.setText(selectedSubjectsStr);
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());

        builder.setNeutralButton(R.string.clear_all, (dialogInterface, i) -> {
            for (int j = 0; j < availableSubjects.length; j++) {
                checkedItems[j] = false;
                selectedSubjectsList.set(j,"");
            }
            mbinding.ETClass.setText("");
        });

        builder.show();

    }

    private boolean[] getCheckedItems(String[] availableSubjects, List<String> selectedSubjects, List<String> selectedSubjectsList) {
        boolean[] checkedItems = new boolean[availableSubjects.length];
        for(int i = 0; i < availableSubjects.length; i++) {
            checkedItems[i] = selectedSubjects.contains(availableSubjects[i]);
            if(checkedItems[i]) {
                selectedSubjectsList.add(availableSubjects[i]);
            }
            else {
                selectedSubjectsList.add("");
            }
        }
        return checkedItems;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}