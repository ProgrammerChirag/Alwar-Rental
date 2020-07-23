package Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.selflearn.alwarrenter.R;

import AddressAndMap.TakingAddressActivity;
import ModelClasses.PropertyData;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ActivityPostNewProperty extends AppCompatActivity {

    RadioGroup numBHK , area , background , gender , purchaseType;
    EditText numRooms , cost , contactNumber , address;
    private static final String TAG = "ActivityPostNewProperty";
    private boolean IS_FORM_VALIDATED = false;
    CustomProgressDialog customProgressDialog;
    int numImage;

    String NunBHK , Area , Background , Gender , PurchaseType , NumRooms , Cost , ContactNumber , Address;

    Button next;
    private int REQUEST_CODE = 100;
    PropertyData propertyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_property);

        customProgressDialog = new CustomProgressDialog(ActivityPostNewProperty.this);

        findID();
    }

    private void findID() {

        numBHK = findViewById(R.id.numBHK);
        if (numBHK == null)
            Log.d(TAG, "findID: returned null");
        area  = findViewById(R.id.area);
        background = findViewById(R.id.background);
        gender = findViewById(R.id.gender);
        purchaseType = findViewById(R.id.purchaseType);

        numRooms = findViewById(R.id.numRoom);
        cost = findViewById(R.id.cost);
        contactNumber = findViewById(R.id.contactNum);
        address = findViewById(R.id.address_property);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValidated()){
                    getDetailAndChangeActivity();
                }
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPostNewProperty.this , TakingAddressActivity.class);
                startActivityForResult(intent , 1234);
            }
        });

    }

    private void getDetailAndChangeActivity() {

        RadioButton selectedRadioButton ;
        int id  = numBHK.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(id);
        NunBHK = selectedRadioButton.getText().toString().trim();

        Log.d(TAG, "getDetailAndChangeActivity: "+ NunBHK);

        id = area.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(id);
        Area = selectedRadioButton.getText().toString().trim();

        Log.d(TAG, "getDetailAndChangeActivity: "+Area);

        id = background.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(id);
        Background = selectedRadioButton.getText().toString().trim();

        Log.d(TAG, "getDetailAndChangeActivity: "+Background);

        id = gender.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(id);
        Gender = selectedRadioButton.getText().toString().trim();

        Log.d(TAG, "getDetailAndChangeActivity: "+Gender);

        id = purchaseType.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(id);
        PurchaseType = selectedRadioButton.getText().toString().trim();

        Log.d(TAG, "getDetailAndChangeActivity: "+PurchaseType);

        changeActivity();

    }

    private void changeActivity() {
        Intent intent = new Intent(ActivityPostNewProperty.this , UploadPropertyImage.class);
//
//        intent.putExtra(String.valueOf(R.string.numBHK) , NunBHK);
//        intent.putExtra(String.valueOf(R.string.area) , Area);
//        intent.putExtra(String.valueOf(R.string.background) , Background);
//        intent.putExtra(String.valueOf(R.string.gender) , Gender);
//        intent.putExtra(String.valueOf(R.string.purchase_type) , PurchaseType);
//        intent.putExtra(String.valueOf(R.string.numRooms) , NumRooms);
//        intent.putExtra(String.valueOf(R.string.cost) , Cost);
//        intent.putExtra(String.valueOf(R.string.contactNumber) , ContactNumber);
//        intent.putExtra(String.valueOf(R.string.address) , Address);
//
//        finish();

        startActivityForResult(intent , REQUEST_CODE);

        }

    private boolean isFormValidated() {
        NumRooms = numRooms.getText().toString().trim();
        Cost = cost.getText().toString().trim();
        ContactNumber = contactNumber.getText().toString().trim();
        Address = address.getText().toString().trim();

        if(!NumRooms.isEmpty() && !ContactNumber.isEmpty() && !Cost.isEmpty() && !Address.isEmpty())
        {
            IS_FORM_VALIDATED = true;
        }else if (NumRooms.isEmpty())
        {
            numRooms.setError("please enter how many rooms you have");
            numRooms.requestFocus();
        }else if (Cost.isEmpty()){
            cost.setError("please enter price of your property");
            cost.requestFocus();
        }else if (ContactNumber.isEmpty())
        {
            contactNumber.setError("please enter your number for contact");
            contactNumber.requestFocus();
        }else if (address.getText().toString().isEmpty()){
            address.setError("please enter address of your property");
            address.requestFocus();
        }else
        {
            IS_FORM_VALIDATED = true;
        }

        return  IS_FORM_VALIDATED;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                if (data != null) {
                    try {
                        numImage = data.getIntExtra("numImage", 0);
                        String dirName = data.getStringExtra("dirName");
                        if (customProgressDialog != null)
                            customProgressDialog.startLoadingDailog();
                            uploadDataToDataBase(dirName);
                    }catch (Exception e)
                    {
                        Log.d(TAG, "onActivityResult: error");
                        Log.d(TAG, "onActivityResult: "+e.getMessage() + " " + e.getClass().getName());
                    }
                }else
                    Log.d(TAG, "onActivityResult: no data found");

            }
        }else if (requestCode == 1234)
        {
            if (data !=null) {
                Address = data.getStringExtra("address");
                address.setText(Address);
                }
            }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadDataToDataBase(String dirName) {

        try {
            propertyData = new PropertyData();

            propertyData.setAddressProperty(Address);
            propertyData.setArea(Area);
            propertyData.setBackground(Background);
            propertyData.setCost(Cost);
            propertyData.setEmail(new SettingMemoryData(ActivityPostNewProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_EMAIL)));
            propertyData.setGenderSeller(Gender);
            propertyData.setNumImages(String.valueOf(numImage));
            propertyData.setNumberToContact(ContactNumber);
            propertyData.setNumBHk(NunBHK);
            propertyData.setNumRooms(NumRooms);
            propertyData.setSellerId(new SettingMemoryData(ActivityPostNewProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)));
            propertyData.setPurchaseType(PurchaseType);
            propertyData.setSellerName(new SettingMemoryData(ActivityPostNewProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_NAME)));


