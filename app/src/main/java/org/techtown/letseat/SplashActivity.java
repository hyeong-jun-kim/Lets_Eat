package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Animation anim_fade_in_1, anim_fade_in_2;
    TextView title, subtitle, copyright;
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}