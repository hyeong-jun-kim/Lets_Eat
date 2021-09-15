package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.letseat.R;

public class MyTab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_main);

        Button w_s_button = findViewById(R.id.waiting_status_button);
        w_s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), waiting_Layout.class);
                startActivity(intent);
            }
        });

        Button i_f_button = findViewById(R.id.information_fix_button);
        i_f_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), information_fix.class);
                startActivity(intent);
            }
        });
    }
}