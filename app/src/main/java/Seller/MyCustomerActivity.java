package Seller;

import android.annotation.SuppressLint;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ModelClasses.CustomerData;
import Seller.Adapter.MyCustomerAdapter;
import Utils.SettingMemoryData;

public class MyCustomerActivity extends AppCompatActivity {

    TextView textView;
    TextView message;
    ImageButton backBtn;

    RecyclerView recyclerView;
    private static final String TAG = "MyCustomerActivity";
    List<CustomerData> customerDataList;
    DatabaseReference data_fetcher;

    ValueEventListener listener_for_getting_data = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                    if (Objects.equals(snapshot1.getKey(), new SettingMemoryData(getApplicationContext()).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)))) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren())
                        {
                            message.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onDataChange: " + snapshot2.getKey());
                            customerDataList.add(snapshot2.getValue(CustomerData.class));
                        }
                    }
            }
            if (customerDataList !=null)
            {
                if (customerDataList.size() > 0)
                {
                    Log.d(TAG, "onDataChange: "+customerDataList.size());
                    // setting Adapter
                    MyCustomerAdapter adapter = new MyCustomerAdapter(MyCustomerActivity.this , customerDataList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MyCustomerActivity.this , RecyclerView.VERTICAL , false));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter);
                    Log.d(TAG, "onDataChange: removing listener");
                    data_fetcher.removeEventListener(listener_for_getting_data);
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

        findID();
        findData();

    }

    private void findData() {
        data_fetcher = FirebaseDatabase.getInstance().getReference("MyCustomerData");
        String uid = new SettingMemoryData(MyCustomerActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
        Log.d(TAG, "findData: "+uid);
        data_fetcher.child(uid);
        data_fetcher.addValueEventListener(listener_for_getting_data);
    }

    @SuppressLint("SetTextI18n")
    private void findID() {

        textView = findViewById(R.id.text);
        textView.setText("My Customers");

        recyclerView = findViewById(R.id.recycler_view_request_history);
        message = findViewById(R.id.text2);
        customerDataList = new ArrayList<>();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backBtn.setClickable(false);
                onBackPressed();

            }
        });

    }
}
