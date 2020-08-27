package User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.selflearn.alwarrenter.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import ModelClasses.PropertyData;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ShowDetailsOfProperty extends AppCompatActivity  {

    Toolbar toolbar;
    List<Bitmap> list;
    FloatingActionButton sendRequest;
    TextView SellerName , NUMBHK , NUMROOMS , ADDRESS , BACKGROUND , AREA , COST , PROPERTYTYPE;
    CustomProgressDialog customProgressDialog;
    List<PropertyData> propertyDataList;
    ViewPager viewPager ;
    DotsIndicator indicator;
    String name;
    private static final String TAG = "ShowDetailsOfProperty";
    private List<Bitmap> bitmapList;
    int i;
    String sellerName;
    String numBHK , Address , numRooms , background , area , cost;
    DatabaseReference databaseReference;
    String propertyType;
    String sellerID;
    String dirName;
    List<String> names ;
    String dateUpload , Cost;
    List<String> maps ;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            maps = new ArrayList<>();
            for (DataSnapshot snapshot1 : snapshot.getChildren())
            {
                 maps.add(snapshot1.getValue(String.class));
            }

            if (maps != null) {
                Log.d(TAG, "onDataChange: map is not null ");
                Log.d(TAG, "onDataChange: "+maps.size());
//                for (String key : map.keySet())
//                {
//                    names.add(Objects.requireNonNull(map.get(key)).toString());
//                    Log.d(TAG, String.format("onDataChange: %s", Objects.requireNonNull(Objects.requireNonNull(map.get(key)).toString())));
//                }
            }
            else
                Log.d(TAG, "onDataChange: map us null");


//            Log.d(TAG, "onDataChange: "+storage.getPath());

            for (i=0 ; i < maps.size() ; i++)
            {
                StorageReference storage = FirebaseStorage.getInstance().getReference("SellerPropertyImages");
                storage = storage.child("seller");
                String uid = sellerName;
                storage = storage.child(uid);
                storage = storage.child(dirName);

                storage = storage.child(maps.get(i));

                Log.d(TAG, "onDataChange: "+storage.getPath());

                storage.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap;
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        bitmapList.add(bitmap);
                        Log.d(TAG, "onSuccess: "+bitmapList.size());

                        if (bitmapList.size() > 0)
                        {
                            ViewPagerAdapter adapter = new ViewPagerAdapter(ShowDetailsOfProperty.this , bitmapList);
                            viewPager.setAdapter(adapter);
                            indicator.setViewPager(viewPager);
                        }
                        else
                        {
                            Log.d(TAG, "onDataChange: size is 0");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage() + " "+e.getClass().getName());
                    }



                });

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, "onCancelled: "+error.getClass().getName() + " " + error.getMessage());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsingtoolbarfordetais);

//
//        intent.putExtra(String.valueOf(R.string.numBHK) , list.get(position).getNumBHk());
//        intent.putExtra(String.valueOf(R.string.purchase_type) , list.get(position).getPurchaseType());
//        intent.putExtra(String.valueOf(R.string.area) , list.get(position).getArea());
//        intent.putExtra(String.valueOf(R.string.address) , list.get(position).getAddressProperty());
//        intent.putExtra(String.valueOf(R.string.background) , list.get(position).getBackground());
//        intent.putExtra(String.valueOf(R.string.cost) , list.get(position).getCost());
//        intent.putExtra(String.valueOf(R.string.numRooms) , list.get(position).getNumRooms());


        findID();
        findData();
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this , list);
//        viewPager.setAdapter(adapter);
    }

//    private void findData() {

