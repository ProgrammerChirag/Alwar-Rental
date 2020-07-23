package FirebaseConnectivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import ModelClasses.UserData;
import Utils.SettingMemoryData;

import java.util.Objects;

import Helper.UserSignUpHelper;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class ActivityAfterMobileAuth extends AppCompatActivity {

    private static boolean IS_VALID_EMAIL = false;
    EditText name , email , password , username;
    String Name , Email , Password , Username;

    String account_type;
    UserData userData;
    CustomDialogMaker customDialogMaker;
    CustomProgressDialog customProgressDialog;

    Button sign_up_btn;
    Boolean IS_VALIDATED = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        customProgressDialog.dismissDialog();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_account_after_mobileauth);

//        account_type = getIntent().getStringExtra(String.valueOf(R.string.account_type_key));
//        if (account_type != null)
//        Log.d("account_type" , account_type);

        findID();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm() )
                {
                    customProgressDialog.startLoadingDailog();
                    Log.d("keyfromlistener", new SettingMemoryData(ActivityAfterMobileAuth.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)));
                    sendDataToDataBase();
//                    setSharedPref();
//                    customProgressDialog.dismissDialog();
                }
                else
                {
                    Log.d("error","form is not validated");
                    customDialogMaker.createAndShowDialogWarning("form is not validated please try again");
                }
            }
        });
    }

//    private void setSharedPref() {
//
//        SettingMemoryData settingMemoryData = new SettingMemoryData(ActivityAfterMobileAuth.this);
//        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), username.getText().toString());
//
//    }

    private void sendDataToDataBase() {
        //sending data to the database
        retrieveData();
        // updateData()
        // uploadData()
    }

    private void retrieveData() {
        final String userID = new SettingMemoryData(ActivityAfterMobileAuth.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
        userData = new UserData();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    userData = snapshot1.getValue(UserData.class);
                    Log.d("key" , Objects.requireNonNull(userData).getUsername());
                    if(userData!=null)
                    if(userData.getUsername().equals(userID)){
//                        if (new UserNameAccessibility().MatchUserName(userID)){
//                            Log.d("found","data found");
//                        }
                        deletePrevious(userID);
                        UpdateData(userData);
                        Log.d("success" , "data found");
                        break;
                    }else
                    {
                        Log.d("error" , "no data found" + " " + userData.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error" , error.getMessage() + " " + error.getClass().getName());
                customProgressDialog.dismissDialog();
            }
        });
    }

    private void deletePrevious(final String userID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.child(userID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("deleting user" , "Deleting User from database"+userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("error", e.getMessage() + " "  + e.getClass().getName());
            }
        });
    }

    private void verifyUserName(final UserData userData) {

//        String Username = username.getText().toString().trim();

      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
      databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              for(DataSnapshot snapshot1 : snapshot.getChildren())
              {
                  UserData userData1 = snapshot1.getValue(UserData.class);
                  if (userData1 != null)
                  if (userData1.getUsername().equals(userData.getUsername()))
                  {
                      username.setError("please enter valid username");
                      username.requestFocus();
                      customProgressDialog.dismissDialog();
                      return;
                  }
              }
              SettingMemory(userData);
              UploadDataToDataBase(userData);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }

    private void UpdateData(UserData userData) {

        Log.d("userData" , userData.getEmail() + userData.getUsername()+userData.getPassword() + userData.getNumber() );

        userData.setNumber_of_purchase("0");
        userData.setNumber_of_requests("0");
        userData.setUsername(username.getText().toString().trim());
        userData.setPassword(Password);
        userData.setAddress("Alwar , Rajastha");
        userData.setEmail(Email);
        userData.setName(Name);

        verifyEmailAndPassword(userData);

    }

    private void SettingMemory(UserData userData) {

        SettingMemoryData settingMemoryData = new SettingMemoryData(ActivityAfterMobileAuth.this);

        Log.d("memory_writing" , "writing data into memory");

        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), userData.getUsername());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_EMAIL), userData.getEmail());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , userData.getName()) ;
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_ADDRESS_USER) , userData.getAddress());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , userData.getNumber());
        settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key) , "user");
    }

    private void verifyEmailAndPassword(final UserData userData) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(userData.getEmail() , userData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                    verifyUserName(userData);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException)
                {
                    customProgressDialog.dismissDialog();
                    email.setError("please enter valid email address");
                    email.requestFocus();
                }
            }
        });
    }


    private void UploadDataToDataBase(UserData userData) {

        new UploadData(ActivityAfterMobileAuth.this).UploadDataOfUser(userData);

        customDialogMaker.createAndShowDialogSuccess("you have successfully logged in" , "user");
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
        sign_up_btn = findViewById(R.id.login_button1);

        customDialogMaker = new CustomDialogMaker(ActivityAfterMobileAuth.this);
        customProgressDialog = new CustomProgressDialog(ActivityAfterMobileAuth.this);
    }

    private boolean isValidEmail(String email)
    {
        String regex = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

        if(email.trim().matches(regex))
            IS_VALID_EMAIL = true;

        return  IS_VALID_EMAIL;
    }



}
