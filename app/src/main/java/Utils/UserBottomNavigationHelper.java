package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.selflearn.alwarrenter.R;

import java.util.logging.Handler;

import User.UserDashBoardHomeActivity;
import User.UserProfileActivity;

public class UserBottomNavigationHelper {

    static long back_pressed_times =0;
    static int TIME_INTERVAL =2000;

    public static void enableNavigation(final Context context , BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId())
            {
                case R.id.home:
                    if(! (((Activity)(context)) instanceof UserDashBoardHomeActivity)) {

                        Intent intent1 = new Intent(context, UserDashBoardHomeActivity.class);
                        ((Activity) (context)).finish();
                        ((Activity) (context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        context.startActivity(intent1);
                    }
                    break;
                case R.id.profile:

                    if(!( ((Activity)(context)) instanceof UserProfileActivity)) {

                        Intent intent2 = new Intent(context, UserProfileActivity.class);
                        ((Activity) (context)).finish();
                        ((Activity) (context)).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        context.startActivity(intent2);
                    }
                    break;
            }
                return false;
            }
        });
    }
    public static void onBackPressedButton(Context context , int activity_id)
    {
        switch (activity_id)
        {
            case 0:
                if (back_pressed_times+TIME_INTERVAL>System.currentTimeMillis())
                {
                    ((Activity)(context)).finish();
                }
                else
                {
                    Toast.makeText(context , "press again to exit",Toast.LENGTH_LONG).show();
                }
                back_pressed_times=System.currentTimeMillis();
                break;
            case 1:
                Intent intent2 = new Intent(context , UserDashBoardHomeActivity.class);
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                context.startActivity(intent2);
                break;
        }
    }
}
