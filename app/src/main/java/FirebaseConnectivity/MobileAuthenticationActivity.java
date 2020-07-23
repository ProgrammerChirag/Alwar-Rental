package FirebaseConnectivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.selflearn.alwarrenter.AddListenerOnTextChange;
import com.selflearn.alwarrenter.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;


public class MobileAuthenticationActivity extends AppCompatActivity {

    PinView pinView_for_otp;
    Button  get_OTP;
    EditText phone_number;
    PhoneAuthCredential credential;
    CustomDialogMaker customDialogMaker;
    CustomProgressDialog customProgressDialog ;
    String account_type ;
    String code ;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
            firebaseAuth.signOut();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile_authentication);

        findID();

        account_type = getIntent().getStringExtra("account_type");

        customProgressDialog = new CustomProgressDialog(MobileAuthenticationActivity.this);

        phone_number.addTextChangedListener(new AddListenerOnTextChange(this, phone_number));
//
//        login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        get_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone_number.getText().toString().length() < 10){
                    phone_number.setError("please enter 10 digit number");
                    phone_number.requestFocus();
                }
                else{
                    getOTP();
                    pinView_for_otp.setText(code);
                    customProgressDialog.startLoadingDailog();
//                    signInWithCredentials(credential);
                }
            }
        });
    }

    private void getOTP() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919982917736","917736");

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String sms = phoneAuthCredential.getSmsCode();

                assert sms != null;
                Log.d("code", String.valueOf(phoneAuthCredential));

                credential = phoneAuthCredential;

                code = credential.getSmsCode();

                if(phoneAuthCredential.getSmsCode() == null){
                    customProgressDialog.startLoadingDailog();
                    signInWithCredentials(phoneAuthCredential);
                }
                else {
                    pinView_for_otp.setText(credential.getSmsCode());
                    signInWithCredentials(credential);
                }

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Log.d("code sent ", "code sent"+s);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("error", Objects.requireNonNull(e.getMessage()));
            }
        };


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone_number.getText().toString().trim(),
                20,
                TimeUnit.SECONDS,
                MobileAuthenticationActivity.this,
                mcallbacks
        );
    }

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult() != null)
                    customProgressDialog.dismissDialog();
                sendDataToDataBase(phone_number);
               if( Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getAdditionalUserInfo()).isNewUser()) {

                   Intent intent = new Intent(MobileAuthenticationActivity.this , ActivityAfterMobileAuth.class);
                   finish();
                   intent.putExtra(String.valueOf(R.string.account_type_key), account_type);
                   startActivity(intent);
                   Toast.makeText(getApplicationContext(),"congratulation you are a new user",Toast.LENGTH_LONG).show();
               }
               else {
                   if(new SettingMemoryData(MobileAuthenticationActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_NAME))==null)
                   {
                       Intent intent = new Intent(MobileAuthenticationActivity.this , ActivityAfterMobileAuth.class);
                       finish();
                       intent.putExtra(String.valueOf(R.string.account_type_key), account_type);
                       startActivity(intent);
                       Toast.makeText(getApplicationContext(),"please fill form to complete sign up process",Toast.LENGTH_LONG).show();
                   }else
                   {
                       customDialogMaker.createAndShowDialogSuccess("you have successfully logged in",account_type);
                       Toast.makeText(getApplicationContext() , "welcome back",Toast.LENGTH_LONG).show();
                   }
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendDataToDataBase(EditText phone_number) {
        String phone = phone_number.getText().toString().trim();

        UserData userData = new UserData();
        userData.setNumber("+91"+phone);
        String user = getAlphaNumericString(10);
        userData.setUsername(user);

        new SettingMemoryData(MobileAuthenticationActivity.this).setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), user);

        new UploadData(MobileAuthenticationActivity.this).UploadDataOfUser(userData);

    }

    private void findID() {
        pinView_for_otp= findViewById(R.id.pin_view);
        get_OTP = findViewById(R.id.get_otp_button);
//        login_button= findViewById(R.id.login_button);
        phone_number = findViewById(R.id.phone_number);
        customDialogMaker = new CustomDialogMaker(MobileAuthenticationActivity.this);
    }

    public  String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
