package Seller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;

import ModelClasses.SellerData;
import Utils.SettingMemoryData;
import Helper.UserSignUpHelper;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class SellerSignUpActivity extends AppCompatActivity {

    LinearLayout login_account_button;
    LinearLayout forgot_password_button;
    Button SIGN_UP ;
    EditText U_ID , EMAIL , PASSWORD , CONFIRM_PASSWORD , NAME;

    CustomProgressDialog customProgressDialog;
    CustomDialogMaker customDialogMaker;

    boolean IS_FORM_VALIDATED = false;
    SellerData sellerData;
    boolean IS_EMAIL_VALIDATED = false;
    boolean IS_USERNAME_AVAILABLE = true;
    DatabaseReference databaseReference;

    String Name , Email , Password , Username;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "onDataChange: started");
//                SellerData sellerData;
            List<SellerData> sellerDataList = new ArrayList<>();


            for(DataSnapshot snapshot1 : snapshot.getChildren())
            {
                sellerData = snapshot1.getValue(SellerData.class);
//                    sellerDataList.add(sellerData);
                sellerDataList.add(sellerData);
            }

            if(sellerDataList.size() > 0)
            {
                Log.d(TAG, "onDataChange: total data size :"+ sellerDataList.size()) ;
                for( int i =0 ; i<sellerDataList.size() ; i++)
                {
                    if(Username.equals(sellerDataList.get(i).getUsername()))
                    {
                        U_ID.setError("please enter valid username");
                        customProgressDialog.dismissDialog();
//                            customDialogMaker.createAndShowDialogWarning("username already taken");
                        U_ID.requestFocus();
                        Log.d(TAG, "onDataChange: " + sellerDataList.get(i).getUsername());
                        Log.d(TAG, "onDataChange: " + Username);
                        IS_USERNAME_AVAILABLE = false;
                        Log.d(TAG, "onDataChange: user deleted successfully");
                        deleteUser();
                        return;
                    }
                    else IS_USERNAME_AVAILABLE = true;
                }
            }
            if(IS_USERNAME_AVAILABLE){
                Log.d(TAG, "onDataChange: verifying email");
//                    mDatabase.removeEventListener(userNavListener);
                databaseReference.removeEventListener(listener);
                verifyEmailAndPassWord();
            }else
            {
                Log.d(TAG, "onDataChange: username not available");
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getMessage() + " " +error.getClass().getName());
            customProgressDialog.dismissDialog();
        }
    };

    private static final String TAG = "SellerSignUpActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_signup_activity);

        databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");

        sellerData = new SellerData();
        Log.d(TAG, "onCreate: seller sign up activity");

        findID();
        // when someone click on bottom already have an account button

        login_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ActivityNavigation.openSellerLoginWithEmailActivity(SellerSignUpActivity.this);
            }
        });

        forgot_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open up activity to change password.
                Utils.ActivityNavigation.openForgotPassowrdActivity(SellerSignUpActivity.this);
            }
        });

        SIGN_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()) {
                    customProgressDialog.startLoadingDailog();
                    isUserNameAvailable();
                    // removing the error from edit text.
                    EMAIL.setError(null);
                    U_ID.setError(null);

                }else{
                    Log.d(TAG, "setListener: form not validated");
                }

            }
        });

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

    private void verifyEmailAndPassWord() {
        Email = EMAIL.getText().toString().trim();
        Password  = PASSWORD.getText().toString().trim();

        if(Email != null)
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email , Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Log.d(TAG, "onSuccess: email login success");
                    UpdateData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if(e instanceof FirebaseAuthUserCollisionException)
                    {
//                        customDialogMaker.createAndShowDialogWarning("email is already taken");
                        EMAIL.setError("email is already taken");
                        EMAIL.requestFocus();
                        customProgressDialog.dismissDialog();
                        Log.d(TAG, "onFailure: " + e.getMessage() + " " +e.getClass().getName());
                    }
                }
            });
        }
    }

    private boolean validateForm() {
        // validate the form

        Username = U_ID.getText().toString().trim();
        Email = EMAIL.getText().toString().trim();
        Password= PASSWORD.getText().toString().trim();
        String confirm_password = CONFIRM_PASSWORD.getText().toString().trim();
        Name = NAME.getText().toString().trim();

        if( (!Username.isEmpty()) && (!Email.isEmpty()) && (!Password.isEmpty()) &&  (!Name.isEmpty()) &&(!confirm_password.isEmpty()) && (isEmailValid(Email)) ){
            if(Password.equals(confirm_password)){
                IS_FORM_VALIDATED = true;
            }
            else{
                CONFIRM_PASSWORD.setError("password not matching");
                CONFIRM_PASSWORD.requestFocus();
            }
        }else{
            if(Username.isEmpty()){
                U_ID.setError("please enter valid user ID");
                U_ID.requestFocus();
            }
            else if(Email.isEmpty()){
                EMAIL.setError("please enter email");
                EMAIL.requestFocus();
            }
            else if(Password.isEmpty()){
                PASSWORD.setError("please enter password");
                PASSWORD.requestFocus();
            }
            else if(confirm_password.isEmpty()){
                CONFIRM_PASSWORD.setError("please enter password here");
                CONFIRM_PASSWORD.requestFocus();
            }
            else if(Name.isEmpty()){
                NAME.setError("please enter name");
                NAME.requestFocus();
            }else
                IS_FORM_VALIDATED = true;

        }


        return  IS_FORM_VALIDATED;
    }

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

    private void findID() {
        login_account_button=findViewById(R.id.login_acc_button_seller);
        U_ID= findViewById(R.id.user_id);
        EMAIL = findViewById(R.id.seller_email);
        PASSWORD = findViewById(R.id.user_password);
        CONFIRM_PASSWORD = findViewById(R.id.seller_confirm_password);
        SIGN_UP = findViewById(R.id.sign_up_button_seller);
        customProgressDialog = new CustomProgressDialog(SellerSignUpActivity.this);
        customDialogMaker = new CustomDialogMaker(SellerSignUpActivity.this);
        forgot_password_button = findViewById(R.id.forgot_password_button);
        NAME = findViewById(R.id.name_account_holder);
    }

    private void isUserNameAvailable() {

        Log.d(TAG, "isUserNameAvailable: checking for username availability");

//        final List<SellerData> sellerDataList = new ArrayList<>();
        Username = U_ID.getText().toString().trim();


        databaseReference.addValueEventListener(listener);
    }

    private void deleteUser(){

        if (FirebaseAuth.getInstance().getCurrentUser() != null)

        FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: user deleted");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());
            }
        });
    }

    private void UpdateData() {

        sellerData.setName(Name);
        sellerData.setAddress("");
        sellerData.setEmail(Email);
        sellerData.setNumber("");
        sellerData.setPassword(Password);
        sellerData.setUsername(Username);
        sellerData.setNumberOfPost("0");
        sellerData.setNumOfCustomer("0");

        isEmailExist();

    }

    private void isEmailExist() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: confirmation email sent");
                    sendDataToDataBase();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " +e.getMessage() + " " +e.getClass().getName());
                    EMAIL.setError("email not exists");
                    EMAIL.requestFocus();
                    customProgressDialog.dismissDialog();
                    deleteUser();
                }
            });
    }

    private void sendDataToDataBase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference.child(sellerData.getUsername()).setValue(sellerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: all things are set");
                Log.d(TAG, "onSuccess: data uploaded to database ");
                Log.d(TAG, "onSuccess: username: "+sellerData.getUsername());

                saveSharedPreferences();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed");
                Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());
                customProgressDialog.dismissDialog();
            }
        });
    }

    private void saveSharedPreferences() {

        SettingMemoryData settingMemoryData = new SettingMemoryData(SellerSignUpActivity.this);

        settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key), "seller");
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , sellerData.getNumber());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , sellerData.getName());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), sellerData.getUsername());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_EMAIL) , sellerData.getEmail());

        changeActivityToDashBoard();

    }

    private void changeActivityToDashBoard() {

        customProgressDialog.dismissDialog();

        EMAIL.setError(null);
        PASSWORD.setError(null);
        U_ID.setError(null);
        NAME.setError(null);

        new CustomDialogMaker(SellerSignUpActivity.this).createAndShowDialogSuccess("login success" , "seller");

    }
}
