package AddressAndMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.selflearn.alwarrenter.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class
TakingAddressActivity extends AppCompatActivity
{

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "TakingAddressActivity";
    Button  saveBtn;
    Button getCurrentLocationBtn;
    Address address_by_user;
    private static final int request_code = 1;
    String Address ;
    EditText pincode , house_info , road_or_area_info , city_info , state_info , landmark_info ;
    String pinCode , houseInfo , roadOrAreaInfo , cityInfo , stateInfo , landInfo;
    float lattitude,  longitude;
    private LatLng latLng_data ;


    private View.OnClickListener listener_for_getLocationBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isServicesOK())
            {
                Intent intent = new Intent(TakingAddressActivity.this , MapActivity.class);
                startActivityForResult(intent , request_code);
            }else {
                Log.d(TAG, "onClick: services are not good");
            }

        }
    };

    private View.OnClickListener listener_for_save_details = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent data = new Intent();

            data.putExtra("address" , houseInfo + " "+roadOrAreaInfo +" " + cityInfo +" "+ stateInfo +" "+ (landInfo!=null?landInfo:"") +" "+ pinCode);
            setResult(RESULT_OK,data);
            finish();
        }
    };
    private Object marker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_address_or_crrenent_address);

        findId();
        setListeners();

//        address_by_user.setPostalCode(pinCode);
//        address_by_user.setCountryCode("IN");
//        address_by_user.setFeatureName(landInfo);
//        address_by_user.setAddressLine(0,houseInfo);
//        address_by_user.setAddressLine(1, roadOrAreaInfo);
//        address_by_user.setAddressLine(2 , cityInfo);
//        address_by_user.setAddressLine(4 , stateInfo);
//

    }

    private void setListeners() {
        saveBtn.setOnClickListener(listener_for_save_details);
        getCurrentLocationBtn.setOnClickListener(listener_for_getLocationBtn);
    }

    private void findId() {
        getCurrentLocationBtn = findViewById(R.id.get_current_location_btn1);
        saveBtn = findViewById(R.id.saveBtn_location1);
        pincode = findViewById(R.id.pin_code);
        house_info = findViewById(R.id.building_info);
        road_or_area_info = findViewById(R.id.road_name_area_colony);
        city_info = findViewById(R.id.city);
        state_info = findViewById(R.id.state);
        landmark_info = findViewById(R.id.landmark);
    }

    @SuppressLint("LongLogTag")
    public boolean isServicesOK() {
        Log.d(TAG , "checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS)
        {
            // everything is fine and user can make map request
            Log.d(TAG , "google play services are working");
            return  true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            // error can be resolved

            Log.d(TAG, "isServicesOK: we can resolve the error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(TakingAddressActivity.this , available , ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext() , "we cannot make a map request ", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == request_code)
        {
            if(resultCode == RESULT_OK)
            {
                 lattitude = Float.parseFloat(Objects.requireNonNull(Objects.requireNonNull(data).getStringExtra("lat")));
                 longitude = Float.parseFloat(Objects.requireNonNull(data.getStringExtra("long")));

                Log.d(TAG, "onActivityResult: " +String.valueOf(lattitude) + " " + String.valueOf(longitude));

                latLng_data = new LatLng(lattitude , longitude);

                setValueInForm();

            }
        }
                super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("SetTextI18n")
    private void setValueInForm() {
        if(latLng_data != null)
        {
            Geocoder geocoder = new Geocoder(TakingAddressActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng_data.latitude, latLng_data.longitude, 3);
                if (addresses.size() > 0)
                {
//                    marker =map.addMarker(new MarkerOptions().position(latLng_data).title(addresses.get(0).getAddressLine(0)));
//                    markkedAddress = addresses.get(0).getAddressLine(0);

                    pinCode = addresses.get(0).getPostalCode();
                    pincode.setText(pinCode);

                    String str  = addresses.get(0).getAddressLine(0);
                    List<String> list = Arrays.asList(str.split(","));
                    houseInfo  = list.get(0);
                    house_info.setText("house number "+ houseInfo);

                    roadOrAreaInfo = list.get(1);
                    if(!roadOrAreaInfo.trim().equals(addresses.get(0).getSubLocality().trim()))
                    road_or_area_info.setText(roadOrAreaInfo + addresses.get(0).getSubLocality());
                    else
                        road_or_area_info.setText(roadOrAreaInfo );

                    cityInfo = addresses.get(0).getLocality();
                    city_info.setText(cityInfo);

                    stateInfo = addresses.get(0).getAdminArea();
                    state_info.setText(stateInfo);



                  ;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
