package User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.selflearn.alwarrenter.ChooseLoginTypeActivity;
import com.selflearn.alwarrenter.R;

import Utils.CustomDialogMaker;
import Utils.SettingMemoryData;

public class ActivitySetting extends AppCompatActivity {

    ImageButton backBtnSetting;
    Button EditProfile , ChangePassword , SignOut , Notification , PrivacyAndPolicy , About , BecomeASeller;

    View.OnClickListener backBtnListener , EditProfileListener , ChangePassListener , SignOutListener, NotificationListener , privacyAndPolicyListener ,
                        AboutListener , BecomeASellerListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_ui);

        initListeners();
        findId();


    }

    private void initListeners() {

        backBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ActivitySetting.this , BottomNavActivity.class);
                intent.putExtra(String.valueOf(R.string.account_type_key), "user");
                startActivity(intent);
            }
        };

        EditProfileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ActivitySetting.this , ActivityUserEditProfile.class);
                startActivity(intent);
            }
        };

        ChangePassListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change password activity
                new CustomDialogMaker(ActivitySetting.this).EnterCurrentPassword("UserData");

            }
        };

        SignOutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
                finish();
                startActivity(new Intent(ActivitySetting.this, ChooseLoginTypeActivity.class));
            }
        };

        NotificationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notification
            }
        };

        privacyAndPolicyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // open privacy and policy
            }
        };

        AboutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open about ui
            }
        };

        BecomeASellerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seller user shifter Activity
                new SettingMemoryData(ActivitySetting.this).setSharedPrefString(String.valueOf(R.string.account_type_key), "seller");
                Utils.ActivityNavigation.startSellerDashBoard(ActivitySetting.this , "seller");

            }
        };
    }

    private void findId() {

        backBtnSetting = findViewById(R.id.backBtn_setting);
        backBtnSetting.setOnClickListener(backBtnListener);
        EditProfile = findViewById(R.id.edit_profile_setting);
        EditProfile.setOnClickListener(EditProfileListener);
        ChangePassword = findViewById(R.id.change_password_setting);
        ChangePassword.setOnClickListener(ChangePassListener);
        SignOut = findViewById(R.id.sign_out_setting);
        SignOut.setOnClickListener(SignOutListener);
        Notification = findViewById(R.id.notification_setting);
        Notification.setOnClickListener(NotificationListener);
        PrivacyAndPolicy = findViewById(R.id.privacy_policy);
        PrivacyAndPolicy.setOnClickListener(privacyAndPolicyListener);
        About = findViewById(R.id.about_setting);
        About.setOnClickListener(AboutListener);
        BecomeASeller = findViewById(R.id.Become_seller_button_setting);
        BecomeASeller.setOnClickListener(BecomeASellerListener);

    }
    private void SignOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //deleting all shared preferences

        new SettingMemoryData(ActivitySetting.this).removeSharedPref();

        firebaseAuth.signOut();

    }
}
