package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import ModelClasses.PropertyData;
import Seller.ActivityFindRentalProperty;
import Seller.Adapter.FindPropertyAdapter;
import Utils.CustomProgressDialog;

public class ListPropertiesActivity extends AppCompatActivity {

    List<PropertyData> propertyDataList;
    List<String> sellerIDs;
    ImageButton backBtn;
    RecyclerView recyclerView;
    List<String> dirNames;
    TextView title , message;
    CustomProgressDialog customProgressDialog;
    private static final String TAG = "ListPropertiesActivity";
    DatabaseReference propertyDataReference;

    private ValueEventListener data_fetcher  = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                sellerIDs.add(snapshot1.getKey());
                for (DataSnapshot snapshot2 : snapshot1.getChildren())
                {
                    dirNames.add(snapshot2.getKey());
                    propertyDataList.add(snapshot2.getValue(PropertyData.class));
                    Log.d(TAG, "onDataChange: "+propertyDataList.size());
                }
            }
            boolean condition = propertyDataList.size() == dirNames.size();
            if (propertyDataList.size() > 0 && dirNames.size() >0 && condition)
            {
                customProgressDialog.dismissDialog();
                Log.d(TAG, "onSuccess: calling adapter");
                recyclerView.setAdapter(new FindPropertyAdapter(ListPropertiesActivity.this, propertyDataList, dirNames));
                message.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d(TAG, "onCancelled: "+error.getMessage() +"  " + error.getClass().getName());
            customProgressDialog.dismissDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_user);

        findID();
        setListeners();
        try {
            customProgressDialog = new CustomProgressDialog(ListPropertiesActivity.this);
            customProgressDialog.startLoadingDailog();
            findData();
        }catch (Exception e)
        {
            Log.d(TAG, "onCreate: "+e.getMessage()+" " +e.getClass().getName());

        }
    }

    private void findData() {
        propertyDataReference = FirebaseDatabase.getInstance().getReference("PropertyData");
        propertyDataReference.addValueEventListener(data_fetcher);
    }


    @SuppressLint("SetTextI18n")
    private void findID() {

        propertyDataList  = new ArrayList<>();
        backBtn  = findViewById(R.id.backBtn);
        title = findViewById(R.id.text);
        message = findViewById(R.id.text2);
        title.setText("All property");

        recyclerView = findViewById(R.id.recycler_view_request_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));
        recyclerView.setHasFixedSize(true);

        sellerIDs = new ArrayList<>();
        dirNames  =new ArrayList<>();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}