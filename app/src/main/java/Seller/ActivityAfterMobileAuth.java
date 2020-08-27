package Seller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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

import Helper.UserSignUpHelper;
import ModelClasses.SellerData;
import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ActivityAfterMobileAuth extends AppCompatActivity {

    EditText name, email, password, username;
    String phone;
    Button signUp;
    String Name , Email , Password , Username;
    SellerData sellerData;

    boolean IS_VALIDATED = false;
    boolean IS_VALID_EMAIL =false;
    boolean IS_USERNAME_AVAILABLE = true;

    private static final String TAG = "ActivityAfterMobileAuth";
    CustomDialogMaker customDialogMaker;
    CustomProgressDialog customProgressDialog;


    DatabaseReference databaseReference ;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            final List<UserData> sellerDataList = new ArrayList<>();

            for(DataSnapshot snapshot1 : snapshot.getChildren())
            {
                userData = snapshot1.getValue(UserData.class);
                sellerDataList.add(userData);
            }
            if(sellerDataList.size() > 0)
            {
                for(int i =0 ; i<sellerDataList.size() ; i++)
                {
                    if(Username.equals(sellerDataList.get(i).getUsername()))
                    {
                        username.setError("please enter valid username");
                        customProgressDialog.dismissDialog();
//                            customDialogMaker.createAndShowDialogWarning("username already taken");
                        username.requestFocus();
                        IS_USERNAME_AVAILABLE = false;
                        deleteUser();
                        return;
                    }else
                        IS_USERNAME_AVAILABLE =true;
                }
            }
            if (IS_USERNAME_AVAILABLE)
            {
                Log.d(TAG, "onDataChange: veryfying email");
                databaseReference.removeEventListener(listener);
                verifyEmailAndPassWord();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getMessage() + " " +error.getClass().getName());
            customProgressDialog.dismissDialog();
        }
    };
    private UserData userData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_account_after_mobileauth);

        databaseReference  = FirebaseDatabase.getInstance().getReference("SellerData");

        phone = getIntent().getStringExtra(String.valueOf(R.string.KEY_PHONE_NUMBER));
        sellerData = new SellerData();
        userData = new UserData();
        customDialogMaker = new CustomDialogMaker(ActivityAfterMobileAuth.this);
        customProgressDialog = new CustomProgressDialog(ActivityAfterMobileAuth.this);
//        sellerData = new SellerData();
        userData = new UserData();
        findID();
        setListener();

    }

    private void setListener() {

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateForm()) {
                        customProgressDialog.startLoadingDailog();
                        isUserNameAvailable();
                        // removing the error from edit text.
                        email.setError(null);
                        username.setError(null);

                    } else {
                        Log.d(TAG, "setListener: form not validated");
                    }
                }catch (Exception e)
                {
                    Log.d(TAG, "onClick: "+e.getMessage() + e.getClass().getName()) ;
                }
            }
        });

    }

    private void verifyEmailAndPassWord() {

        Email = email.getText().toString().trim();
        Password  = password.getText().toString().trim();
        
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
                        email.setError("email is already taken");
                        email.requestFocus();
                        customProgressDialog.dismissDialog();
                        deleteUser();
                    }
                }
            });
        }
    }

    private void isUserNameAvailable() {

//        Username = username.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(listener);
    }

    private void UpdateData() {

        sellerData.setName(Name);
        sellerData.setAddress("");
        sellerData.setEmail(Email);
        sellerData.setNumber(phone);
        sellerData.setPassword(Password);
        sellerData.setUsername(Username);
        sellerData.setNumberOfPost("0");
        sellerData.setNumOfCustomer("0");

        userData.setName(Name);
        userData.setAddress("");
        userData.setEmail(Email);
        userData.setNumber(phone);
        userData.setPassword(Password);
        userData.setUsername(Username);
        userData.setNumber_of_requests("0");
        userData.setNumber_of_purchase("0");

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
                sendDataToDataBaseSeller();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                email.setError("email not exists");
                email.requestFocus();
                customProgressDialog.dismissDialog();

              deleteUser();
            }
        });
    }

    private void deleteUser() {

        AuthCredential credential = EmailAuthProvider.getCredential(Email , String.valueOf(password));
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());
            }
        });
    }

    private void sendDataToDataBaseSeller() {

        Toast.makeText(getApplicationContext() , "sending data to database" ,Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference.child(sellerData.getUsername()).setValue(sellerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: all things are set");
                Log.d(TAG, "onSuccess: data uploaded to database ");
                Log.d(TAG, "onSuccess: username: "+sellerData.getUsername());

//                saveSharedPreferences();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("UserData");
                databaseReference1.child(userData.getUsername()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

//                        Log.d(TAG, "onSuccess: all things are set");
//                        Log.d(TAG, "onSuccess: data uploaded to database ");
//                        Log.d(TAG, "onSuccess: username: "+sellerData.getUsername());

                        Toast.makeText(getApplicationContext() , "data sent to database" , Toast.LENGTH_SHORT).show();
               saveSharedPreferences();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: failed");
                        Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());
                        if (customProgressDialog != null)
                        customProgressDialog.dismissDialog();
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed");
                Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());
                if (customProgressDialog != null)
                customProgressDialog.dismissDialog();
            }
        });
    }

    private void saveSharedPreferences() {

        SettingMemoryData settingMemoryData = new SettingMemoryData(ActivityAfterMobileAuth.this);

//        settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key), "user");
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , sellerData.getNumber());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , sellerData.getName());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), sellerData.getUsername());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_EMAIL) , sellerData.getEmail());

        changeActivityToDashBoard();

    }

    private void changeActivityToDashBoard() {

        customProgressDialog.dismissDialog();
//        new CustomDialogMaker(ActivityAfterMobileAuth.this).createAndShowDialogSuccess("login success" , "user");

        Utils.ActivityNavigation.startChooseAccountTypeActivity( ActivityAfterMobileAuth.this );
        
    }

    private boolean validateForm() {

        Name = name.getText().toString().trim();
        Username = username.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if((!Name.isEmpty()) && (!Username.isEmpty()) && (!Password.isEmpty()) && (!Email.isEmpty())&& isValidEmail(Email) && new UserSignUpHelper().isPasswordValid(Password) ) {
            Log.d("progress","all condition done");
            IS_VALIDATED = true;
        }else if(Name.isEmpty())
        {
            IS_VALIDATED = false;
            name.setError("please enter name");
            name.requestFocus();

        }else if(Email.isEmpty())
        {
            IS_VALIDATED = false;
            email.setError("please enter Email");
            email.requestFocus();
        }else if(Password.isEmpty())
        {
            IS_VALIDATED = false;
            password.setError("please enter password");
            password.requestFocus();
        }else if(username.getText().toString().isEmpty()){
            IS_VALIDATED = false;
            username.setError("please enter valid username");
            username.requestFocus();
        }else IS_VALIDATED = true;

        return  IS_VALIDATED;

    }

    private void findID() {

        name = findViewById(R.id.User_Name);
        email = findViewById(R.id.Email_edit_text);
        password = findViewById(R.id.password_edit_text);
        username = findViewById(R.id.enter_user_id);
        signUp = findViewById(R.id.login_button1);

    }

    private boolean isValidEmail(String email) {
        String regex = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

        if(email.trim().matches(regex))
            IS_VALID_EMAIL = true;

        return  IS_VALID_EMAIL;
    }
}
