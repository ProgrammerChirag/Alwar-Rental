package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.selflearn.alwarrenter.R;

public class AdminDashBoardActivity extends AppCompatActivity {

    TextView listSeller , listUsers , ChangeAdminPassword , listRequest , listProperty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin_dashboard);

        findID();
        setListeners();
    }

    private void setListeners() {

        listSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        listUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoardActivity.this , ActivityListUsers.class));
            }
        });

        listRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoardActivity.this , ActivityRequestForAdminSide.class));
            }
        });

        listProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoardActivity.this , ListPropertiesActivity.class));
            }
        });

        ChangeAdminPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoardActivity.this , ChangeAdminIDAndPassword.class));
            }
        });
    }

    private void findID() {

        listSeller = findViewById(R.id.listSeller);
        listUsers = findViewById(R.id.listUsers);
        listRequest = findViewById(R.id.listRequest);
        listProperty = findViewById(R.id.listProperty);
        ChangeAdminPassword = findViewById(R.id.changeAdminPassword);
    }

}