//        customProgressDialog.startLoadingDailog();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PropertyData");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                String  uid=  new SettingMemoryData(ShowDetailsOfProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));
//
//                propertyDataList = new ArrayList<>();
//
//                for (DataSnapshot snapshot1 : snapshot.getChildren())
//                {
////                    String username = snapshot1.getValue(String.class);
////                    if(uid != username)
////                    {
////                        for (DataSnapshot snapshot2 : snapshot1.getChildren())
////                        {
////                            if (snapshot2.getValue(PropertyData.class) != null) {
////                                if (Objects.requireNonNull(snapshot2.getValue(PropertyData.class)).getPurchaseType().equals(" On Rent")) {
////                                    requestDataList.add(snapshot2.getValue(PropertyData.class));
////                                }
////                            }
////                        }
////                    }
//
//                    PropertyData propertyData;
//                    for (DataSnapshot snapshot2 : snapshot1.getChildren())
//                    {
//                        propertyData = snapshot2.getValue(PropertyData.class);
//                        Log.d(TAG, "onDataChange: " + uid);
//                        if (propertyData != null) {
//                            Log.d(TAG, "onDataChange: "+propertyData.getSellerId());
//                        }
//                        Log.d(TAG, "onDataChange: property type"+propertyType);
//                        if (propertyData != null)
//                            Log.d(TAG, "onDataChange: "+propertyData.getSellerName());
//                        if (propertyData != null && !uid.equals(propertyData.getSellerId()) && propertyData.getPurchaseType().equals(propertyType))
//                            propertyDataList.add(propertyData);
//                    }
//                    Log.d(TAG, "onDataChange: "+ propertyDataList.size());
//
//                }
//
//                if (propertyDataList.size() != 0) {
////                    textView1.setVisibility(View.INVISIBLE);
////                recyclerView.setAdapter(new FindPropertyAdapter(ActivityFindRentalProperty.this , propertyDataList));
//                    getImages();
//                }
//                else {
//                    if (customProgressDialog != null)
//                        customProgressDialog.dismissDialog();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getClass().getName() + " " + error.getMessage());
//            }
//        });
//    }
//    private void getImages() {
//
//        for ( i=0 ; i <propertyDataList.size() ; i++) {
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerPropertyImages").child("seller");
//            databaseReference = databaseReference.child(propertyDataList.get(i).getSellerId());
//            Log.d(TAG, "getImages: "+propertyDataList.get(i).getSellerId() + propertyDataList.get(i));
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot snapshot1 : snapshot.getChildren())
//                    {
//                        map = (Map<String, Object>) snapshot1.getValue();
//
//                        Log.d(TAG, "onDataChange: loading image");
//
//                    }
//                    Log.d(TAG, "onDataChange: "+i);
//                    for (int j=0 ; j <i;j++)
//                        if (propertyDataList.size()>0)
//                            loadImage(map, propertyDataList.get(j).getSellerId(), propertyDataList.get(j).getDateUpload());
//                        else customProgressDialog.dismissDialog();
//                }
//
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.d(TAG, "onCancelled: "+error.getMessage() + " " +error.getClass().getName());
//                }
//            });
//
//            if (map!=null)
//            {
//                for (String key : map.keySet())
//                {
//                    Log.d(TAG, "onDataChange: "+map.get(key));
//                }
//            }
//        }
//
//    }
//
//    private void loadImage(Map<String, Object> map, String sellerId, String dateUpload) {
//
//        Log.d(TAG, "loadImage: " + sellerId + dateUpload);
//
//        if (!sellerId.equals(new SettingMemoryData(ShowDetailsOfProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)))) {
//            List<String> stringList = new ArrayList<>();
//
//            for (String key : map.keySet()) {
//                stringList.add(Objects.requireNonNull(map.get(key)).toString());
//                Log.d(TAG, "loadImage: "+ Objects.requireNonNull(map.get(key)).toString());
//            }
//
//            String url;
//            url = "gs://alwarrenter.appspot.com/SellerPropertyImages/seller" + "/" + sellerId + "/" + dateUpload + "/" + stringList.get(0);
//            Log.d(TAG, "loadImage: " + url);
//            Log.d(TAG, "loadImage: " + "gs://alwarrenter.appspot.com/SellerPropertyImages/seller/junejachirag022/2020-07-14 11:42:35 AM/3rb2tVgyKO");
//            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
////        storageReference = storageReference.child("SellerPropertyImages").child("seller").child(sellerId).child(dateUpload);
////        Log.d(TAG, "loadImage: "+stringList.get(0)+".jpeg");
////        storageReference = storageReference.child(stringList.get(0)+".jpeg");
////        Log.d(TAG, "loadImage: "+storageReference.child("jg").getPath());
//            storageReference.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bitmap;
//                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    bitmapList.add(bitmap);
//                    Log.d(TAG, "onSuccess: image load success");
//
//                    if (propertyDataList.size() >0) {
////                        recyclerView.setAdapter(new FindPropertyAdapter(ShowDetailsOfProperty.this, propertyDataList , bitmapList));
//
//                    }
//                    customProgressDialog.dismissDialog();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, "onFailure: "+e.getMessage());
//                }
//            });
//        }
//    }

    @SuppressLint("RestrictedApi")
    public void findData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("SellerPropertyImages");
        databaseReference = databaseReference.child("seller");
        databaseReference = databaseReference.child(sellerName);
        if (dirName != null) {
            databaseReference = databaseReference.child(dirName);
            Log.d(TAG, "findData: "+databaseReference.getPath());
            databaseReference.addValueEventListener(listener);
            Log.d(TAG, "findData: "+"waiting for calling getImage function");
        }
    }

    public void findID()
    {
        names = new ArrayList<>();
        propertyType = getIntent().getStringExtra(String.valueOf(R.string.purchase_type));
        dirName = getIntent().getStringExtra("dirName");
        sellerName = getIntent().getStringExtra("sellername");
        numBHK = getIntent().getStringExtra(String.valueOf(R.string.numBHK));
        numRooms = getIntent().getStringExtra(String.valueOf(R.string.numRooms));
        area = getIntent().getStringExtra(String.valueOf(R.string.area));
        Address = getIntent().getStringExtra(String.valueOf(R.string.address));
        background = getIntent().getStringExtra(String.valueOf(R.string.background));
        cost = getIntent().getStringExtra(String.valueOf(R.string.cost));
        name = getIntent().getStringExtra("Name");
        sellerID = getIntent().getStringExtra(String.valueOf(R.string.KEY_USER_ID));
        Cost = getIntent().getStringExtra(String.valueOf(R.id.cost));
        dateUpload = getIntent().getStringExtra(String.valueOf(R.id.purchaseDate));

        customProgressDialog = new CustomProgressDialog(ShowDetailsOfProperty.this);
        propertyDataList = new ArrayList<>();
        bitmapList= new ArrayList<>();

        list = new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.dot);

        SellerName = findViewById(R.id.seller_name);
        SellerName.setText(String.format("seller name : %s", name));
        NUMBHK = findViewById(R.id.numBHKItem);
        NUMBHK.setText(String.format("%s BHK HOUSE", numBHK));
        NUMROOMS = findViewById(R.id.numRoomItem);
        NUMROOMS.setText(String.format("Number Of Rooms : %s", numRooms));
        ADDRESS = findViewById(R.id.address_property_item);
        ADDRESS.setText(String.format("Address : %s", Address));
        PROPERTYTYPE = findViewById(R.id.property_type_item);
        PROPERTYTYPE.setText(String.format("property type : %s", propertyType));
        BACKGROUND = findViewById(R.id.backgroundItem);
        BACKGROUND.setText(String.format("with background : %s", background));
        AREA = findViewById(R.id.areaItem);
        AREA.setText(String.format("%s area", area));
        COST = findViewById(R.id.costItem);
        COST.setText(String.format("cost : %s", cost));

        sendRequest = findViewById(R.id.send_request);

        toolbar = findViewById(R.id.toolbar_collapsing);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new SettingMemoryData(ShowDetailsOfProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID))!= null) {

                    Intent intent = new Intent(ShowDetailsOfProperty.this, ActivityPropertyForRentUI.class);
                    intent.putExtra(String.valueOf(R.string.address), Address);
                    intent.putExtra(String.valueOf(R.string.numRooms), numRooms);
                    intent.putExtra("requestType", propertyType);
                    intent.putExtra(String.valueOf(R.string.KEY_USER_ID), sellerID);
                    intent.putExtra(String.valueOf(R.id.cost), Cost);
                    intent.putExtra(String.valueOf(R.id.purchaseDate), dateUpload);
                    startActivity(intent);
                }
                else {
                    Log.d(TAG, "onClick: account type is null or you are trying to access without account type please reset your memory or reinstall the app");
                    Toast.makeText(getApplicationContext() ,"no account type defined you are not able to send request" , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
