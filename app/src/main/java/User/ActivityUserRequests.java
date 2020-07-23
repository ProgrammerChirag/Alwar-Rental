package User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;

import ModelClasses.RequestData;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ActivityUserRequests extends AppCompatActivity {

    ImageButton backBtn ;
    RecyclerView recyclerView_user_request;
    List<RequestData> requestDataList;
    CustomProgressDialog customProgressDialog;
    TextView text;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.layout_request_user);

        requestDataList = new ArrayList<>();
        customProgressDialog = new CustomProgressDialog(ActivityUserRequests.this);
        backBtn = findViewById(R.id.backBtn);
        recyclerView_user_request = findViewById(R.id.recycler_view_request_history);
        text = findViewById(R.id.text2);
        recyclerView_user_request.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL ,false));
        recyclerView_user_request.setHasFixedSize(true);

        getAllRequestData();

    }

    private void getAllRequestData() {

        customProgressDialog.startLoadingDailog();
        String UID = new SettingMemoryData(ActivityUserRequests.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

        Log.d("UID"  , UID);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestData");
        databaseReference = databaseReference.child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    RequestData requestData = snapshot1.getValue(RequestData.class);
                    requestDataList.add(requestData);
                }
                Log.d("number of values" , String.valueOf(requestDataList.size()));

                if(recyclerView_user_request != null) {

                    text.setVisibility(View.INVISIBLE);
                    recyclerView_user_request.setAdapter(new UserRequestViewAdapter(ActivityUserRequests.this, requestDataList));
                    customProgressDialog.dismissDialog();
                }
                else
                    Toast.makeText(getApplicationContext() , "no data found", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error",error.getMessage() + " " + error.getClass().getName());
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intent = new Intent(ActivityUserRequests.this , BottomNavActivity.class);
        intent.putExtra(String.valueOf(R.string.account_type_key),"user");
        finish();
        startActivity(intent);
    }
}
