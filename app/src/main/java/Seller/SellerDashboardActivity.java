package Seller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.selflearn.alwarrenter.R;

import java.util.Objects;

import FirebaseConnectivity.UploadData;
import ModelClasses.SellerData;
import Seller.Fragement.SellerDashBoardHomeFragment;
import Seller.Fragement.SellerProfileFragment;
import Utils.CustomDialogMaker;
import Utils.SettingMemoryData;

public class SellerDashboardActivity extends AppCompatActivity {

    private static final String TAG = "SellerDashboardActivity";
    CustomDialogMaker customDialogMaker;
    DatabaseReference databaseReference ;
    String username;
    SellerData sellerData;
    String number;
    SettingMemoryData settingMemoryData;
    String acconut_type ;
    private Fragment selected;
    BottomNavigationViewEx bottomNavigationViewEx;



    // define all the listeners

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            settingMemoryData = new SettingMemoryData(SellerDashboardActivity.this);
            username = settingMemoryData.getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                sellerData = snapshot1.getValue(SellerData.class);
                if (sellerData != null)
                if(username.equals(sellerData.getUsername())){

                    sellerData.setNumber(number);

                    new UploadData(SellerDashboardActivity.this).UploadDataOfSeller(sellerData , SellerDashboardActivity.this);
                    Toast.makeText(getApplicationContext() , "you are all set" , Toast.LENGTH_LONG).show();
                    databaseReference.removeEventListener(listener);
                    return;

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d(TAG, "onCancelled: error occur" + error.getMessage() + " " + error.getClass().getName());

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener listener_Activity = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId())
            {
                case R.id.home:

                    selected = new SellerDashBoardHomeFragment(acconut_type , SellerDashboardActivity.this);
                    break;
                case R.id.profile:

                    selected = new SellerProfileFragment(SellerDashboardActivity.this , acconut_type);
                    break;
            }

            assert selected != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , selected).commit();

            return true;
        }
    };


    @Override
    protected void onStart() {

        customDialogMaker = new CustomDialogMaker(SellerDashboardActivity.this);

        checkEmailVerifiedOrNot();

        super.onStart();
    }


    // don't need to check again and again

//    @Override
//    protected void onResume() {
//
//        customDialogMaker = new CustomDialogMaker(SellerDashboardActivity.this);
//
//        checkEmailVerifiedOrNot();
//        super.onResume();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_user);
        customDialogMaker = new CustomDialogMaker(SellerDashboardActivity.this);

        acconut_type = getIntent().getStringExtra(String.valueOf(R.string.account_type_key));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , new SellerDashBoardHomeFragment(acconut_type , SellerDashboardActivity.this)).commit();
        findID();
//        checkEmailVerifiedOrNot();

    }

    private void findID() {
        bottomNavigationViewEx = findViewById(R.id.Bottom_Navigation);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(listener_Activity);
    }


    // functions
    public void checkEmailVerifiedOrNot() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        firebaseUser.reload();
        if (firebaseUser != null && firebaseUser.getEmail() != null)
        if (!firebaseUser.isEmailVerified() && !firebaseUser.getEmail().isEmpty())
        {
            Log.d(TAG, "checkEmailVerifiedOrNot: " + firebaseUser.getEmail());
            customDialogMaker.createAndShowDialogWarningWithoutCancel("please verify your email account ");
            Log.d(TAG, "checkEmailVerifiedOrNot: email not verified");
            Log.d(TAG, "checkEmailVerifiedOrNot: " + firebaseUser.getEmail());
            sendMailAgain();

        }else if(firebaseUser.isEmailVerified())
        {
            Log.d(TAG, "checkEmailVerifiedOrNot: email verified");
//            customDialogMaker.dismissDialog();
            checkPhoneNumberExistsOrNot();
        }

    }

    private void sendMailAgain() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: email sent again");
                    Toast.makeText(getApplicationContext() , "email sent successfully"  , Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: "+e.getMessage()+  " " +e.getClass().getName());
                }
            });
        }
    }

    private void checkPhoneNumberExistsOrNot() {
//        Toast.makeText(getApplicationContext() , "checking for phone number"  , Toast.LENGTH_LONG).show();

        String Uid = new SettingMemoryData(SellerDashboardActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER));
        if(Uid == null)
        {
           customDialogMaker.createAndShowDialogWarning("something went wrog");
        }else
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
            databaseReference.child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SellerData sellerData ;

                    sellerData = snapshot.getValue(SellerData.class);

                    if (sellerData != null)
                    if(sellerData.getNumber() == null)
                    {
                        Toast.makeText(getApplicationContext() , "phone number not found" , Toast.LENGTH_LONG).show();
                        getPhoneNumberFromSeller();
                    }else{
                        Toast.makeText(getApplicationContext() , "everything is good" , Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void getPhoneNumberFromSeller() {

        Log.d(TAG, "getPhoneNumberFromSeller: calling phone number function");
        // take phone number as input and then set to database and also in internal memory.

        AlertDialog.Builder builder = new AlertDialog.Builder(SellerDashboardActivity.this);
//        builder = new AlertDialog.Builder(activity);
        builder.setTitle("password")
                .setMessage("please enter your phone number")
        ;
        final EditText editText = new EditText(SellerDashboardActivity.this);
        editText.setBackground(this.getDrawable(R.drawable.edit_text_style));
        editText.setHint("please enter your phone number");
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
//        editText.setPadding(100,10,100,10);
        editText.setWidth(150);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(20, 10, 20, 10);
//        editText.setLayoutParams(lp);

        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        builder.setView(editText);
        builder.setCancelable(false);

        builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editText.getText().toString().length()<10)
                {
                    editText.setError("please enter valid phone number");
                    editText.requestFocus();
                }
                else
                {
//                    password = editText.getText().toString().trim();
//                    new CustomProgressDialog(activity).startLoadingDailog();
//                    new UserDataHandler(activity).checkPasswordAndChangeActivity(password);
                    number = editText.getText().toString().trim();
                    dialog.dismiss();
                    retrieveData();

                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog;
        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable( this.getApplicationContext().getDrawable(R.drawable.custom_dialog_background_style) );
        dialog.show();

    }

    private void retrieveData() {

         databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");

        databaseReference.addValueEventListener(listener);
    }

}
