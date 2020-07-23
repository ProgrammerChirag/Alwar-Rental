package User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.selflearn.alwarrenter.R;
import Utils.SettingMemoryData;

import java.util.Objects;

import FirebaseConnectivity.UploadData;
import Helper.UserSignUpHelper;
import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class UserSignUpActivity extends AppCompatActivity {

    LinearLayout login_bottom_button;
    LinearLayout forgot_password_button;
    Button SIGN_UP ;
    EditText U_ID , EMAIL , PASSWORD , CONFIRM_PASSWORD , NAME;

    CustomProgressDialog customProgressDialog ;
    CustomDialogMaker customDialogMaker;

    boolean IS_FORM_VALIDATED = false;
    boolean IS_EMAIL_VALIDATED = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_user);

        findID();

        // when someone click on bottom already have an account button

        login_bottom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ActivityNavigation.openUserLoginWithEmialActivity(UserSignUpActivity.this);
            }
        });


        SIGN_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    sendDataToDataBase();
                    loginWithData();
                }
            }
        });

//        forgot_password_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //open up activity to change password.
//                Utils.ActivityNavigation.openForgotPassowrdActivity(UserSignUpActivity.this);
//            }
//        });


        PASSWORD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String pass = PASSWORD.getText().toString().trim();

                if(pass.length()<8){
                    PASSWORD.setError("please enter 8 digit password ");
                }
                else{
                    if(new UserSignUpHelper().isPasswordValid(pass)){
                        PASSWORD.setError("password must have one capital and small letter and one special character and should be of 8 minimum 8 digit");
                    }
                }
            }
        });

        CONFIRM_PASSWORD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = CONFIRM_PASSWORD.getText().toString().trim();

                if(pass.length()<8){
                    CONFIRM_PASSWORD.setError("please enter 8 digit password ");
                }
                else{
                    String pass2 = PASSWORD.getText().toString().trim();

                    if(!pass.equals(pass2)){
                        CONFIRM_PASSWORD.setError("password is not matching");
                        CONFIRM_PASSWORD.requestFocus();
                    }
                }
            }
        });

    }

    private void setSharedPref() {
        SettingMemoryData settingMemoryData = new SettingMemoryData(UserSignUpActivity.this);
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), U_ID.getText().toString());
    }

    private void loginWithData() {

        customProgressDialog.startLoadingDailog();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword( EMAIL.getText().toString().trim() , PASSWORD.getText().toString().trim() ).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() && task.getResult() != null)
                        {

                            new SettingMemoryData(UserSignUpActivity.this).setSharedPrefString(String.valueOf(R.string.account_type_key),"user");
                            if(Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                                setSharedPref();
                                customDialogMaker.createAndShowDialogSuccess("Account Created Success","user");
                                Toast.makeText(getApplicationContext(), "Successfully login", Toast.LENGTH_LONG).show();
                                customProgressDialog.dismissDialog();
                                //changeActivity();
                            }
//                            else {
//
//                            }
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        customProgressDialog.dismissDialog();
                        Log.d("error", Objects.requireNonNull(e.getMessage()));
                        Log.d("error",e.getClass().getName());

                        if(e instanceof FirebaseAuthUserCollisionException){

                            customDialogMaker.createAndShowDialogWarning("Account with this given email already exits");

                        }
                    }
                });


    }

//    private void changeActivity() {
//        // changing activity to dashboard.
//
//        finish();
//        Intent intent = new Intent(UserSignUpActivity.this , UserDashBoardHomeActivity.class);
//        startActivity(intent);
//
//    }


    private boolean isEmailValid(String email){

        if(email.isEmpty())
        {
            return IS_EMAIL_VALIDATED;
        }
        else{
            String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";
            if(email.matches(EMAIL_PATTERN))
                IS_EMAIL_VALIDATED = true;
        }

        return  IS_EMAIL_VALIDATED;
    }

    private void sendDataToDataBase() {

        String USERNAME = U_ID.getText().toString().trim();
        String email = EMAIL.getText().toString().trim();
        String password = PASSWORD.getText().toString().trim();
        String name = NAME.getText().toString();

        UserData userData = new UserData();
        userData.setName(name);
        userData.setEmail(email);
        userData.setNumber_of_purchase("0");
        userData.setNumber_of_requests("0");
        userData.setUsername(USERNAME);
        userData.setPassword(password);
        userData.setAddress("Alwar , Rajasthan");
        userData.setNumber("+91");

        FirebaseConnectivity.UploadData uploadData = new UploadData(UserSignUpActivity.this);

        uploadData.UploadDataOfUser(userData);

        // code for sending data to the user database.
    }

    private boolean validateForm() {
        // validate the form

        String USERNAME = U_ID.getText().toString().trim();
        String email = EMAIL.getText().toString().trim();
        String password = PASSWORD.getText().toString().trim();
        String confirm_password = CONFIRM_PASSWORD.getText().toString().trim();
        String name = NAME.getText().toString();

        if( (!USERNAME.isEmpty()) && (!email.isEmpty()) && (!password.isEmpty()) && (!confirm_password.isEmpty()) && (isEmailValid(email)) ){
            if(password.equals(confirm_password)){
                IS_FORM_VALIDATED = true;
            }
            else{
                CONFIRM_PASSWORD.setError("password not matching");
                CONFIRM_PASSWORD.requestFocus();
            }
        }else{
            if(name.isEmpty()){
                NAME.setError("please enter your name");
                NAME.requestFocus();
            }
            else if(USERNAME.isEmpty()){
                U_ID.setError("please enter valid user ID");
                U_ID.requestFocus();
            }
            else if(email.isEmpty()){
                EMAIL.setError("please enter email");
                EMAIL.requestFocus();
            }
            else if(password.isEmpty()){
                PASSWORD.setError("please enter password");
                PASSWORD.requestFocus();
            }
            else if(confirm_password.isEmpty()){
                CONFIRM_PASSWORD.setError("please enter password here");
                CONFIRM_PASSWORD.requestFocus();
            }
            else{
                IS_FORM_VALIDATED = true;
            }
        }


        return  IS_FORM_VALIDATED;
    }

    private void findID() {
        
        login_bottom_button=findViewById(R.id.login_acc_button);
        U_ID= findViewById(R.id.user_id);
        EMAIL = findViewById(R.id.user_email);
        PASSWORD = findViewById(R.id.user_password);
        CONFIRM_PASSWORD = findViewById(R.id.user_confirm_password);
        SIGN_UP = findViewById(R.id.sign_up_button_seller);
        customProgressDialog = new CustomProgressDialog(UserSignUpActivity.this);
        customDialogMaker = new CustomDialogMaker(UserSignUpActivity.this);
//        forgot_password_button = findViewById(R.id.forgot_password_button);
        NAME=findViewById(R.id.name_account_holder);
    }
}
