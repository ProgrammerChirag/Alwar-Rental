package com.selflearn.alwarrenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;


public class ChooseLoginTypeActivity extends AppCompatActivity {

    Button Seller , User , NextBtn;
    boolean USER_SELECTED = true;
    private static final String TAG = "ChooseLoginTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_type);

        findID();
        setListeners();

    }

    private void setListeners() {

        Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Seller.setBackground(getDrawable(R.drawable.btn_style2));
                Seller.setTextColor(Color.WHITE);
                User.setBackground(getDrawable(R.drawable.btn_style6));
                User.setTextColor(Color.BLACK);

                USER_SELECTED=false;
            }
        });

        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                USER_SELECTED = true;
                Seller.setBackground(getDrawable(R.drawable.btn_style6));
                Seller.setTextColor(Color.BLACK);
                User.setBackground(getDrawable(R.drawable.btn_style2));
                User.setTextColor(Color.WHITE);

            }
        });

        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (USER_SELECTED)
                {
                    Log.d(TAG, "onClick: user button is selected and getting start user dashboard");
                    new SettingMemoryData(ChooseLoginTypeActivity.this).setSharedPrefString(String.valueOf(R.string.account_type_key) , "user" );
                    new CustomDialogMaker(ChooseLoginTypeActivity.this).createAndShowDialogSuccess("login completed" , "user");

                }else {

                    Log.d(TAG, "onClick: seller button is selected and getting start seller dashboard");
                    new SettingMemoryData(ChooseLoginTypeActivity.this).setSharedPrefString(String.valueOf(R.string.account_type_key) , "seller" );
                    new CustomDialogMaker(ChooseLoginTypeActivity.this).createAndShowDialogSuccess("login completed" , "seller");

                }

            }
        });

    }


    private void findID() {

        Seller = findViewById(R.id.seller);
        User = findViewById(R.id.user);
        NextBtn = findViewById(R.id.BtnNext);

    }

    @Override
    protected void onDestroy() {
        new CustomProgressDialog(ChooseLoginTypeActivity.this).dismissDialog();
        Log.d(TAG, "onDestroy: "+"activity closed");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: "+"activity started");
        super.onStart();
    }

    @Override
    protected void onStop() {

        Log.d(TAG, "onStop: "+"Activity stopped");
        super.onStop();
    }

    @Override
    protected void onRestart() {

        Log.d(TAG, "onRestart: "+"activity restarted");
        super.onRestart();
    }

    @Override
    protected void onResume() {

        Log.d(TAG, "onResume: "+"Activity resume");
        super.onResume();
    }
}