package User;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import ModelClasses.SellerData;
import Utils.SettingMemoryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ModelClasses.UserData;

import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityUserEditProfile extends AppCompatActivity {

    CircleImageView profile_image ;
    boolean IS_VALIDATED = false;
    ImageButton backBtn ;
    Button saveBtn;
    EditText name , email  , address , mobile;
    Button verify;
    SellerData sellerData;
    ImageButton doneBtn;
    PinView pinView;
    DatabaseReference databaseReference , databaseReference2 , databaseReferenceGetUserData;
    List<UserData> userDataList;
    private static final String TAG = "ActivityUserEditProfile";
    List<SellerData> sellerDataList;
    String Email_data;
    UserData userData;
    String Name , Address , Mobile , Email ;
    PhoneAuthCredential credential;
    String code;
    CustomProgressDialog customProgressDialog;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            boolean isEmailVerified = true;
            userDataList = new ArrayList<>();
            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                UserData userData = snapshot1.getValue(UserData.class);
                if (userData != null)
                {
                    if (userData.getEmail() .equals(Email_data))
                    {
                        Log.d(TAG, "onDataChange: "+userData.getEmail() + Email_data);
                        Log.d(TAG, "onDataChange: data matching problem");
                        isEmailVerified = false;
                        Log.d(TAG, "onDataChange: loop breaked");
                        customProgressDialog.dismissDialog();
                        break;
                    }
                }
            }

            if (isEmailVerified && userData != null)
            {
                Log.d("success", "data found");
                databaseReference.removeEventListener(listener);

                if (userData != null) {
                    ChangeData(userData);
                    getSellerData(userData.getUsername());
                }else
                {
                    Log.d(TAG, "onDataChange: user data is getting null");
                }

            }else
                email.setError("please enter valid email id");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {


            sellerData = snapshot.getValue(SellerData.class);
            if (sellerData != null) {
                changeData();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener listenerGettingUserData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            final String userID = new SettingMemoryData(ActivityUserEditProfile.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

            if(userID != null)
                Log.d("userId current user",userID);

            for(DataSnapshot snapshot1 : snapshot.getChildren())
            {
                userData = snapshot1.getValue(UserData.class);
                Log.d("data", Objects.requireNonNull(userData).getUsername());
                if(userID != null && !userID.isEmpty() && userData!= null) {
                    if (userData.getUsername().equals(userID))
                    {
                        databaseReferenceGetUserData.removeEventListener(listenerGettingUserData);

                        Email_data = email.getText().toString().trim();
                        validateEmail();
                        break;
                    }
                    else{
                        Log.d("error","no data found");
                    }
                }else {
                    Log.d("error","something went wrong");
                    customProgressDialog.dismissDialog();
                    new CustomDialogMaker(ActivityUserEditProfile.this).createAndShowDialogSuccess("invalid details" , "user");
                    return;
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void changeData() {

        sellerData.setName(Name);
        sellerData.setEmail(Email);
        sellerData.setAddress(Address);
        sellerData.setNumber("+91"+Mobile);

        Log.d("success","data set ");

        UploadData();
        setCurrentUserInfo(userData);


    }

    private void setCurrentUserInfo(final UserData userData) {

        //EmailAuthCredential credential = (EmailAuthCredential) EmailAuthProvider.getCredential(userData.getEmail() , userData.getPassword());
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.fetchSignInMethodsForEmail(email.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
            @Override
            public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {

                auth.createUserWithEmailAndPassword(userData.getEmail() , userData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Log.d(TAG, "onSuccess: login success");
                        Log.d(TAG, "onSuccess: "+ Objects.requireNonNull(authResult.getAdditionalUserInfo()).getProfile());

                        changeActivity();
                        customProgressDialog.dismissDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (customProgressDialog != null)
                        customProgressDialog.dismissDialog();
                        Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());

                    }
                });
            }
        });



    }

    private void getSellerData(String username) {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference2 = databaseReference2.child(username);
        databaseReference2.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_form);

        findId();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    Log.d("success","form validated");
                    if(doneBtn.getVisibility() == View.VISIBLE && verify.getVisibility() == View.INVISIBLE)
                    {
                        customProgressDialog = new CustomProgressDialog(ActivityUserEditProfile.this);
                        customProgressDialog.startLoadingDailog();
                        getDataFormDataBase();
//                        customProgressDialog.dismissDialog();
                    }else
                    {
                        new CustomDialogMaker(ActivityUserEditProfile.this).createAndShowDialogWarning("please first verify phone number");
                    }
                }else
                {
                    Log.d("error" , "form not validated");
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mobile = mobile.getText().toString().trim();
                if(Mobile.length() < 10)
                {
                    mobile.setError("mobile number should be of 10 digit");
                    mobile.requestFocus();
                }
                else
                {
                    verifyPhoneNumber();
                }
            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mobile.getText().toString().length() < 10)
                {
                    mobile.setError("mobile number should be of 10 digits");
                    mobile.requestFocus();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }

    private void verifyPhoneNumber() {

        customProgressDialog = new CustomProgressDialog(ActivityUserEditProfile.this);
        customProgressDialog.startLoadingDailog();
        getOTP();
        pinView.setText(code);
        customProgressDialog.dismissDialog();

    }

    private void getOTP() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919001851307" , "146752");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919982917736","917736");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918764498357" , "917736");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+917976330044" , "991245");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+918824135146" , "998291" );
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919413609362" , "135790");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+917985025413" , "073527");
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+919694533137" , "867542");

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

                    pinView.setText(credential.getSmsCode());
                    signInWithCredentials(credential);
                    Log.d("progress", "you are here");

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

                "+91"+mobile.getText().toString().trim(),
                20,
                TimeUnit.SECONDS,
                ActivityUserEditProfile.this,
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

                if( Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getAdditionalUserInfo()).isNewUser()) {

                    Toast.makeText(getApplicationContext(),"congratulation you have  a new number",Toast.LENGTH_LONG).show();

                }
                else {
//                    customDialogMaker.createAndShowDialogSuccess("you have successfully logged in",account_type);
//                    Toast.makeText(getApplicationContext() , "welcome back",Toast.LENGTH_LONG).show();
                    verify.setVisibility(View.INVISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    Log.d("success" , "number verified");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage() + " " +e.getClass().getName());
            }
        });
    }

    private void changeActivity() {

        Intent intent = new Intent(ActivityUserEditProfile.this , BottomNavActivity.class);
        intent.putExtra(String.valueOf(R.string.account_type_key), "user");
        finish();
        startActivity(intent);

    }

    private void UploadData() {

        DatabaseReference databaseReference ;

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

        databaseReference.child(userData.getUsername()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext() , "data uploaded successfully" , Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error" , e.getMessage() +" " + e.getClass().getName());
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference.child(userData.getUsername()).setValue(sellerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext() , "data uploaded successfully" , Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error" , e.getMessage() +" " + e.getClass().getName());

            }
        });

    }

    private void ChangeData(UserData userData) {

        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Address = address.getText().toString().trim();
        Mobile = mobile.getText().toString().trim();

        if(!Name.isEmpty() && !Email.isEmpty() && !Address.isEmpty() && !Mobile.isEmpty())
        {
            if(userData != null)
            {
                userData.setName(Name);
                userData.setEmail(Email);
                userData.setAddress(Address);
                userData.setNumber("+91"+Mobile);

                Log.d("success","data set ");
            }
        }else{
            Log.d("error","user data object is null");
        }

    }

    private void getDataFormDataBase() {




        databaseReferenceGetUserData = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReferenceGetUserData .addValueEventListener(listenerGettingUserData);

//        Log.d("userId",userData.getUsername());
    }

    private void  validateEmail() {

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(listener);


    }

    private boolean validateForm() {

        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Address =address.getText().toString().trim();
        Mobile = mobile.getText().toString().trim();

        if( (!Name.isEmpty()) && (!Email.isEmpty()) && (!Address.isEmpty()) && (!Mobile.isEmpty()) )
        {
            if(Mobile.length() < 10)
            {
                IS_VALIDATED = false;
                mobile.setError("please enter 10 digit number");
                mobile.requestFocus();
            }
            else{
                IS_VALIDATED = true;
            }
        }else if(Name.isEmpty())
        {
            IS_VALIDATED = true;
            name.setError("please enter name");
            name.requestFocus();
        }else if(Email.isEmpty())
        {
            IS_VALIDATED = true;
            email.setError("please enter email");
            email.requestFocus();
        }else if(Address.isEmpty())
        {
            IS_VALIDATED = true;
            address.setError("please enter address");
            address.requestFocus();
        }else if(mobile.getText().toString().trim().isEmpty())
        {
            IS_VALIDATED = true;
            mobile.setError("please enter valid number");
            mobile.requestFocus();
        }else {
            IS_VALIDATED = true;
        }

        return  IS_VALIDATED;
    }

    private void findId() {

        profile_image = findViewById(R.id.profile_image);
        backBtn = findViewById(R.id.backBtn_to_dashboard);
        saveBtn = findViewById(R.id.switchAccountBtn);
        name = findViewById(R.id.name_account_holder);
        email = findViewById(R.id.email);
        address =findViewById(R.id.address);
        mobile = findViewById(R.id.number);
        verify = findViewById(R.id.verify_btn);
        doneBtn = findViewById(R.id.done_btn);
        pinView = findViewById(R.id.pin_view);
        customProgressDialog = new CustomProgressDialog(ActivityUserEditProfile.this);

    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onDestroy();
    }
}