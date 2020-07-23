package com.selflearn.alwarrenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class AddListenerOnTextChange implements TextWatcher {

    private  Context context ;
    private  EditText editText;

    public AddListenerOnTextChange(Context context1, EditText editText1){
        super();
        context = context1;
        editText = editText1;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(editText.getText().toString().length() < 10){
            editText.setError("please enter 10 digit phone number");
            editText.setFocusable(true);
        }
    }
}
