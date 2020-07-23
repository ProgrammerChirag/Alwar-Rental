package User;

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
import android.widget.TextView;
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

import FirebaseConnectivity.UploadData;
import Utils.SettingMemoryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class UserLoginActivity extends AppCompatActivity {

    LinearLayout create_acc_button;
    TextView forgotten_password_button;
    EditText email , password;
    Button login ;
    boolean IS_VALIDATED=false;
    boolean IS_VALID_EMAIL = false;
    CustomDialogMaker customDialogMaker ;
    CustomProgressDialog customProgressDialog;
    List<UserData> userDataList ;


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_login);
        setContentView(R.layout.activity_user_login);

        findID();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()){

                    customProgressDialog.startLoadingDailog();
                    sendDataToDataBase();
                    loginWithEmailAndPassword();
                }
            }
        });

//        forgotten_password_button.setVisibility(View.VISIBLE);
//        forgotten_password_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserLoginActivity.this , ForgotPasswordHelper.class));
//            }
//        });


        create_acc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ActivityNavigation.openUserSignupActivity(UserLoginActivity.this);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

    private void sendDataToDataBase() {

        savingMemoryData();

        // sending the data into database
    }

    private void savingMemoryData() {
        final String Email = email.getText().toString().trim() ;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    UserData userData = snapshot1.getValue(UserData.class);
                    if(userData!=null){
                        if(Email.equals(userData.getEmail()))
                        {
//                            SaveSharedMemoryData(userData);

                            SettingMemoryData settingMemoryData = new SettingMemoryData(UserLoginActivity.this);

                            settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID) , userData.getUsername());
                            settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , userData.getName());
                            settingMemoryData.setSharedPrefString(String.valueOf(R.string.account_type_key) , "user");
                            settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_ADDRESS_USER) , userData.getAddress());
                            settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) ,userData.getNumber());

                            new UploadData(UserLoginActivity.this).UploadDataOfUser(userData);

                            return;
                        }
                    }
                    else{
                            Log.d("error" , "userdata object is null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error" , error.getMessage() + " " + error.getClass().getName());
            }
        });

    }

    private void findID() {

        create_acc_button=findViewById(R.id.create_acc_button);
        login = findViewById(R.id.login_button_seller);
        email = findViewById(R.id.user_id);
        password = findViewById(R.id.user_password);
        forgotten_password_button = findViewById(R.id.forgot_password_button2);
         customDialogMaker = new CustomDialogMaker(UserLoginActivity.this);
        customProgressDialog = new CustomProgressDialog(UserLoginActivity.this);


    }

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
                            customProgressDialog.dismissDialog();

                            settingSharedPref();

                            customDialogMaker.createAndShowDialogSuccess("Happy login","user");

                            //setting account type in memory with shared pref
                            new SettingMemoryData(UserLoginActivity.this).setSharedPrefString(String.valueOf(R.string.account_type_key),"user");

                            if(Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()){
                                Toast.makeText(getApplicationContext(), "congratulation you are logged in",Toast.LENGTH_LONG).show();
                            }
                            else if(!task.getResult().getAdditionalUserInfo().isNewUser()){
                                Toast.makeText(getApplicationContext() , "welcome back" , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismissDialog();

                        Log.d("error", e.getClass().getName());
                        if(e instanceof FirebaseAuthInvalidUserException)
                        {
                            customDialogMaker.createAndShowDialogWarning("no user found");
                        }
                        else if(e instanceof FirebaseAuthInvalidCredentialsException){
                            customDialogMaker.createAndShowDialogWarning("please enter valid email and password");
                        }
                    }
                });
    }

    private void settingSharedPref() {

        userDataList = new ArrayList<>();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData userData = snapshot1.getValue(UserData.class);
                    userDataList.add(userData);
                }
                Log.d("list size", String.valueOf(userDataList.size()));
                settingSharedPrefData(userDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error" , error.getMessage() + " " + error.getClass().getName());
            }
        });


    }

    private void settingSharedPrefData(List<UserData> userDataList) {

        String userId ;

        String Email = email.getText().toString().trim();

        if(userDataList != null) {
            for (int i = 0; i < userDataList.size(); i++) {
                if (!Email.isEmpty()) {
                    if (Email.equals(userDataList.get(i).getEmail())) {
                        userId = userDataList.get(i).getUsername();
                        Log.d("success", "user id found");
                        new SettingMemoryData(UserLoginActivity.this).setSharedPrefString(String.valueOf(R.string.KEY_USER_ID), userId);
                        break;
                    }
                } else {
                    Log.d("error", "email is empty");
                }
            }
        }
    }

    private boolean isValidEmail(String email)
    {
        String regex = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

        if(email.trim().matches(regex))
            IS_VALID_EMAIL = true;

        return  IS_VALID_EMAIL;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(UserLoginActivity.this, ChooseLoginTypeActivity.class));
    }
}