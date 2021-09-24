package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.AppHelper;
import org.techtown.letseat.DatePickerFragment;
import org.techtown.letseat.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private String birthday_string, email_string, pwd_string, name_string, pwd_check_string, gender;
    private Editable email_edit, pwd_edit, name_edit, pwd_check_edit;
    private EditText email, pwd, name, pwd_check, birthday;
    private RadioGroup gender_radio;
    private RadioButton male, female;
    Button btn_register, btn_email_check, btn_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        email = findViewById(R.id.register_email);
        pwd = findViewById(R.id.register_pwd);
        pwd_check = findViewById(R.id.regitser_pwd_check);
        name = findViewById(R.id.register_name);
        birthday = findViewById(R.id.register_birthday);
        gender_radio = findViewById(R.id.register_gender);
        male = findViewById(R.id.register_male);
        female = findViewById(R.id.register_female);
        birthday.setEnabled(false);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwd_check.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn_birthday = findViewById(R.id.register_birthday_input);
        btn_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        btn_email_check = findViewById(R.id.register_email_check);
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email.getText().toString();
                sendLoginCheckRequest(email_string, email);
            }
        });
        if (AppHelper.requestQueue != null) { //RequestQueue 생성
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email.getText().toString();
                pwd_string = pwd.getText().toString();
                name_string = name.getText().toString();
                pwd_check_string = pwd_check.getText().toString();
                if (male.isChecked() || female.isChecked()) {
                    if (male.isChecked()) {
                        gender = "male";
                    } else {
                        gender = "female";
                    }
                }
                if (email_string.isEmpty() || pwd_string.isEmpty() || name_string.isEmpty()
                        || birthday_string.isEmpty() || (!male.isChecked() && !female.isChecked())) {
                    Toast.makeText(getApplicationContext(), "email_string: " + email_string + " pwd_string: " + pwd_string +
                            " name_string: " + name_string + "birthday_string: " + birthday_string, Toast.LENGTH_SHORT).show();
                    Log.d("testcase", "email_edit: " + email_edit + " pwd_edit: " + pwd_edit +
                            " name_edit: " + name_edit + "birthday.text: " + birthday.getText().toString());
                } else if (!pwd_check_string.equals(pwd_string)) {
                    Toast.makeText(getApplicationContext(), "비밀번호랑 비밀번호 확인란이 일치하지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    sendRegisterRequest();
                }
            }
        });
    }
    // DatePicker Fragment 보여줌
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
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
    // 회원가입 POST 요청
    public void sendRegisterRequest() {
        String url = "http://183.100.237.18:8000/letseat/register/normal";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email_string);
            postData.put("password", pwd_string);
            postData.put("birthday", birthday_string);
            postData.put("gender", gender);
            postData.put("name", name_string);
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
                        Intent intent = new Intent(RegisterActivity.this, Login.class);
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override // 에러 발생 시
                    public void onErrorResponse(VolleyError error) {
                        println("이미 존재하는 회원입니다.");
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }

    // 로그인 중복 확인 GET
    public void sendLoginCheckRequest(String email_string, TextView email) {
        String url = "http://183.100.237.18:8000/letseat/register/email/check?email=" + email_string;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override // 응답 잘 받았을 때
                    public void onResponse(String response) {
                        if (response.equals("emailCheckFail")) {
                            println("위 이메일 주소는 사용불가능합니다.");
                            email.setText("");
                        } else {
                            println("사용가능한 이메일입니다.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override // 에러 발생 시
                    public void onErrorResponse(VolleyError error) {
                        println("연결 상태 불량");
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }

    public void println(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
