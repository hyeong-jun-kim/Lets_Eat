package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.login.Login;

public class SplashActivity extends AppCompatActivity {
    Animation anim_fade_in_1, anim_fade_in_2;
    TextView title, subtitle, copyright;
    String email_string, pwd_string;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        copyright = findViewById(R.id.copyright);
        anim_fade_in_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_1);
        anim_fade_in_2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_2);
        subtitle.startAnimation(anim_fade_in_2);
        copyright.startAnimation(anim_fade_in_2);
        title.startAnimation(anim_fade_in_1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                email_string = pref.getString("email", "");
                pwd_string = pref.getString("pwd", "");
                if (!email_string.isEmpty() && !pwd_string.isEmpty()) {
                    login();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    // 로그인 POST 요청
    void login() {
        String url = "http://183.100.237.18:8000/letseat/login/normal";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email_string);
            postData.put("password", pwd_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override // 응답 잘 받았을 때
                    public void onResponse(JSONObject response) {
                        // 화면 전환
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // 에러 발생 시
                    public void onErrorResponse(VolleyError error) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}