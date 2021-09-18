package org.techtown.letseat.restaurant.info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuActivity;
import org.techtown.letseat.mytab.MyTab;
import org.techtown.letseat.photo.Photo;
import org.techtown.letseat.photo.PhotoFragment;

public class RestInfoMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_info_activity);


        Button menu_button = findViewById(R.id.menu_button);
        Button review_button = findViewById(R.id.review_button);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent);
            }
        });


    }
}