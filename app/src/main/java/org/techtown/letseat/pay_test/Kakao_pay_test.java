package org.techtown.letseat.pay_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.letseat.R;

public class Kakao_pay_test extends AppCompatActivity {

    Button button;
    EditText editTextName;
    EditText editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay_test);

        editTextName = findViewById(R.id.editName);
        editTextPrice = findViewById(R.id.editPrice);
        button = findViewById(R.id.kakaopay_test_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String price = editTextPrice.getText().toString();

                PayActivity payActivity = new PayActivity(name, price);

                Intent intent = new Intent(getApplicationContext(), payActivity.getClass());
                startActivity(intent);

            }
        });
    }
}