//        Date today = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//        String dateToStr = format.format(today);

            propertyData.setDateUpload(dirName);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PropertyData");

            databaseReference = databaseReference.child((new SettingMemoryData(ActivityPostNewProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID))));

            Log.d(TAG, "uploadDataToDataBase: "+dirName);
            Log.d(TAG, "uploadDataToDataBase: "+new SettingMemoryData(ActivityPostNewProperty.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)));

            databaseReference = databaseReference.child(dirName);
            databaseReference.setValue(propertyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: data uploaded");
//                customProgressDialog.dismissDialog();

                    if (customProgressDialog != null)
                    customProgressDialog.startLoadingDailog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                        customProgressDialog.dismissDialog();
                            Utils.ActivityNavigation.startSellerDashBoard(ActivityPostNewProperty.this
                                    , "seller");
                        }
                    }, 5000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getClass().getName() + " " + e.getMessage());
                }
            });

        }catch (Exception e)
        {
            Log.d(TAG, "uploadDataToDataBase: error");
            Log.d(TAG, "uploadDataToDataBase: "+e.getMessage() +" " +e.getClass().getName());
        }
    }

    @Override
    protected void onPause() {
        customProgressDialog.dismissDialog();
        Log.d(TAG, "onPause: "+"post new property activity paused");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        customProgressDialog.dismissDialog();
        Log.d(TAG, "onDestroy: "+"post property activity closed and dismissing dialog ");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: "+ "activity restarting");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: "+"activity resuming");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        finish();
//        startActivity();
        Utils.ActivityNavigation.startSellerDashBoard(ActivityPostNewProperty.this , "seller" );
//        super.onBackPressed();
    }

    @Override
    protected void onStart() {

        Log.d(TAG, "onStart: "+"activity started");

        super.onStart();
    }

    @Override
    public void finish() {
        Log.d(TAG, "finish: "+"activity finishing");

        super.finish();
    }
}