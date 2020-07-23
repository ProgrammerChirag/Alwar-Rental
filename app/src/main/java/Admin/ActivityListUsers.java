package Admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;

import Admin.AdminAdapter.UserListAdapter;
import ModelClasses.UserData;
import Utils.CustomProgressDialog;


public class ActivityListUsers extends AppCompatActivity {

    TextView text;
    TextView text1;
    private static final String TAG = "ActivityListUsers";
    RecyclerView recyclerView;
    ImageButton backBtn;
    DatabaseReference databaseReference;
    List<UserData> userDataList;
    CustomProgressDialog customProgressDialog;
    List<Bitmap> bitmapList;


    private ValueEventListener listener_get_user_data = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot){
            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                userDataList.add(snapshot1.getValue(UserData.class));
            }
            if (userDataList.size() > 0)
            {

                findProfileOfUsers();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            customProgressDialog.dismissDialog();
        }
    };

    private void findProfileOfUsers() {

        for (int i=0 ; i <userDataList.size() ; i ++)
        {
             String url = "gs://alwarrenter.appspot.com/ProfileImages/user/" + userDataList.get(i).getUsername() + "/" + "Profile";
             final String  uname = userDataList.get(i).getUsername();

             StorageReference storageReference;
             storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            Log.d(TAG, "findProfileOfUsers: "+storageReference);

            storageReference.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
                    bitmapList.add(bitmap);
                    Log.d(TAG, "onSuccess: bitmap added to the list");

                    Log.d(TAG, "findProfileOfUsers: checking");
                    if (userDataList.size() > 0 && bitmapList.size() == userDataList.size()) {

                        recyclerView.setAdapter(new UserListAdapter(  userDataList , ActivityListUsers.this , bitmapList));
                        text1.setVisibility(View.INVISIBLE);
                        databaseReference.removeEventListener(listener_get_user_data);
                        customProgressDialog.dismissDialog();
                    }
                    else {
                        Log.d(TAG, "findProfileOfUsers: " + bitmapList.size());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int ERROR_CODE = ((StorageException)e).getErrorCode();
                    if (ERROR_CODE == StorageException.ERROR_OBJECT_NOT_FOUND)
                    {
                        Log.d(TAG, "onFailure: "+uname);
                        Log.d(TAG, "onFailure: "+e.getMessage() + " " + e.getClass().getName());
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.demoimage);
                        bitmapList.add(bitmap);
                        Log.d(TAG, "onFailure: bitmap added to the list");

                        Log.d(TAG, "findProfileOfUsers: checking");
                        if (userDataList.size() > 0 && bitmapList.size() == userDataList.size()) {

                            recyclerView.setAdapter(new UserListAdapter(  userDataList , ActivityListUsers.this , bitmapList));
                            text1.setVisibility(View.INVISIBLE);
                            databaseReference.removeEventListener(listener_get_user_data);
                            customProgressDialog.dismissDialog();
                        }
                        else {
                            Log.d(TAG, "findProfileOfUsers: " + bitmapList.size());
                        }
                    }
                }
            });


        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_user);


        findID();
        loadUserData();
    }


    private void loadUserData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        customProgressDialog.startLoadingDailog();
        databaseReference.addValueEventListener(listener_get_user_data);
    }

    private void findID() {

        text = findViewById(R.id.text);
        text.setText(getResources().getString(R.string.list_of_users_in_your_app));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_request_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL  , false));

        userDataList = new ArrayList<>();
        bitmapList = new ArrayList<>();

        text1 = findViewById(R.id.text2);

        customProgressDialog = new CustomProgressDialog(ActivityListUsers.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
