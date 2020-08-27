package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.selflearn.alwarrenter.R;

import Admin.ModelClass.AdminCredentials;

public class ChangeAdminIDAndPassword extends AppCompatActivity {
    
    EditText id , password;
    Button change;
    ImageButton backBtn;
    private static final String TAG = "ChangeAdminIDAndPasswor";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chnage_password);
        
        findID();
    }

    private void findID() {
        
        id = findViewById(R.id.new_pass);
        id.setInputType(InputType.TYPE_CLASS_NUMBER);
        id.setHint(" enter  code for new AdminID");
        password  = findViewById(R.id.new_pass_confirm);
        password.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        backBtn =findViewById(R.id.backBtn_change_pass);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        change = findViewById(R.id.change);
        
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm())
                {
                    setAdminIDAndPassForFirstTime();
                }
            }
        });
        
    }

    private boolean validateForm() {
        boolean result = false;

        if (id.getText().toString().isEmpty())
        {
            id.setError("please enter 4 digit numeric safe code");
            id.requestFocus();
        }else if (password.getText().toString().isEmpty())
        {
            password.setError("please enter at least 4 digit code");
            password.requestFocus();
        }else result = true;

        return  result;
    }

    private void setAdminIDAndPassForFirstTime() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminAccess");
        AdminCredentials credentials = new AdminCredentials("AlwarRental@AdminAccess#" + id.getText().toString().trim(), "AlwarRental@PasswordAccess#" + password.getText().toString().trim());
        databaseReference.setValue(credentials).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "onSuccess: data values are settled in database for admin access");
                finish();
                startActivity(new Intent(ChangeAdminIDAndPassword.this , AdminDashBoardActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage()+" " +e.getClass().getName());
            }
        });
    }
}
