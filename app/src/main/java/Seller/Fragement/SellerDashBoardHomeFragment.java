package Seller.Fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import Seller.MyCustomerActivity;
import Seller.MyPostActivity;
import Utils.SellerActivityNavigation;
import Utils.SettingMemoryData;


public class  SellerDashBoardHomeFragment extends Fragment{

    private static final String TAG = "SellerDashBoardHomeFrag";
    String account_type;
    Context context;
    Button myPosts;
    Button myCustomer ;
    Button postNewProperty;
    RecyclerView recyclerView;
    List<PropertyData> propertyDataList ;

    public SellerDashBoardHomeFragment()
    {

    }

    private View.OnClickListener listener_my_post = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // when seller click on my post button.
            startActivity(new Intent(context.getApplicationContext() , MyPostActivity.class));
        }
    };

//    private View.OnClickListener listener_my_requests = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // when seller click on my request button.
//            startActivity(new Intent(context.getApplicationContext() , MyRequestActivitySeller.class));
//
//        }
//    };

    private View.OnClickListener listener_post_new_property = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // when seller try to post some new house posts
            Log.d(TAG, "onClick: post new property clicked");
            new SellerActivityNavigation(context).openPostNewPropertyActivity();
        }
    };

//    private View.OnClickListener listener_find_house_for_rent = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // when seller click on find property for rent.
//            startActivity(new Intent(context.getApplicationContext() , ActivityFindRentalProperty.class));
//        }
//    };

//    private  View.OnClickListener listener_find_house_for_purchase = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // when seller try to find new property for purchase.
//
//
//        }
//    };

    private View.OnClickListener listener_my_customer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context.getApplicationContext() , MyCustomerActivity.class));

            // when seller click on myCustomer button.
        }
    };

    public SellerDashBoardHomeFragment(String acconut_type, Context context) {
        this.account_type = acconut_type;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return  inflater.inflate(R.layout.layout_home_fragment_seller , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        propertyDataList = new ArrayList<>();
        findID((ViewGroup) view);
        loadMyPosts();
    }

    private void loadMyPosts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PropertyData");
        String uid = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PropertyData propertyData;
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    propertyData = snapshot1.getValue(PropertyData.class);
                    propertyDataList.add(propertyData);
                }

                recyclerView.setAdapter(new MyPostAdapter(propertyDataList , context));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findID(ViewGroup container) {

        try {
//
//            myRequest = container.findViewById(R.id.your_requests_seller);
//            Log.d(TAG, "findID: button initialized");
//            myRequest.setOnClickListener(listener_my_requests);

            myCustomer = container.findViewById(R.id.button_my_customers_seller);
            Log.d(TAG, "findID: button initialized");

            myCustomer.setOnClickListener(listener_my_customer);

            myPosts = container.findViewById(R.id.btn_my_posts);
            Log.d(TAG, "findID: button initialized");

            myPosts.setOnClickListener(listener_my_post);

            postNewProperty = container.findViewById(R.id.button_post_new_property);
            Log.d(TAG, "findID: applying listener on post new property");
            postNewProperty.setOnClickListener(listener_post_new_property);

//            findNewPropertyForRent = container.findViewById(R.id.find_property_for_rent_seller);
//            Log.d(TAG, "findID: button initialized");
//
//            findNewPropertyForRent.setOnClickListener(listener_find_house_for_rent);
//
//            findNewPropertyForPurchase = container.findViewById(R.id.purchase_property);
//            Log.d(TAG, "findID: button initialized");
//
//            findNewPropertyForPurchase.setOnClickListener(listener_find_house_for_purchase);

        }catch (Exception e)
        {
            Log.d(TAG, "findID: " + e.getMessage() + " " +e.getClass().getName() );
            e.printStackTrace();
        }

        recyclerView =container.findViewById(R.id.recycler_view_my_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext() , RecyclerView.VERTICAL , false));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}