package Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.ChooseLoginTypeActivity;
import com.selflearn.alwarrenter.R;

import ModelClasses.SellerData;
import Utils.SettingMemoryData;


import java.util.Objects;

import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class SellerLoginActivity extends AppCompatActivity {

    Button login_button ;
    LinearLayout create_acc_button;
    LinearLayout forgotten_password_button;
    EditText email , password;
    boolean IS_VALIDATED=false;
    boolean IS_VALID_EMAIL = false;
    CustomDialogMaker customDialogMaker ;
    CustomProgressDialog customProgressDialog;
    private static final String TAG = "SellerLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_login_activity);

        Log.d(TAG, "onCreate: seller login with email activity");


        findID();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomProgressDialog customProgressDialog = new CustomProgressDialog(SellerLoginActivity.this);

                if(validateForm()){
                    customProgressDialog.startLoadingDailog();
                    loginWithEmailAndPassword();
                    }
            }
        });

        create_acc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ActivityNavigation.openSellerSignUpActivity(SellerLoginActivity.this);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (password.getText().toString().length() < 10)
//                {
//                    password.setError("password should be atleast 8 digit");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = password.getText().toString().trim();

                if(pass.length()<8){
                    password.setError("password must be at least 8 digit");
                    password.requestFocus();
                }
            }
        });
    }


    private void loginWithEmailAndPassword() {

        String EMAIL = email.getText().toString().trim();
        String PASSWORD = password.getText().toString().trim();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(EMAIL , PASSWORD).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() && task.getResult() != null)
                        {
//                            customProgressDialog.dismissDialog();
//                            customDialogMaker.createAndShowDialogSuccess("Happy login","seller");
//
//                            new SettingMemoryData(SellerLoginActivity.this).setSharedPrefString(String.valueOf(R.string.account_type_key),"seller");

                            if(Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()){
                                Toast.makeText(getApplicationContext(), "congratulation you are logged in",Toast.LENGTH_LONG).show();
                            }
                            else if(!task.getResult().getAdditionalUserInfo().isNewUser()){
                                Toast.makeText(getApplicationContext() , "welcome back" , Toast.LENGTH_LONG).show();
                                retrieveData();
                            }
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismissDialog();

                        Log.d("error", e.getClass().getName());
                        if(e instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            customDialogMaker.createAndShowDialogWarning("invalid email or password");
                        }
                    }
                });
    }

    private void retrieveData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SellerData sellerData;
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    sellerData = snapshot1.getValue(SellerData.class);
                    if(sellerData != null)
                    {
                        if (email.getText().toString().trim().equals(sellerData.getEmail()))
                        {
                            Log.d(TAG, "onDataChange: email found");
                            setMemoryData(sellerData);
                            return;
                        }
                    }else {
                        Log.d(TAG, "onDataChange: seller data is null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customProgressDialog.dismissDialog();
                Log.d(TAG, "onCancelled: " + error.getMessage() + "" + error.getClass().getName());
            }
        });
    }

    private void setMemoryData(SellerData sellerData) {

        SettingMemoryData settingMemoryData = new SettingMemoryData(SellerLoginActivity.this);

        settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key), "seller");
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , sellerData.getNumber());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , sellerData.getName());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), sellerData.getUsername());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_EMAIL) , sellerData.getEmail());

        changeActivityToDashBoard();
    }

    private void changeActivityToDashBoard() {

        customProgressDialog.dismissDialog();
        new CustomDialogMaker(SellerLoginActivity.this).createAndShowDialogSuccess("login success" , "seller");
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(SellerLoginActivity.this, ChooseLoginTypeActivity.class));

    }

//    private void sendDataToDataBase() {
//        // sending the data into database
//    }

    private boolean validateForm() {

        String EMAIL = email.getText().toString().trim();
        String PASSWORD = password.getText().toString().trim();

        if( (!EMAIL.isEmpty()) && (!PASSWORD.isEmpty()) && (isValidEmail(EMAIL)))
        {
            IS_VALIDATED = true;
        }
        else if(PASSWORD.isEmpty()){
            password.setError("please enter password");
            password.requestFocus();
        }else if(EMAIL.isEmpty()){
            email.setError("please enter valid email");
            email.requestFocus();
        }
        else{
            IS_VALIDATED = true;
        }

        return IS_VALIDATED;
    }

    private boolean isValidEmail(String email)
    {
        String regex = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

        if(email.trim().matches(regex))
            IS_VALID_EMAIL = true;

        return  IS_VALID_EMAIL;
    }

    private void findID()
    {
        create_acc_button=findViewById(R.id.create_acc_button);
        login_button = findViewById(R.id.login_button_seller);
        email = findViewById(R.id.user_id);
        password = findViewById(R.id.user_password);
        forgotten_password_button = findViewById(R.id.forgot_password_button1);
        customDialogMaker = new CustomDialogMaker(SellerLoginActivity.this);
        customProgressDialog = new CustomProgressDialog(SellerLoginActivity.this);


    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        super.onDestroy();
    }
}