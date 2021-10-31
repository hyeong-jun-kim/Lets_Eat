package org.techtown.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class RestSearch2 extends AppCompatActivity {

    EditText editText;
    String text;
    Double latitude, longitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        editText = findViewById(R.id.search_text);

        Button searchbtn = findViewById(R.id.search);
        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),Restaurant_Search.class);
                text = editText.getText().toString();
                intent2.putExtra("text",text);
                intent2.putExtra("latitude",latitude);
                intent2.putExtra("longitude",longitude);
                startActivity(intent2);
            }
        });
    }
}