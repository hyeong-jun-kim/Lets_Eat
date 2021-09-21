package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import org.techtown.letseat.KaKaoCallBack;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;

import java.security.MessageDigest;

public class Login extends AppCompatActivity {
    Button button;
    Button kakao_login_button;
    LoginButton loginButton;

    private KaKaoCallBack kaKaoCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);




        kaKaoCallBack = new KaKaoCallBack();
        Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().checkAndImplicitOpen();

        loginButton = findViewById(R.id.login_button);
        kakao_login_button = findViewById(R.id.kakao_login_button);

        kakao_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        HashKey();

        button = findViewById(R.id.review_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void kakaoError(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kaKaoCallBack);
    }

    private void HashKey() {
        try {
            PackageInfo pkinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : pkinfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                String result = new String(Base64.encode(messageDigest.digest(), 0));
                Log.d("해시", result);
            }
        }
        catch (Exception e) {
        }
    }
}