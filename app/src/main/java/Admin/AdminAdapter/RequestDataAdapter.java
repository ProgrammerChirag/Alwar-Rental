package Admin.AdminAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.R;

import java.util.ArrayList;
import java.util.List;

import ModelClasses.CustomerData;
import ModelClasses.PropertyData;
import ModelClasses.RequestData;
import ModelClasses.SellerData;
import ModelClasses.TransactionData;
import ModelClasses.UserData;
import Utils.CustomProgressDialog;

public class RequestDataAdapter extends RecyclerView.Adapter<RequestDataAdapter.RequestAdapterViewHolder> {

    List<RequestData> requestDataList;
    Context context;
    String user;
    CustomerData customerData;
    UserData user_data;
    String key;
    String time;
    SellerData  seller_Data;
    DatabaseReference databaseReference2 , databaseReference3 , databaseReference4 , deletePropertyData;
    List<String> sellerID;
    List<String> userData;
    public int position_data;
    RequestData data;
    List<RequestData> list;
    TransactionData transactionData;
    private static final String TAG = "RequestDataAdapter";
    CustomProgressDialog customProgressDialog;
    DatabaseReference databaseReference;
    DatabaseReference myTransaction;

    private  ValueEventListener listener = new ValueEventListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

           for (DataSnapshot snapshot1 : snapshot.getChildren())
           {
               RequestData requestData = snapshot1.getValue(RequestData.class);
               if (requestData != null) {
                   if (requestData.getDateUpload().equals(data.getDateUpload()))
                       key = snapshot1.getKey();
               }
           }

