package org.techtown.letseat.waiting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.RestSearch2;

public class WaitingActivity extends AppCompatActivity {
    private TextView waiting_queue, person_number;
    private Button waiting_btn1, waiting_btn2;
    private ImageButton minus_btn, plus_btn;
    private int person = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_activity);


        waiting_queue = findViewById(R.id.waiting_queue);
        person_number = findViewById(R.id.person_number);
        minus_btn = findViewById(R.id.minus_btn);
        plus_btn = findViewById(R.id.plus_btn);
        waiting_btn1 = findViewById(R.id.waiting_btn1);
        waiting_btn2 = findViewById(R.id.waiting_btn2);

        person_number.setText(person);

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person++;
                person_number.setText(person);
            }
        });

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person--;
                person_number.setText(person);
            }
        });

        waiting_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        waiting_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
