package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.idActivity;
import org.techtown.letseat.order.OrderActivity;
import org.techtown.letseat.passwordActivity;

public class information_fix extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_information_fix);

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton idIb = findViewById(R.id.idIb);
        idIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idActivity.class);
                startActivity(intent);
            }
        });
        ImageButton passwordIb = findViewById(R.id.passwordIb);
        passwordIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), passwordActivity.class);
                startActivity(intent);
            }
        });
    }
}