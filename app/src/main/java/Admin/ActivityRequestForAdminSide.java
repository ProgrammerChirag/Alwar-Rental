package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;

import Admin.AdminAdapter.RequestDataAdapter;
import ModelClasses.RequestData;
import Utils.CustomProgressDialog;

public class ActivityRequestForAdminSide extends AppCompatActivity {

    RecyclerView recyclerView ;
    TextView message ;
    private static final String TAG = "ActivityRequestForAdmin";
    CustomProgressDialog customProgressDialog;
    DatabaseReference databaseReference;
    ImageButton backBtn;
    List<RequestData> requestDataList;
    List<String> list_user_username;
    List<String> list_seller_username;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                for (DataSnapshot snapshot2 : snapshot1.getChildren())
                {
                    list_seller_username.add(snapshot2.getKey());
                    for (DataSnapshot snapshot3 : snapshot2.getChildren())
                    {
                        list_user_username.add(snapshot3.getKey());
                        Log.d(TAG, "onDataChange: "+snapshot2.getKey());
                        requestDataList.add(snapshot3.getValue(RequestData.class));
                    }
                }
            }
            if (requestDataList.size()>0)
            {
                customProgressDialog.dismissDialog();
                // setting adapter for recycler view.
                message.setVisibility(View.INVISIBLE);
                RequestDataAdapter adapter = new RequestDataAdapter(ActivityRequestForAdminSide.this , requestDataList , list_seller_username , list_user_username);
                //adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
//                databaseReference.removeEventListener(listener);


            }
            else {
                Log.d(TAG, "onDataChange: no data found");
                customProgressDialog.dismissDialog();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d(TAG, "onCancelled: " + error.getMessage() + " " + error.getClass().getName());
            customProgressDialog.dismissDialog();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_admin_side);

        findID();
        findData();

    }

    private void findData() {

        databaseReference = FirebaseDatabase.getInstance().getReference("RequestData");
        customProgressDialog.startLoadingDailog();
        databaseReference.addValueEventListener(listener);
    }

    private void findID() {


        customProgressDialog = new CustomProgressDialog(ActivityRequestForAdminSide.this);
        recyclerView = findViewById(R.id.recycler_view_request_history2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));

        message = findViewById(R.id.text2_message);

        backBtn = findViewById(R.id.backBtn_to_dashboard_admin);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        requestDataList = new ArrayList<>();
        list_seller_username = new ArrayList<>();
        list_user_username = new ArrayList<>();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



// 7391082537

