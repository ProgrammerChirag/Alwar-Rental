package Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

import com.selflearn.alwarrenter.R;

import java.util.Objects;

public class CustomProgressDialog {

    private  Activity activity;
    private static AlertDialog dialog;
    private static Drawable drawable;

    public CustomProgressDialog(Activity myActivity){

        activity=myActivity;
        drawable=activity.getResources().getDrawable(R.drawable.custom_dialog_style);
    }

    @SuppressLint("InflateParams")
    public  void startLoadingDailog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog,null));

        builder.setCancelable(false);

        dialog=builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(drawable);
        dialog.show();
    }

    public  void dismissDialog(){
        if(dialog != null && dialog.isShowing())
        dialog.dismiss();
    }
}
