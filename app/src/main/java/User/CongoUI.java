package User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.selflearn.alwarrenter.R;

public class CongoUI extends AppCompatActivity {

    ImageButton imageButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation_ui);

        imageButton = findViewById(R.id.next);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CongoUI.this , BottomNavActivity.class);
                intent.putExtra(String.valueOf(R.string.account_type_key),"user");
                finish();
                startActivity(intent);

            }
        });


    }
}
