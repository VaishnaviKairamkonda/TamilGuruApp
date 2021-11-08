package com.game.TamilGuru.utils.textWatchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import com.game.TamilGuru.R;
import com.game.TamilGuru.utils.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EmptyCheckTextWatcherWithTextFilter implements TextWatcher {

    //private final TextInputLayout textInputLayout;
    private final EditText textInputEditText;
    private final String allowedTextRegex;

    public EmptyCheckTextWatcherWithTextFilter(EditText textInputEditText, String allowedTextRegex) {

        this.textInputEditText = textInputEditText;
        this.allowedTextRegex  = allowedTextRegex;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       // textInputEditText.setErrorEnabled(false);
        Common.editBoxTextFilter(s,textInputEditText,allowedTextRegex);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        if(Common.isGivenStringNullOrEmpty(text)){
           // textInputLayout.setErrorEnabled(true);
            textInputEditText.setError(textInputEditText.getContext().getString(R.string.field_cant_be_empty));
        }
        else{
            //textInputLayout.setErrorEnabled(false);
            textInputEditText.setError(null);
        }
    }
}
