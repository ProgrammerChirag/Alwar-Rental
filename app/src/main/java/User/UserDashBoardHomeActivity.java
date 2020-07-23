package User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.selflearn.alwarrenter.R;

import Utils.UserBottomNavigationHelper;

public class UserDashBoardHomeActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;
    int Activity_id =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        setBottomNavigationMenu();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        UserBottomNavigationHelper.onBackPressedButton(UserDashBoardHomeActivity.this  , Activity_id);
    }

    private void setBottomNavigationMenu() {
        bottomNavigationViewEx=findViewById(R.id.Bottom_Navigation_view_Ex);
        UserBottomNavigationHelper.enableNavigation(UserDashBoardHomeActivity.this,bottomNavigationViewEx);

        Menu menu =bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }
}