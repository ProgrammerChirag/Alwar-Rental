package User;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import FirebaseConnectivity.UploadData;
import Helper.UserSignUpHelper;
import ModelClasses.UserData;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    EditText new_password , confirm_new_password;
    Button change;
    boolean IS_VALIDATED = false;
    String password , confirm_password;
    String UID;
    UserData userData;
    CustomProgressDialog customProgressDialog;
    private DatabaseReference databaseReference;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            customProgressDialog.startLoadingDailog();
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                userData = snapshot1.getValue(UserData.class);
                if (userData != null) {
                    if (userData.getUsername().equals(UID) ) {

                        changePasswordOfUserData();
                        databaseReference.removeEventListener(listener);
                        break;
                    }
                    else
                        Log.d("warning" , "password not matched or username not found" + userData.getUsername());
                }
                else
                    Log.d("error","no data found");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chnage_password);

        UID = getIntent().getStringExtra("UID");

        new_password = findViewById(R.id.new_pass);
        confirm_new_password = findViewById(R.id.new_pass_confirm);
        change = findViewById(R.id.change);
        customProgressDialog = new CustomProgressDialog(ChangePasswordActivity.this);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = new_password.getText().toString().trim();
                confirm_password = confirm_new_password.getText().toString().trim();

                if (validateForm(password , confirm_password))
                {
                    change.setEnabled(false);
                    customProgressDialog.startLoadingDailog();
                    changePassword();
                    Log.d("success","form validated");
                }else
                {
                    Log.d("error","form not validated");
                }
            }
        });

    }

    private void changePassword() {


         databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference.addValueEventListener(listener);
        customProgressDialog.dismissDialog();
//        databaseReference.removeEventListener(listener);

    }

    private void UploadDataInDataBase(UserData userData) {

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        new UploadData(ChangePasswordActivity.this).UpadaePasswordInDataBase(userData);
        new CustomDialogMaker(ChangePasswordActivity.this).createAndShowDialogSuccess("password changed successfully","user");
    }

    private void changePasswordOfUserData() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(userData.getEmail() , userData.getPassword() );
        if(firebaseUser != null)
        firebaseUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseUser.updatePassword(confirm_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("password",userData.getPassword());
                        UploadDataInDataBase(userData);
                        Log.d("success","password updated");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("error1",e.getMessage() +" "+e.getClass().getName());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error2",e.getMessage()+" "+e.getClass().getName());
                Log.d(TAG, "onFailure: "+userData.getPassword());
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    new CustomDialogMaker(ChangePasswordActivity.this).createAndShowDialogWarning("your given details are wrong or someone has changed forcefully your details please contact to our given gmail account");
                }
            }
        });

        userData.setPassword(password);
        Log.d("password",userData.getPassword());

    }

    private boolean validateForm(String password, String confirm_password) {
        if(!password.isEmpty() && !confirm_password.isEmpty()  &&password.equals(confirm_password) && new UserSignUpHelper().isPasswordValid(password))
        {
            IS_VALIDATED = true;
        }
        else if(password.isEmpty())
        {
            new_password.setError("please enter password");
            new_password.requestFocus();
        }else  if(confirm_password.isEmpty())
        {
            confirm_new_password.setError("please enter password");
            confirm_new_password.requestFocus();
        }else if(!password.equals(confirm_password))
        {
            confirm_new_password.setError("please enter valid  password");
            confirm_new_password.requestFocus();
        }else
        {
            IS_VALIDATED = true;
        }
        return  IS_VALIDATED;
    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onDestroy();
    }
}
