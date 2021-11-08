package com.game.TamilGuru.utils.textWatchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import com.game.TamilGuru.R;
import com.game.TamilGuru.utils.Common;
import com.google.android.material.textfield.TextInputLayout;

public class EmptyCheckTextWatcher implements TextWatcher {

    private final EditText textInputLayout;

    public EmptyCheckTextWatcher(EditText textInputEditText) {
        this.textInputLayout = textInputEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //textInputLayout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        if(Common.isGivenStringNullOrEmpty(text)){
            //textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(textInputLayout.getContext().getString(R.string.field_cant_be_empty));
        }
        else{
           // textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        }
    }
}
