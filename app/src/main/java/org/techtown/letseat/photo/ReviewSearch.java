package org.techtown.letseat.photo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import org.techtown.letseat.R;

public class ReviewSearch extends AppCompatActivity {

    EditText editText;
    String text;
    Double latitude, longitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_search);

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
                Intent intent2 = new Intent(getApplicationContext(), ReviewSearchAdapter.class);
                text = editText.getText().toString();
                intent2.putExtra("text",text);
                startActivity(intent2);
            }
        });
    }

}
