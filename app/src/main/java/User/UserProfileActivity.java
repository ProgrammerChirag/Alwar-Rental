package User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.selflearn.alwarrenter.R;

import Utils.UserBottomNavigationHelper;

public class UserProfileActivity extends AppCompatActivity {

    int Activity_id=1;

    ImageView imageView ;

    BottomNavigationViewEx bottomNavigationViewEx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        setBottomNavigationMenu();

        imageView = findViewById(R.id.certified_icon);
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        UserBottomNavigationHelper.onBackPressedButton(UserProfileActivity.this , Activity_id);

    }

    private void setBottomNavigationMenu() {

        bottomNavigationViewEx=findViewById(R.id.bottom_Navigation_view_Ex);
        UserBottomNavigationHelper.enableNavigation(UserProfileActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }
}
