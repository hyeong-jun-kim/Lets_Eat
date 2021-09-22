package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import org.json.JSONObject;
import org.techtown.letseat.KaKaoCallBack;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;




public class Login extends AppCompatActivity {
    Button  btn_register, login_button;
    LoginButton kakao_login_button;
    EditText input_email, input_password;


    private KaKaoCallBack kaKaoCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login_button = findViewById(R.id.login_button);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        btn_register = findViewById(R.id.btnRegister);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });



        kaKaoCallBack = new KaKaoCallBack();
        Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().checkAndImplicitOpen();


        kakao_login_button = findViewById(R.id.kakao_login_button);

        kakao_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        HashKey();
    }

    // 서버 연동 파트
    void login(){
        try {
            String email = input_email.getText().toString();
            String password = input_password.getText().toString();
            Log.w("앱에서 보낸값",email+", "+password);

            CustomTask task = new CustomTask();
            String result = task.execute(email,password).get();
            Log.w("받은값",result);

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){

        }
    }



    class CustomTask extends AsyncTask<String, Void, String> {

        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL urlCon = new URL("http://220.70.169.23:8000/letseat/register/normal");
                HttpURLConnection httpCon = (HttpURLConnection) urlCon.openConnection();

                String json = "";
                Editable email = input_email.getText();
                Editable password = input_password.getText();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("password", password);
                json = jsonObject.toString();

                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestProperty("Content-type", "application/json");

                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);

                OutputStream os = httpCon.getOutputStream();
                os.write(json.getBytes("euc-kr"));
                os.flush();

                try {
                    is = httpCon.getInputStream();
                    if (is != null)
                        result = "Good work!";
                    else
                        result = "Did not work!";
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    httpCon.disconnect();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }
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