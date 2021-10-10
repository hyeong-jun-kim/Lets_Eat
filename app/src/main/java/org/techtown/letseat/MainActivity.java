package org.techtown.letseat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.login.Login;
import org.techtown.letseat.login.RegisterActivity;
import org.techtown.letseat.map.Map_MainActivity;
import org.techtown.letseat.mytab.MyTab;
import org.techtown.letseat.order.OrderActivity;
import org.techtown.letseat.pay_test.Kakao_pay_test;
import org.techtown.letseat.photo.PhotoList;
import org.techtown.letseat.restaurant.list.RestListMain;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.ImageSliderAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private IntentIntegrator qrScan;

    private int ownerId = 1;
    int currentPage = 0;
    private String resName, phoneNumber, openTime, resIntro, businessNumber, restype, location; //qr코드 테스트용
    int aloneAble;
    Timer timer;

    private int[] images = new int[]{
            R.drawable.image1,R.drawable.image2,R.drawable.image3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AppHelper.requestQueue != null) { //RequestQueue 생성
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Button btnQR = findViewById(R.id.btnQR);
        Button btnRest = findViewById(R.id.btnRest);
        Button btnPhoto = findViewById(R.id.btnPhoto);
        Button btnMY = findViewById(R.id.btnMY);
        Button btnOrder = findViewById(R.id.btnOrder);
        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        qrScan = new IntentIntegrator(this);

        //네이버 지도 테스트
        Button map_button = findViewById(R.id.map_test);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Map_MainActivity.class);

                startActivity(intent);
            }
        });




        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));

        // 이미지 자동전환
        final Handler handler = new Handler();
        final Runnable Update = new Runnable(){
            @Override
            public void run() {
                if(currentPage == 3){
                    currentPage = 0;
                }
                sliderViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);



        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(images.length);

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); //QR코드만 식별
                integrator.setCameraId(0); //후면카메라 설정
                integrator.setPrompt("QR 코드를 스캔해주세요");
                integrator.initiateScan(); // QR코드 리더기 실행
            }
        });


        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestListMain.class);
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

        Button pay_test_button = findViewById(R.id.pay_test_button);
        pay_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Kakao_pay_test.class);
                startActivity(intent);
            }
        });

        //카카오 고유 id
        TextView tvEmail = findViewById(R.id.tvEmail);
        Intent intent = getIntent();
        String strEmail = intent.getStringExtra("email");
        tvEmail.setText(strEmail);


        //카카오 로그아웃
        Button kakao_logout_button = findViewById(R.id.kakao_logout_button);
        kakao_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "스캔완료" + result.getContents(), Toast.LENGTH_SHORT).show();
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    resName = obj.getString("resName");
                    phoneNumber = obj.getString("phoneNumber");
                    openTime = obj.getString("openTime");
                    resIntro = obj.getString("resIntro");
                    businessNumber = obj.getString("businessNumber");
                    restype = obj.getString("restype");
                    location = obj.getString("location");
                    aloneAble = obj.getInt("aloneAble");
                    sendRegisterRequest();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "씨발", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void sendRegisterRequest() {
        String url = "http://125.132.62.150:8000/letseat/store/register";
        JSONObject postData = new JSONObject();
        JSONObject ownerData = new JSONObject();
        try {
            ownerData.put("ownerId", ownerId);
            postData.put("resName", resName);
            postData.put("phoneNumber", phoneNumber);
            postData.put("openTime", openTime);
            postData.put("resIntro", resIntro);
            postData.put("businessNumber", businessNumber);
            postData.put("restype", restype);
            postData.put("location", location);
            postData.put("aloneAble",aloneAble);
            postData.put("owner", ownerData);
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
                        Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // 에러 발생 시
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }





    public void fllipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
    }



    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

}