package User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.selflearn.alwarrenter.R;

import java.util.Objects;

import User.ActivityFragments.HomeActivity;
import User.ActivityFragments.ProfileActivity;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class BottomNavActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;
    Fragment selected = null;
    String account_type;
    CustomProgressDialog customProgressDialog;
    private static final String TAG = "BottomNavActivity";

    @Override
    protected void onStart() {

        super.onStart();

        customProgressDialog.startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.dismissDialog();
            }
        },1500);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_user);

        Log.d(TAG, "onCreate: " + new SettingMemoryData(BottomNavActivity.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)));

        account_type = Objects.requireNonNull(getIntent().getStringExtra(String.valueOf(R.string.account_type_key)));

        bottomNavigationViewEx = findViewById(R.id.Bottom_Navigation);
        customProgressDialog = new CustomProgressDialog(BottomNavActivity.this);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener( listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , new HomeActivity(account_type , BottomNavActivity.this)).commit();

        Menu menu =bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



            switch (menuItem.getItemId())
            {
                case R.id.home:

                    selected = new HomeActivity(account_type , BottomNavActivity.this);
                    break;
                case R.id.profile:

                    selected = new ProfileActivity(BottomNavActivity.this , account_type);
                    break;
            }

            assert selected != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , selected).commit();

            return true;
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(selected instanceof ProfileActivity)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , new HomeActivity(account_type , BottomNavActivity.this)).commit();

            Menu menu =bottomNavigationViewEx.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
        super.onDestroy();


    }
}