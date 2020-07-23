package Seller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.selflearn.alwarrenter.R;

public class MyCustomerActivity extends AppCompatActivity {

    TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_user);

        textView = findViewById(R.id.text);
        textView.setText("My Customers");

    }
}
