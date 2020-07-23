package Seller;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import java.util.Objects;

import ModelClasses.PropertyData;
import Seller.Adapter.FindPropertyAdapter;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ActivityFindRentalProperty extends AppCompatActivity {

    TextView textView ;
    RecyclerView recyclerView;
    List<PropertyData> requestDataList ;
    List<PropertyData> propertyDataList ;
    TextView textView1;
    private static final String TAG = "ActivityFindRentalPrope";
    CustomProgressDialog customProgressDialog;
    List<Bitmap> bitmapList;
    DatabaseReference databaseReference;
    List<String> dirName;
    List<String> list;
    PropertyData propertyData;
     String propertyType;


    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "onDataChange: "+"getting property data");

            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                Log.d(TAG, "onDataChange: executing snapshot1");
                String username = snapshot1.getKey();
                Log.d(TAG, "onDataChange: "+username);

                if (!getMyUID().equals(username)) {

                    Log.d(TAG, "onDataChange: "+getMyUID().equals(username));

                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                        Log.d(TAG, "onDataChange: "+snapshot2.getKey());

                        if (snapshot2.getValue(PropertyData.class) != null) {
                            propertyData = snapshot2.getValue(PropertyData.class);
                            if (propertyData != null) {
                                Log.d(TAG, "onDataChange: "+propertyData.getPurchaseType() + propertyType);
                            }

                            if (propertyData != null) {
                                if (propertyData.getPurchaseType().equals(propertyType)) {

                                    propertyDataList.add(snapshot2.getValue(PropertyData.class));
                                    dirName.add(snapshot2.getKey());

                                } else {
                                    Log.d(TAG, "onDataChange: no match");
                                    Log.d(TAG, "onDataChange: " + getMyUID());
                                    Log.d(TAG, "onDataChange: " + Objects.requireNonNull(snapshot2.getValue(PropertyData.class)).getSellerId());
                                }
                            }
                        } else
                            Log.d(TAG, "onDataChange: getting data null");

                        if (propertyDataList.size() > 0 && dirName.size() >0) {
                            textView1.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onSuccess: calling adapter");
                            recyclerView.setAdapter(new FindPropertyAdapter(ActivityFindRentalProperty.this, propertyDataList, dirName));
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_user);

        textView = findViewById(R.id.text);
        textView.setText("properties for rent");
        requestDataList = new ArrayList<>();
        textView1 = findViewById(R.id.text2);
        bitmapList = new ArrayList<>();
        list = new ArrayList<>();
        dirName = new ArrayList<>();
        propertyDataList = new ArrayList<>();
        customProgressDialog = new CustomProgressDialog(ActivityFindRentalProperty.this);

        recyclerView = findViewById(R.id.recycler_view_request_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityFindRentalProperty.this , RecyclerView.VERTICAL , false) );
        recyclerView.setHasFixedSize(true);

        Log.d(TAG, "onCreate: "+propertyType);
        propertyType = getIntent().getStringExtra("propertyType");
        Log.d(TAG, "onCreate: "+propertyType);


        try {
            getPropertyData();
        }catch (Exception e)
        {
            Log.d(TAG, "onCreate: "+e.getMessage() + " " + e.getClass().getName() );
        }

    }



    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onBackPressed();
    }

    public void getPropertyData() {

        Log.d(TAG, "getPropertyData: "+"executing task");
        databaseReference = FirebaseDatabase.getInstance().getReference("PropertyData");
        databaseReference.addValueEventListener(listener);
    }

    public String getMyUID()
    {
        return  new SettingMemoryData(ActivityFindRentalProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
    }

}

//gs://alwarrenter.appspot.com/SellerPropertyImages/seller/junejachirag022/2020-07-14 11:42:35 AM/3rb2tVgyKO




