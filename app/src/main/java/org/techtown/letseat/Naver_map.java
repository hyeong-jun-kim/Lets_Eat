package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapView;

public class Naver_map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_map);

        MapView mapView = new MapView(this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.MapView);
        mapViewContainer.addView(mapView);
    }
}