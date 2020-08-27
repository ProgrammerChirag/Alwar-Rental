package Seller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.ChooseLoginTypeActivity;
import com.selflearn.alwarrenter.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class SellerLoginMobileAuth extends AppCompatActivity {

    EditText phoneNumber ;
    Button getOTPBtn;
    PinView pinView;
    String account_type;

    private static final String TAG = "SellerLoginMobileAuth";

//    PhoneAuthCredential credential;
//    String code;

    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile_authentication);

        customProgressDialog = new CustomProgressDialog(SellerLoginMobileAuth.this);

//        account_type = getIntent().getStringExtra(String.valueOf(R.string.account_type_key));
        findId();
        setListener();

    }

    private void setListener() {

        getOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when get otp button is clicked

                if(phoneNumber.getText().toString().length() < 10)
                {
                    phoneNumber.setError("please enter 10 digit phone number");
                    phoneNumber.requestFocus();
                }else {
                    // phone number is filled now get otp and then login .
                    customProgressDialog.startLoadingDailog();
                    getOTP();
                }
            }
        });
    }

    private void getOTP() {

        Log.d(TAG, "getOTP: getting otp");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919001851307" , "146752");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919982917736","917736");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918764498357" , "917736");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+917976330044" , "991245");
        //firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918824135146" , "998291" );
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919413609362" , "135790");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+917985025413" , "073527");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919694533137" , "867542");

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted: verification completed");

                String sms = phoneAuthCredential.getSmsCode();

                assert sms != null;
                Log.d("code", String.valueOf(phoneAuthCredential));
//
//                credential = phoneAuthCredential;
//
//                code = credential.getSmsCode();

                if(phoneAuthCredential.getSmsCode() == null){

//                    customProgressDialog.startLoadingDailog();
                    signInWithCredentials(phoneAuthCredential);
                }
                else {

                    pinView.setText(phoneAuthCredential.getSmsCode());
                    signInWithCredentials(phoneAuthCredential);

                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Log.d("code sent ", "code sent"+s);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {

                Log.d(TAG, "onCodeAutoRetrievalTimeOut: "+s);

                customProgressDialog.dismissDialog();
//                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("error", Objects.requireNonNull(e.getMessage()));
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                "+91"+phoneNumber.getText().toString().trim(),
                5,
                TimeUnit.SECONDS,
                SellerLoginMobileAuth.this,
                mcallbacks
        );

    }

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser())
                {
                    // change activity to form UI
                    Toast.makeText(getApplicationContext() , "congratulation" , Toast.LENGTH_LONG).show();
                    ChangeActivity();

                }else
                {
                    // change activity to dashboard UI.
                    Toast.makeText(getApplicationContext() , "welcome Back" , Toast.LENGTH_LONG).show();
//                  Utils.ActivityNavigation.startSellerDashBoard(SellerLoginMobileAuth.this , "seller");
                    retrieveData();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                customProgressDialog.dismissDialog();
                new CustomDialogMaker(SellerLoginMobileAuth.this).createAndShowDialogWarning("something went wrong please try again");
                Log.d(TAG, "onFailure: " + e.getMessage() + " " + e.getClass().getName());
            }
        });
    }

    private void retrieveData() {

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData ;
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    userData = snapshot1.getValue(UserData.class);
                    if(userData != null)
                    if(phoneNumber.getText().toString().trim().equals(userData.getNumber())){
                        setMemoryData(userData);
                        return;
                    }
                }
                ChangeActivity();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                customProgressDialog.dismissDialog();
                Log.d(TAG, "onCancelled: "+error.getMessage() + " " + error.getClass().getName());
            }
        });
    }

    private void setMemoryData(UserData userData) {

        if (userData != null)
        {
           setSharedPref(userData);
        }else{
            Log.d(TAG, "setMemoryData: data is null");
        }
    }

    private void setSharedPref(UserData userData) {

        SettingMemoryData settingMemoryData = new SettingMemoryData(SellerLoginMobileAuth.this);

//        settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key), "seller");

        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , userData.getNumber());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , userData.getName());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), userData.getUsername());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_EMAIL) , userData.getEmail());

        changeActivityToDashBoard();

    }

    private void changeActivityToDashBoard() {

        customProgressDialog.dismissDialog();

//        new CustomDialogMaker(SellerLoginMobileAuth.this).createAndShowDialogSuccess("login success" , "user" );
        finish();
        startActivity(new Intent(SellerLoginMobileAuth.this , ChooseLoginTypeActivity.class));

    }

    private void ChangeActivity() {

        customProgressDialog.dismissDialog();
        customProgressDialog =null;

        Intent intent = new Intent(SellerLoginMobileAuth.this , ActivityAfterMobileAuth.class);
        intent.putExtra(String.valueOf(R.string.KEY_PHONE_NUMBER) , phoneNumber.getText().toString().trim());

        finish();
        startActivity(intent);
    }

    private void findId() {
        phoneNumber = findViewById(R.id.phone_number);
        getOTPBtn = findViewById(R.id.get_otp_button);
        pinView = findViewById(R.id.pin_view);
    }
}
