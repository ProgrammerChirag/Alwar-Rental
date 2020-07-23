package com.selflearn.alwarrenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import Admin.AdminLoginActivity;

public class ActivityChooseLoginMethod extends AppCompatActivity {

    CardView Login_with_Email_button;
    CardView Login_with_Phone_number;

    String account_type;


    @Override
    protected void onStart() {
        super.onStart();

        account_type = getIntent().getStringExtra("account_type");

        assert account_type != null;
        if(account_type.equals("mainuser")){
            Log.d("progress","executing");
            finish();
            Intent intent = new Intent(ActivityChooseLoginMethod.this , AdminLoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_input_method);

        FindID();

         account_type = getIntent().getStringExtra("account_type");


        Login_with_Email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_type == null){
                    Log.d("error","connot get Account type");
                }else{
                    switch (account_type) {
                        case "user":

                            Utils.ActivityNavigation.openUserLoginWithEmialActivity(ActivityChooseLoginMethod.this);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        case "mainuser":

                            //startActivity(new Intent(ChooseLoginTypeActivity.this , MainActivity.class));
                            //Utils.ActivityNavigation.openAdmin(ActivityChooseLoginMethod.this);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            //finish();
                            //Intent intent = new Intent(ActivityChooseLoginMethod.this, MainUserLoginActivity.class);

                            break;
                        case "seller":

                            Utils.ActivityNavigation.openSellerLoginWithEmailActivity(ActivityChooseLoginMethod.this);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });

        Login_with_Phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_type == null){
                    Log.d("error","connot get Account type");
                }else{
                    switch (account_type) {
                        case "user":

                            Utils.ActivityNavigation.openUserLoginWithPhoneActivity(ActivityChooseLoginMethod.this , account_type);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        case "mainuser":

                            //startActivity(new Intent(ChooseLoginTypeActivity.this , MainActivity.class));
                            //Utils.ActivityNavigation.openAdmin(ActivityChooseLoginMethod.this);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            finish();
                            //Intent intent = new Intent(ActivityChooseLoginMethod.this , MainUserLoginActivity.class);


                            break;
                        case "seller":

                            Utils.ActivityNavigation.openSellerLoginWithPhoneActivity(ActivityChooseLoginMethod.this , account_type);
                            //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
    }

    private void FindID() {
        Login_with_Email_button = findViewById(R.id.login_with_email_button);
        Login_with_Phone_number = findViewById(R.id.login_with_number_button);
    }
}
