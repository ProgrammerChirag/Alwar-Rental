package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import Admin.ModelClass.AdminCredentials;
import Utils.CustomProgressDialog;

public class AdminLoginActivity extends AppCompatActivity {

    EditText user , pass;
    Button login;
    AdminCredentials credentails;
    CustomProgressDialog customProgressDialog;
    DatabaseReference databaseReference;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            credentails = snapshot.getValue(AdminCredentials.class);

            if (credentails != null )
            {
                loginWithCredentials(credentails);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void loginWithCredentials(AdminCredentials credentails) {

       String id ;
       String password;

        id  = user.getText().toString().trim();
        password = pass.getText().toString().trim();

        if ( !id .isEmpty() && !password.isEmpty())
        {
            if (id.equals(credentails.getAdminID() ) && password.equals(credentails.getAdminPass()))
            {
                loginToNextPage();
            }
        }
    }

    private void loginToNextPage() {

        customProgressDialog.dismissDialog();
        finish();
        startActivity(new Intent(AdminLoginActivity.this , AdminDashBoardActivity.class));

    }

    private static final String TAG = "AdminLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_login);


       // setAdminIDAndPassForFirstTime();

        findID();

    }

    private void setAdminIDAndPassForFirstTime() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminAccess");
        AdminCredentials credentials = new AdminCredentials("AlwarRental@AdminAccess#000" , "AlwarRental@PasswordAccess#000");
        databaseReference.setValue(credentials).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: data values are settled in database for admin access");
                findID();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage()+" " +e.getClass().getName());
            }
        });
    }

    private void findID() {

        user = findViewById(R.id.main_user_id);
        pass = findViewById(R.id.main_user_password);
        login = findViewById(R.id.login_button_main_user);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm())
                {
                    customProgressDialog  = new CustomProgressDialog(AdminLoginActivity.this);
                    customProgressDialog.startLoadingDailog();
                    databaseReference = FirebaseDatabase.getInstance().getReference("AdminAccess");
                    databaseReference.addValueEventListener(listener);
                }
                else
                    Log.d(TAG, "onClick: form not validated");
            }
        });
    }

    private boolean validateForm() {
        boolean result = false;

        if (user.getText().toString().isEmpty())
        {
            user.setError("please enter main user id");
            user.requestFocus();
        }else if (pass.getText().toString().isEmpty())
        {
            pass.setError("please enter password");
            pass.requestFocus();
        }else
            result = true;

        return  result;
    }

    @Override
    public void onBackPressed() {
        customProgressDialog.dismissDialog();
        Log.d(TAG, "onBackPressed: back button pressed");
    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        super.onDestroy();
    }
}