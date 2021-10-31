package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.DatePickerFragment2;

public class Kakao_Login_userInfo extends AppCompatActivity {

    private String kakao_email, nickname_string, birthday_string, gender;
    private String pwd_string = "kakao";
    private EditText nickName, birthday;
    private Button btn_register, register_birthday;
    private RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login_user_info);

        Intent intent = getIntent();
        kakao_email = intent.getStringExtra("send");

        nickName = findViewById(R.id.nickName);
        btn_register = findViewById(R.id.btn_register);
        male = findViewById(R.id.register_male);
        female = findViewById(R.id.register_female);
        birthday = findViewById(R.id.birthday);
        register_birthday = findViewById(R.id.register_birthday);
        register_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname_string = nickName.getText().toString();
                if (male.isChecked() || female.isChecked()) {
                    if (male.isChecked()) {
                        gender = "male";
                    } else {
                        gender = "female";
                    }
                }
                if(nickname_string.isEmpty() || birthday_string.isEmpty() || (!male.isChecked() && !female.isChecked()))
                {
                    Toast.makeText(getApplicationContext(), " nickName_string: " + nickname_string
                            + "birthday_string: " + birthday_string, Toast.LENGTH_SHORT).show();
                }
                else {
                    sendRegisterRequest();
                }
            }
        });
    }

    // 회원가입 POST 요청
    public void sendRegisterRequest() {
        String url = "http://125.132.62.150:8000/letseat/register/normal";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", kakao_email);
            postData.put("password", pwd_string);
            postData.put("birthday", birthday_string);
            postData.put("gender", gender);
            postData.put("name", nickname_string);
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // 에러 발생 시
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }

    // DatePicker Fragment 보여줌
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    // DatePicker에서 Date 선택 후 처리
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        birthday_string = year_string + "." + month_string + "." + day_string;
        birthday.setText(birthday_string);
    }
}