            if (key != null) {

                databaseReference.child(key).removeValue();
                databaseReference.removeEventListener(listener);
                customProgressDialog.dismissDialog();
                Log.d(TAG, "onDataChange: data deleted");

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getMessage() + " " +error.getClass().getName());
            customProgressDialog.dismissDialog();
        }
    };
    private ValueEventListener listener_for_deleting_propertyData = new ValueEventListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                PropertyData propertyData = snapshot1.getValue(PropertyData.class);

                if (propertyData != null && propertyData.getDateUpload() .equals(data.getDateUpload()))
                {
                    key = snapshot1.getKey();
                 break;
                }
            }
            if (key != null)
            {
                Log.d(TAG, "onDataChange: "+deletePropertyData.getPath());
                deletePropertyData.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deletePropertyData.removeEventListener(listener_for_deleting_propertyData);
                        Log.d(TAG, "onDataChange: property data deleted");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getClass().getName() + " " +e.getMessage());
                    }
                });


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void deletePropertyData() {

        deletePropertyData = FirebaseDatabase.getInstance().getReference("PropertyData");
        deletePropertyData = deletePropertyData.child(data.getSellerID());
        Log.d(TAG, "deletePropertyData: "+deletePropertyData.getKey());

        deletePropertyData.addListenerForSingleValueEvent(listener_for_deleting_propertyData);
    }

    private  ValueEventListener listener2 = new ValueEventListener() {

        @SuppressLint("RestrictedApi")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "onDataChange: called");

           for (DataSnapshot snapshot1 : snapshot.getChildren())
           {
               Log.d(TAG, "onDataChange: "+snapshot1.getKey());
               Log.d(TAG, "onDataChange: loop called again");
               RequestData requestData1 = snapshot1.getValue(RequestData.class);

               if (requestData1 != null) {
                   Log.d(TAG, "onDataChange: "+requestData1.getCost());
                   if (data.getDateUpload().equals(requestData1.getDateUpload()) && data.getLocation().equals(requestData1.getLocation())) {
                       key = snapshot1.getKey();
                       break;
                   }else
                   {
                       Log.d(TAG, "onDataChange: data not matching");
                       Log.d(TAG, "onDataChange: "+data.getLocation() + " " + requestData1.getLocation());
                       Log.d(TAG, "onDataChange: "+"\n" + data.getDateUpload() + " " + requestData1.getDateUpload());
                   }
               }
           }

            if (key != null) {
                try {

                    Log.d(TAG, "onDataChange: called");
                    final RequestData requestData =  requestDataList.get(position_data);
                    requestData.setStatus("approved");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RequestData");
                    databaseReference  = databaseReference.child(requestData.getSellerID()).child(requestData.getUserID());
                    databaseReference = databaseReference.child(key);
                    databaseReference.setValue(requestData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d(TAG, "onSuccess: data updated now shifting data of approved to new table");
                            shiftDataToNewTable(requestData);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "OnFailure: "+e.getClass().getName()+ " " + e.getMessage()) ;
                        }
                    });
                }catch (Exception e)
                {
                    Log.d(TAG, "onClick: "+e.getClass().getName()+ " " + e.getMessage()) ;
                }

                Log.d(TAG, "onDataChange: listener removed");
                databaseReference2.removeEventListener(listener2);
            }


        }



        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getMessage() + " " +error.getClass().getName());
            customProgressDialog.dismissDialog();
        }
    };

    private  ValueEventListener listener_for_transaction_data_initializer  = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "onDataChange: called");
            Log.d(TAG, "onDataChange: "+snapshot.getKey());

            seller_Data = snapshot.getValue(SellerData.class);

                transactionData = new TransactionData();

                if (seller_Data != null) {

                    transactionData.setAddress(data.getLocation());
                    time = java.time.LocalDateTime.now().toString();
                    transactionData.setAmountPaid(data.getCost());
                    transactionData.setDate(time);
                    transactionData.setPurchaseType(data.getRequestType());
                    transactionData.setSellerID(data.getSellerID());
                    transactionData.setSellerPhoneNumber(seller_Data.getNumber());
                    transactionData.setSellerName(seller_Data.getName());
                    transactionData.setTransactionMedium("cash");

                    myTransaction.setValue(transactionData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: data set in transaction data table");
                            SettingMyCustomerData(data);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "onFailure: "+e.getMessage() + " " +e.getClass().getName());

                        }
                    });
                }
                else Log.d(TAG, "TransactionDataInitializer: null");

                databaseReference3.removeEventListener(listener_for_transaction_data_initializer);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener listener_for_customer_data_initializer = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            user_data = snapshot.getValue(UserData.class);

            if (user_data!=null)
            {
                databaseReference4.removeEventListener(listener_for_customer_data_initializer);

                customerData = new CustomerData();
                customerData.setAddress(data.getLocation());
                customerData.setDate(time);
                customerData.setEmail(user_data.getEmail());
                customerData.setName(user_data.getName());
                customerData.setPhone(user_data.getNumber());
                customerData.setRequestType(data.getRequestType());
                customerData.setUserID(user_data.getUsername());

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyCustomerData");
                databaseReference = databaseReference.child(data.getUserID());
                String key = databaseReference.push().getKey();
                if (key != null) {
                    databaseReference = databaseReference.child(key);
                    databaseReference.setValue(customerData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: all data settled success in database");
                            Log.d(TAG, "onSuccess: removing this data from database");

                            removeDataAfterAccept();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+e.getClass().getName() + " " + e.getMessage());
                        }
                    });
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getClass().getName() + " " + error.getMessage());
        }
    };

    public RequestDataAdapter(Context context , List<RequestData> requestDataList , List<String> sellerID , List<String>  userData)
    {
        this.context = context;
        this.requestDataList = requestDataList;
        customProgressDialog = new CustomProgressDialog((Activity) context);
        this.sellerID = sellerID;
        this.userData = userData;
        list = new ArrayList<>();
    }


    @NonNull
    @Override
    public RequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_request_user_admin , parent , false);
        return new RequestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapterViewHolder holder, final int position) {
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                // changing status from pending to approved.
                // updating data to new table
                // setting myCustomer and transaction history table
                // setting exact amount paid by user.

                data= requestDataList.get(position);
                position_data = position;
                Log.d(TAG, "onClick: "+position_data);
                databaseReference2 = FirebaseDatabase.getInstance().getReference("RequestData");
                databaseReference2 = databaseReference2.child(data.getSellerID()).child(data.getUserID());
                Log.d(TAG, "onClick: "+databaseReference2.getPath());
                databaseReference2.addValueEventListener(listener2);


            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customProgressDialog = new CustomProgressDialog((Activity) context);
                customProgressDialog.startLoadingDailog();


                try {

                    Log.d(TAG, "onClick: deleting user");
                    data = requestDataList.get(position);
                    user = userData.get(position);
                    deleteUser(data);


                }catch (Exception e)
                {
                    Log.d(TAG, "onClick: "+e.getMessage() + " " +e.getClass().getName());
                    Log.d(TAG, "onClick: cannot delete data from database");
                    customProgressDialog.dismissDialog();
                    Log.d(TAG, "onClick: " + requestDataList.size());
                }
                
            }
        });
    }

    @Override
    public int getItemCount() {

        return requestDataList.size();

    }

    public static class RequestAdapterViewHolder extends RecyclerView.ViewHolder {

        Button accept , decline;
        TextView name , reason , location , budget , profession , marriedStatus , requestType , numRooms;


        public RequestAdapterViewHolder(@NonNull View itemView) {

            super(itemView);

            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);

            name = itemView.findViewById(R.id.Name);
            reason = itemView.findViewById(R.id.reasonItem);
            location = itemView.findViewById(R.id.locationItem);
            budget = itemView.findViewById(R.id.budget);
            profession = itemView.findViewById(R.id.professionItem);
            marriedStatus = itemView.findViewById(R.id.married_status_item);
            requestType = itemView.findViewById(R.id.requestTypeItem);
            numRooms = itemView.findViewById(R.id.numRoomItem);
        }
    }

    // all the functions used by adapter.
    private void deleteUser(RequestData data) {

//        Log.d(TAG, "deleteUser: delete user called");
//        databaseReference = FirebaseDatabase.getInstance().getReference("RequestData");
//        databaseReference = databaseReference.child(sellerid).child(userid);
//        databaseReference.addValueEventListener(listener);

        databaseReference = FirebaseDatabase.getInstance().getReference("RequestData");
        databaseReference = databaseReference.child(data.getSellerID()).child(data.getUserID());

        Log.d(TAG, "deleteUser: called");

        databaseReference.addListenerForSingleValueEvent(listener);
    }

    private void shiftDataToNewTable(final RequestData requestData) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ApprovedRequests");
        databaseReference = databaseReference.child(requestData.getSellerID()).child(requestData.getUserID());
        String key = databaseReference.push().getKey();
        if (key != null) {
            databaseReference = databaseReference.child(key);

            databaseReference.setValue(requestData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d(TAG, "onSuccess: data updated to the new table");
                    // setting myCustomer and transaction history table

                    SettingMyTransactionData(requestData);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "OnFailure: " + e.getClass().getName() + " " + e.getMessage());
                }
            });
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SettingMyTransactionData(final RequestData requestData) {

        Log.d(TAG, "SettingMyTransactionData: called");

        myTransaction = FirebaseDatabase.getInstance().getReference("MyTransaction");
        myTransaction = myTransaction.child(requestData.getUserID());
        String key = myTransaction.push().getKey();

        if (key != null) {

            myTransaction = myTransaction.child(key);

            Log.d(TAG, String.format("SettingMyTransactionData: %s", myTransaction.getPath()));
            TransactionDataInitializer(requestData);

        }else
            Log.d(TAG, "SettingMyTransactionData: key returned null");


    }

    private void SettingMyCustomerData(final RequestData requestData) {

        myCustomerDataInitializer(requestData);

    }

    private void removeDataAfterAccept()
    {
        try {

            Log.d(TAG, "onClick: deleting user");
            data = requestDataList.get(position_data);
            user = userData.get(position_data);
            deleteUser(data);
            Log.d(TAG, "removeData: all set");

            deletePropertyData();

        }catch (Exception e)
        {
            Log.d(TAG, "onClick: "+e.getMessage() + " " +e.getClass().getName());
            Log.d(TAG, "onClick: cannot delete data from database");
            customProgressDialog.dismissDialog();
            Log.d(TAG, "onClick: " + requestDataList.size());
        }
    }

    private void myCustomerDataInitializer(RequestData requestData) {

        databaseReference4 = FirebaseDatabase.getInstance().getReference("UserData");
        databaseReference4 = databaseReference4.child(requestData.getSellerID());
        databaseReference4.addValueEventListener(listener_for_customer_data_initializer);



    }


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void TransactionDataInitializer(RequestData requestData) {

        Log.d(TAG, "TransactionDataInitializer: called");

        databaseReference3 = FirebaseDatabase.getInstance().getReference("SellerData");
        databaseReference3 = databaseReference3.child(requestData.getSellerID());
        Log.d(TAG, String.format("TransactionDataInitializer: %s", databaseReference3.getPath()));
        databaseReference3.addListenerForSingleValueEvent(listener_for_transaction_data_initializer);

    }

}
