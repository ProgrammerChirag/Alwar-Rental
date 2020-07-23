package FirebaseConnectivity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;
import Utils.SettingMemoryData;

import java.util.ArrayList;
import java.util.List;

import ModelClasses.UserData;

public class UserDataHandler {
    private  Context context;
    private  List<UserData> userDataList;
    UserData userData ;

    public UserDataHandler(Context context)
    {
        this.context = context;
    }

//    public List<UserData> getUserData(){
//
//          userDataList = new ArrayList<>();
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1 : snapshot.getChildren())
//                {
//                    UserData userData = snapshot1.getValue(UserData.class);
//                    userDataList.add(userData);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("error" , error.getMessage() + " " + error.getClass().getName());
//            }
//        });
//
//        Log.d("data", userDataList.toString());
//
//        return  userDataList;
//    }

    public void checkPasswordAndChangeActivity(final String password , String DataType)
    {
        final String UserID = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DataType);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    userData = snapshot1.getValue(UserData.class);
                    if (userData != null) {
                        if (userData.getUsername().equals(UserID) && password.equals(userData.getPassword())) {
                            Log.d("data",userData.getUsername()+userData.getPassword());
                            new Utils.ActivityNavigation().ChnageActivityToPassword(context, password , UserID);
                            break;
                        }
                            else
                                Log.d("warning" , "password not matched or username not found");
                        }
                    else
                        Log.d("error","no data found");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
