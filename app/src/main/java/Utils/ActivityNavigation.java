package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import FirebaseConnectivity.MobileAuthenticationActivity;

import Seller.SellerDashboardActivity;
import Seller.SellerLoginMobileAuth;
import User.BottomNavActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.selflearn.alwarrenter.ChooseLoginTypeActivity;
import com.selflearn.alwarrenter.R;

import Admin.AdminDashBoardActivity;
import Helper.ForgotPasswordHelper;
import Seller.SellerSignUpActivity;
import User.ChangePasswordActivity;
import User.UserLoginActivity;
import Seller.SellerLoginActivity;
import User.UserSignUpActivity;

import static android.content.ContentValues.TAG;

public class ActivityNavigation  {


    public  static void openSellerActivity(final Context context, final String account_type)
    {
        final Intent intent = new Intent(context, BottomNavActivity.class);
//        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
//
//            }
//        },2000);

        intent.putExtra(String.valueOf(R.string.account_type_key),account_type);
        ((Activity)(context)).finish();
        ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        context.startActivity(intent);

    }

    public static void openUserActivity(final Context context , String account_type)
    {

        final Intent intent = new Intent(context, BottomNavActivity.class);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
//
//            }
//        },2000);

        intent.putExtra(String.valueOf(R.string.account_type_key),account_type);
        ((Activity)(context)).finish();
        ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        context.startActivity(intent);
    }
    public static  void openAdmin(final Context context)
    {
        final Intent intent = new Intent(context, AdminDashBoardActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(intent);
            }
        },2000);

    }

    public static void openUserSignupActivity(final Context context)
    {
        Intent intent = new Intent(context, UserSignUpActivity.class);
        ((Activity)(context)).finish();
        context.startActivity(intent);
    }

    public static void openSellerSignUpActivity(final Context context)
    {
        Intent intent = new Intent(context, SellerSignUpActivity.class);
        ((Activity)(context)).finish();
        context.startActivity(intent);
    }

    public static void openUserLoginWithEmialActivity(final Context context)
    {
        final Intent intent = new Intent(context, UserLoginActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(intent);
            }
        },2000);
    }

    public static void openSellerLoginWithEmailActivity(final Context context)
    {

        final Intent intent = new Intent(context, SellerLoginActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(intent);
            }
        },2000);
    }

    public static void openForgotPassowrdActivity(final Context context ){

        Intent intent = new Intent(context, ForgotPasswordHelper.class);
        ((Activity)(context)).finish();
        context.startActivity(intent);
    }

    public static void openUserLoginWithPhoneActivity(final Context context , final String  account_type)
    {
        final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                intent.putExtra("account_type",account_type);
                context.startActivity(intent);
            }
        },2000);
    }


    public static void openSellerLoginWithPhoneActivity(final Context context , final String account_type)
    {
        final Intent intent = new Intent(context, SellerLoginMobileAuth.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                intent.putExtra("account_type",account_type);
                context.startActivity(intent);
            }
        },2000);
    }

    public static void startChooseAccountTypeActivity(final Context context) {

        Log.d(TAG, "startChooseAccountTypeActivity: starting activity for choosing login type user or seller");
//        Log.d(TAG, "startSellerDashBoard: starting seller dashboard activity" );

        final Intent intent = new Intent(context, ChooseLoginTypeActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                intent.putExtra(String.valueOf(R.string.account_type_key), account_type);
                context.startActivity(intent);
            }
        },2000);
    }

    public void ChnageActivityToPassword(final Context context ,final String password , final String  UserID)
    {
        final Intent intent = new Intent(context, ChangePasswordActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                intent.putExtra("curr_password",password);
                intent.putExtra("UID", UserID);
                context.startActivity(intent);
            }
        },2000);
    }

    public static void startSellerDashBoard(final Context context , final  String account_type )
    {
        Log.d(TAG, "startSellerDashBoard: starting seller dashboard activity" );

        final Intent intent = new Intent(context, SellerDashboardActivity.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                intent.putExtra(String.valueOf(R.string.account_type_key), account_type);
                context.startActivity(intent);
            }
        },2000);

    }

    public static void openLoginWithPhoneActivity(final Context context)
    {

//        CustomProgressDialog customProgressDialog;
//        final Intent intent = new Intent(context, MobileAuthenticationActivity.class);
//        customProgressDialog = new Utils.CustomProgressDialog(((Activity)(context)));
//        customProgressDialog.startLoadingDailog();
//        customProgressDialog.dismissDialog();
//
//        ((Activity)(context)).finish();
//        ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
////                intent.putExtra("account_type",account_type);
//        context.startActivity(intent);

        final Intent intent = new Intent(context, SellerLoginMobileAuth.class);
        new Utils.CustomProgressDialog(((Activity)(context))).startLoadingDailog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Utils.CustomProgressDialog(((Activity)(context))).dismissDialog();
                ((Activity)(context)).finish();
                ((Activity)(context)).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(intent);
            }
        },2000);

    }


}

