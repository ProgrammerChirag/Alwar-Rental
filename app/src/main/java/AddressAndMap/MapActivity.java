package AddressAndMap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.selflearn.alwarrenter.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivityProgress";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    Button Done;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    // variables
    private boolean IS_PERMISSON_GRANTED = false;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private float defaultZoom = 15f;
    LatLng latLng_data;
    private String markkedAddress ;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Done = findViewById(R.id.done);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markkedAddress != null && latLng_data != null)
                {
                    Intent data = new Intent();
                    data.putExtra("lat", String.valueOf(latLng_data.latitude));
                    data.putExtra("long",String.valueOf(latLng_data.longitude));
                    setResult(RESULT_OK,data);
                    finish();
                }
            }
        });

        getLocationPermission();



    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permission from user");

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                IS_PERMISSON_GRANTED = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: Called");

        IS_PERMISSON_GRANTED = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            IS_PERMISSON_GRANTED = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    IS_PERMISSON_GRANTED = true;
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");

                    // initialize our map .

                    initMap();
                }
                break;
        }
    }


    private void initMap() {
        SupportMapFragment mapFragment = Objects.requireNonNull((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(MapActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latLng_data = latLng;
                moveCamera(latLng, 18);
                if(marker != null)
                    marker.remove();
//                Location location_by_user = new Location(LocationManager.GPS_PROVIDER);
//                location_by_user.setLatitude(latLng.latitude);
//                location_by_user.setLongitude(latLng.longitude);
//                String title = location_by_user.toString();
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 3);
                    if (addresses.size() > 0)
                    {
                        marker =map.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0)));
                        markkedAddress = addresses.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        Toast.makeText(getApplicationContext(), "here is your location ", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onMapReady: Map is fully ready");

        if (IS_PERMISSON_GRANTED) {
            getDevicesLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);

        }
    }

    private void getDevicesLocation(){
        Log.d(TAG, "getDevicesLocation: getting devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (IS_PERMISSON_GRANTED)
            {
                final Task<Location> location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: location found");
                            Location currentLocation = (Location) task.getResult();
                            latLng_data = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            // storing user location in string
                            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 3);
                                if (addresses.size() > 0)
                                {
                                    marker =map.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title(addresses.get(0).getAddressLine(0)));
                                    markkedAddress = addresses.get(0).getAddressLine(0);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // move camera to the current location.
                            moveCamera(new LatLng(currentLocation.getLatitude() ,currentLocation.getLongitude()) , defaultZoom);
                        }else{
                            Log.d(TAG, "onComplete: current location not found please turn on your location or try again");
                            Toast.makeText(getApplicationContext() , "unable to get location" , Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }catch (SecurityException se)
        {
            Log.d(TAG, "getDevicesLocation: security exception"+se.getMessage() + " " +se.getClass().getName());
        }

    }

    private void moveCamera(LatLng latLng  , float zoom)
    {
        Log.d(TAG, "moveCamera: moving camera to lat :" + latLng.latitude + " , long: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng  , zoom));


    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int X = (int) event.getX();
//        int Y = (int) event.getY();
//
//        moveCamera(new LatLng(X, Y) , defaultZoom);
//        return super.onTouchEvent(event);
//
//    }
}