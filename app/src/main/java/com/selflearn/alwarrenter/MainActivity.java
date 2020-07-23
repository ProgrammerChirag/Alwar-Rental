package com.selflearn.alwarrenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import AccountHandler.ActivityChooseLoginType;
import FirebaseConnectivity.ActivityAfterMobileAuth;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class MainActivity extends AppCompatActivity {

            CustomProgressDialog customProgressDialog;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, BackGroundConnectivity.class);
//        startService(intent);

        customProgressDialog = new CustomProgressDialog(MainActivity.this );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    // code for setting the value in shared preferences.
//            SharedPreferences sharedPreferences  = getSharedPreferences(String.valueOf(R.string.account_type_key), Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor =  sharedPreferences.edit();
//
//            editor.putString(String.valueOf(R.string.account_type_key), "seller");
//            editor.apply();

//                    SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.account_type_key), Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sharedPreferences.edit();

                    String account_type = new SettingMemoryData(MainActivity.this).getSharedPrefString(String.valueOf(R.string.account_type_key));
                    if (account_type == null) {
                        Log.d("details" , "you are getting null account type");
                        new CustomDialogMaker(MainActivity.this).createAndShowDialogWarning("session expired please login again");
//                        sharedPreferences.edit().remove(String.valueOf(R.string.account_type_key)).apply();
//                        finish();
                        startActivity(new Intent(MainActivity.this, ActivityChooseLoginType.class));
                    } else {
                        switch (account_type) {
                            case "user":
//                                customProgressDialog = new CustomProgressDialog(MainActivity.this);
//                                customProgressDialog.startLoadingDailog();
                                String name = new SettingMemoryData(MainActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_NAME));
                                String number = new SettingMemoryData(MainActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_PHONE_NUMBER));
                                if (name == null && number == null)
                                {
                                    Log.d("result" , "both are null");
                                    FirebaseAuth.getInstance().signOut();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, ChooseLoginTypeActivity.class));
                                }
                                else
                                    if(name == null){
                                        Log.d("details"  , "only name is null");
                                        Intent intent =new Intent(MainActivity.this , ActivityAfterMobileAuth.class);
                                        intent.putExtra(String.valueOf(R.string.account_type_key) , account_type);
                                        finish();
                                        startActivity(intent);
                                    }
                                    else Utils.ActivityNavigation.openUserActivity(MainActivity.this,account_type);
                                break;
                            case "seller":
//                                customProgressDialog.startLoadingDailog();
                                Utils.ActivityNavigation.startSellerDashBoard(MainActivity.this,account_type);
                                break;
                            case "mainuser":
                                Utils.ActivityNavigation.openAdmin(MainActivity.this);
                                break;
                            default:
                                new CustomDialogMaker(MainActivity.this).createAndShowDialogWarning("something went wrong");
                                finish();
//                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                break;
                        }
                    }

                } else {
                    Log.d("details" , "current user is null");
                    Toast.makeText(getApplicationContext() , "starting" , Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, ActivityChooseLoginType.class));
                }
            }
        },1000);

    }

//    private void reload() {
//        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        customProgressDialog.dismissDialog();
//        customProgressDialog = null;
        super.onStop();
    }
}