package FirebaseConnectivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import ModelClasses.SellerData;
import Utils.CustomDialogMaker;
import Utils.SettingMemoryData;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.List;

import ModelClasses.RequestData;
import ModelClasses.UserData;

import static android.content.ContentValues.TAG;


public class UploadData {
    private  Context context;
    private String UserID;

//    private boolean IS_UPLOADED = false;

    public UploadData(Context context)
    {
        this.context = context;
    }

    public void UpadaePasswordInDataBase(final UserData userData)
    {
        final DatabaseReference databaseReference;

        Log.d("password", userData.getPassword());

        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

        databaseReference.child(userData.getUsername()).child("password").setValue(userData.getPassword())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success","password updated in database");

                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("SellerData");
                        databaseReference1.child(userData.getUsername()).child("password").setValue(userData.getPassword()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: " + "data fully uploaded");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage() + " " + e.getClass().getName());
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage() + " " + e.getClass().getName());

            }
        });
    }

    public void UploadDataOfUser(UserData userData)
    {
         DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

        databaseReference.child(userData.getUsername()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //                    IS_UPLOADED = true;
                Log.d("success","data uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("errorfromuploaddata",e.getMessage()+e.getClass().getName());
                new CustomDialogMaker((Activity) context).dismissDialog();
//                IS_UPLOADED = false;
            }
        });

    }

    public void UploadDataRequest(final RequestData requestData, String sellerID)
    {
        Log.d("sucess", "UploadDataRequest: upload data called");

        UserID = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

        if (UserID != null && !UserID.isEmpty())
            Log.d("userId" , UserID);

        UploadDataFromUserId(requestData , UserID , sellerID);

    }

    private void UploadDataFromUserId(RequestData requestData, String uid, String sellerID) {

        Log.d("success", "UploadDataFromUserId: data uploading to the database");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestData").child(sellerID);
        String key = databaseReference.child(uid).push().getKey();
        if (key != null) {
            databaseReference.child(uid).child(key).setValue(requestData).addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful() && task.getResult() != null)
             {
                 Toast.makeText(context.getApplicationContext() , "Request Sent" , Toast.LENGTH_LONG).show();
             }
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Log.d("error",e.getMessage()+" "+e.getClass().getName());
         }
     });
        }
    }

    public void UploadDataOfSeller(final SellerData sellerData , final Context context)
    {
        Log.d(TAG, "UploadDataOfSeller: uploading data to database of seller");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference.child(sellerData.getUsername()).setValue(sellerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: data uploaded successfully");
                new SettingMemoryData(context).setSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER) , sellerData.getNumber());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: error while uploading data to database");
                Log.d(TAG, "onFailure: " + e.getMessage() + " " + e.getClass().getName());
            }
        });
    }
}
