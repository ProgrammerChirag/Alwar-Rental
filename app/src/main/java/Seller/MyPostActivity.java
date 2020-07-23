package Seller;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import ModelClasses.PropertyData;
import Seller.Adapter.MyPostAdapter;
import Utils.SettingMemoryData;

public class MyPostActivity extends AppCompatActivity {

    TextView textView;
    RecyclerView recyclerView;
    private List<PropertyData> propertyDataList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_user);

        textView = findViewById(R.id.text);
        textView.setText("My Posts");
        propertyDataList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_request_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));
        loadMyPosts();
    }

    private void loadMyPosts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PropertyData");
        String uid = new SettingMemoryData(MyPostActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PropertyData propertyData;
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    propertyData = snapshot1.getValue(PropertyData.class);
                    propertyDataList.add(propertyData);
                }

                recyclerView.setAdapter(new MyPostAdapter(propertyDataList , MyPostActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
