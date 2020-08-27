package User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.selflearn.alwarrenter.R;

import java.util.Objects;

import AddressAndMap.TakingAddressActivity;
import FirebaseConnectivity.UploadData;
import ModelClasses.RequestData;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class ActivityPropertyForRentUI extends AppCompatActivity {

    Button submit ;
    EditText name , reason , numRoom , location , budget;
    Spinner spinner_profession , spinner_gender , spinner_married_status;
    String requestType;
    String  Name , Reason , Location;
    Double  Budget;
    Integer NumRoom ;
    ImageView backBtn;
    String Cost , dateUpload;
    String sellerID;
    String  Profession , Gender , Married_Status ;
    String numRooms , Address;

    boolean IS_VALIDATED = false;
    private int request_code = 1;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityPropertyForRentUI.this , BottomNavActivity.class);
        intent.putExtra(String.valueOf(R.string.account_type_key), "user");
        finish();
        startActivity(intent);
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_u_i);

        requestType = getIntent().getStringExtra("requestType");
        sellerID = getIntent().getStringExtra(String.valueOf(R.string.KEY_USER_ID));
        Cost = getIntent().getStringExtra(String.valueOf(R.id.cost));
        dateUpload = getIntent().getStringExtra(String.valueOf(R.id.purchaseDate));
        if (requestType != null)
        Log.d("requestType",requestType);

        findID();
        // setting default data
        numRooms = Objects.requireNonNull(getIntent().getStringExtra(String.valueOf(R.string.numRooms)));
        numRoom.setText(numRooms);
        numRoom.setEnabled(false);
        numRoom.setFocusable(false);
        numRoom.setClickable(false);

        Address = Objects.requireNonNull(getIntent().getStringExtra(String.valueOf(R.string.address)));
        location.setText(Address);
        location.setClickable(false);
        location.setEnabled(false);
        location.setFocusable(false);

        setSpinner();
        setListener();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setListener() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()){
                    sendDataToDataBase(requestType , sellerID);
                    changeActivityToCongratulationPage();
                }
            }
        });

    }

    private void changeActivityToCongratulationPage() {
        // changing activity to congratulation page.

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(ActivityPropertyForRentUI.this);
        customProgressDialog.startLoadingDailog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                customProgressDialog.dismissDialog();
                finish();
                Intent intent = new Intent(ActivityPropertyForRentUI.this , CongoUI.class);
                startActivity(intent);
            }
        },3000);
    }

    private void sendDataToDataBase(String requestType, String sellerID) {

        //sending data to the database.
        UploadData uploadData = new UploadData(ActivityPropertyForRentUI.this);
        RequestData requestData = new RequestData(Name , Reason , String.valueOf(NumRoom) , Location , String.valueOf(Budget) ,
                                                Profession , Married_Status , Gender , requestType , "pending" , Cost , dateUpload , sellerID ,
                new SettingMemoryData(ActivityPropertyForRentUI.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID))
                );

        uploadData.UploadDataRequest(requestData, sellerID);


    }

    private boolean validateForm() {

        if(!name.getText().toString().trim().isEmpty() && !reason.getText().toString().trim().isEmpty() &&
                !location.getText().toString().trim().isEmpty() && !numRoom.getText().toString().isEmpty()&&
                !budget.getText().toString().isEmpty()
        ) {

            Name = name.getText().toString().trim();
            Reason = reason.getText().toString().trim();
            Location = location.getText().toString().trim();
            NumRoom = Integer.valueOf(numRoom.getText().toString());
            Budget = Double.valueOf(budget.getText().toString());
        }


        if (!numRoom.getText().toString().trim().equals(""))
        NumRoom = Integer.parseInt(numRoom.getText().toString().trim());

        if (!budget.getText().toString().trim().equals(""))
        Budget = Double.valueOf(budget.getText().toString().trim());

        if( (!name.getText().toString().trim().isEmpty()) && (!reason.getText().toString().trim().isEmpty()) && (!location.getText().toString().trim().isEmpty())
                && (!numRoom.getText().toString().isEmpty()) && (!budget.getText().toString().isEmpty())
        ){
            IS_VALIDATED =  true;
        }else if(name.getText().toString().trim().isEmpty()){
            IS_VALIDATED = false;
            name.setError("please enter name");
            name.requestFocus();
        }else if(reason.getText().toString().trim().isEmpty()){
            IS_VALIDATED = false;
            reason.setError("please enter reason for buying");
            reason.requestFocus();
        }else if(location.getText().toString().trim().isEmpty()){
            IS_VALIDATED = false;
            location.setError("please choose the location first");
            location.requestFocus();
        }else if(numRoom.getText().toString().isEmpty()){
            IS_VALIDATED = false;
            numRoom.setError("please enter number of rooms");
            numRoom.requestFocus();
        }else if(budget.getText().toString().isEmpty()){
            IS_VALIDATED = false;
            budget.setError("please enter your budget");
            budget.requestFocus();
        }else
        {
            IS_VALIDATED = true;
        }

        return IS_VALIDATED;

    }

    private void setSpinner() {

        spinner_profession.setPrompt("choose your profession");

        ArrayAdapter<CharSequence> profession_array_adapter = ArrayAdapter.createFromResource(this,
                R.array.profssion ,android.R.layout.simple_spinner_item
                );
        profession_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_profession.setAdapter(profession_array_adapter);

        spinner_profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Profession = spinner_profession.getSelectedItem().toString();
                spinner_profession.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ArrayAdapter<CharSequence> gender_array_adapter = ArrayAdapter.createFromResource(this,
                R.array.gender , android.R.layout.simple_spinner_item
                );
        gender_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(gender_array_adapter);
        spinner_gender.setPrompt("Gender");

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Gender = spinner_gender.getSelectedItem().toString();
                spinner_gender.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<CharSequence> married_status_array_adapter = ArrayAdapter.createFromResource(this ,
                R.array.married_status , android.R.layout.simple_spinner_item
                );
        married_status_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_married_status.setAdapter(married_status_array_adapter);
        spinner_married_status.setPrompt("select your married status");

        spinner_married_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Married_Status = spinner_married_status.getSelectedItem().toString();
                spinner_married_status.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findID() {

        name = findViewById(R.id.user_name);
        reason = findViewById(R.id.reason);
        numRoom = findViewById(R.id.number_of_rooms);
        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPropertyForRentUI.this , TakingAddressActivity.class);
                startActivityForResult(intent , request_code);
            }
        });
        budget = findViewById(R.id.budget);
        submit = findViewById(R.id.SubmitBtn);
        backBtn = findViewById(R.id.backBtn);
        spinner_profession = findViewById(R.id.spinner_profession);
        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_married_status = findViewById(R.id.spinner_married_status);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (data != null) {
            String str = data.getStringExtra("address");
            location.setText(str);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

// phone number :-