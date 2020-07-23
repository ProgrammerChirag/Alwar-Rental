package AccountHandler;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.selflearn.alwarrenter.R;

import Utils.CustomProgressDialog;

public class ActivityChooseLoginType extends AppCompatActivity {

    CardView LoginWithEmailButton , LoginWithPhoneNumberButton;
    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_input_method);
        customProgressDialog = new CustomProgressDialog(ActivityChooseLoginType.this);

        findId();

        LoginWithEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Utils.ActivityNavigation.openSellerLoginWithEmailActivity(ActivityChooseLoginType.this);
            }
        });

        LoginWithPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Utils.ActivityNavigation.openLoginWithPhoneActivity(ActivityChooseLoginType.this  );
            }
        });

    }

    private void findId() {

        LoginWithEmailButton = findViewById(R.id.login_with_email_button);
        LoginWithPhoneNumberButton = findViewById(R.id.login_with_number_button);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        customProgressDialog.dismissDialog();
        customProgressDialog = null;
    }

}
