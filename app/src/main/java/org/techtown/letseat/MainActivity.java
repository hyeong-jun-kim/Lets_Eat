package org.techtown.letseat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

import org.techtown.letseat.mytab.MyTab;
import org.techtown.letseat.order.OrderActivity;
import org.techtown.letseat.photo.PhotoList;
import org.techtown.letseat.restaurant.Rest_List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnQR = findViewById(R.id.btnQR);
        Button btnRest = findViewById(R.id.btnRest);
        Button btnPhoto = findViewById(R.id.btnPhoto);
        Button btnMY = findViewById(R.id.btnMY);

        Button btnOrder = findViewById(R.id.btnOrder);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); //QR코드만 식별
                integrator.setCameraId(0); //후면카메라 설정
                integrator.setPrompt("식당 QR 코드를 스캔해주세요");
                integrator.initiateScan(); // QR코드 리더기 실행
            }
        });

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Rest_List.class);
                startActivity(intent);
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoList.class);
                startActivity(intent);
            }
        });

        btnMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyTab.class);
                startActivity(intent);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}