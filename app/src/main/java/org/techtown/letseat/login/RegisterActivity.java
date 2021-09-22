package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.letseat.R;

public class RegisterActivity extends AppCompatActivity {

    String state;
    EditText et_ID, et_PW;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        et_ID = findViewById(R.id.et_ID);
        et_PW = findViewById(R.id.et_PW);
        btn_register = findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_ID.getText().toString();
                String pw = et_PW.getText().toString();

            }
        });
    }
}