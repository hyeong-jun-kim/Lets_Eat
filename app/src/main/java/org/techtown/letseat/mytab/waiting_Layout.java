package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.list.RestListAdapter;

public class waiting_Layout extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_waiting);

    }
}