package User.ActivityFragments;

import android.app.Activity;
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

import ModelClasses.TransactionData;
import User.ActivityUserRequests;
import User.UserTransactionAdapter;
import Utils.SettingMemoryData;

public class HomeActivity extends Fragment {

    String account_type;
    Context context;
    Button firstOption, secondOption , thirdOption;
    RecyclerView history;
    private static final String TAG = "HomeActivity";

    List<TransactionData> transactionDataList;


    public HomeActivity(String account_type , Context context) {
        this.account_type = account_type;
        this.context = context;
    }
    public HomeActivity()
    {}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home_fragment, container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findID(view);
        setOnClickListener();
        setAdapter();
    }

    private void setAdapter() {
        // setting adapter for history recycler view

        if (history != null)
        {
            history.setLayoutManager(new LinearLayoutManager(context , RecyclerView.VERTICAL , false));
            history.setHasFixedSize(true);
            retrieveDataAndSetAdapter(history);
        }else
            Log.d(TAG, "setAdapter:  history is null");
    }

    private void retrieveDataAndSetAdapter(final RecyclerView history) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if (transactionDataList != null) {

                        transactionDataList.add(snapshot1.getValue(TransactionData.class));
                        Log.d(TAG, "onDataChange: " + transactionDataList.size());
                    }
                }
                if (transactionDataList != null)
                history.setAdapter(new UserTransactionAdapter(context , transactionDataList));
                else Log.d(TAG, "onDataChange: "+ "null list please initialize the list");

                if (transactionDataList.size() == 0)
                {
                    Log.d(TAG, "onDataChange: "+"no data found in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

            databaseReference = databaseReference.child("MyTransaction");

        String userId = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
        if (userId != null)
        {
            databaseReference.child(userId).addValueEventListener(listener);
        }

    }

    private void setOnClickListener() {
        firstOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)(context)).finish();
                if (getActivity() != null)
                getActivity().startActivity(new Intent(getActivity() , ActivityUserRequests.class));
            }
        });
        secondOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_type.equals("user"))
                {
                    Intent intent = new Intent(context.getApplicationContext() , Seller.ActivityFindRentalProperty.class);
                    Log.d(TAG, "onClick: "+getResources().getString(R.string.on_rent));
                    intent.putExtra("propertyType" , getResources().getString(R.string.on_rent));
                    startActivity(intent);
                }
            }
        });
        thirdOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_type.equals("user"))
                {
                    Intent intent = new Intent(context.getApplicationContext() , Seller.ActivityFindRentalProperty.class);
                    intent.putExtra("propertyType" , getResources().getString(R.string.for_purchase));
                    startActivity(intent);
                }
            }
        });
    }

    private void findID(View view) {

        firstOption = view.findViewById(R.id.your_requests);
        secondOption = view.findViewById(R.id.find_property_for_rent);
        thirdOption = view.findViewById(R.id.purchase_property);
        history = view.findViewById(R.id.recycler_view_transaction_history);
        transactionDataList = new ArrayList<>();

    }
}
