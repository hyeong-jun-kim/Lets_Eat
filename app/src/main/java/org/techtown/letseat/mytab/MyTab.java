package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.MyReview;
import org.techtown.letseat.R;
import org.techtown.letseat.login.Login;

public class MyTab extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_main);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);

        ImageButton reviewManageBtn = findViewById(R.id.reviewManageBtn);
        reviewManageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyReview.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        Button w_s_button = findViewById(R.id.waiting_status_button);
        w_s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), waiting_Layout.class);
                startActivity(intent);
            }
        });

        ImageButton i_f_button = findViewById(R.id.information_fix_button);
        i_f_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), information_fix.class);
                startActivity(intent);
            }
        });

        //카카오 로그아웃
        Button kakao_logout_button = findViewById(R.id.kakao_logout_button);
        kakao_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MyTab.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}