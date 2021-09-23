package org.techtown.letseat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.letseat.DatePickerFragment;
import org.techtown.letseat.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private String birthday_string, email_string, pwd_string, name_string, pwd_check_string, gender;
    private TextView birthday;
    private Editable email_edit, pwd_edit, name_edit, pwd_check_edit;
    private EditText email, pwd, name, pwd_check;
    private RadioGroup gender_radio;
    private RadioButton male, female;
    Button btn_register, email_check, register_input;

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
        register_input = findViewById(R.id.register_birthday_input);
        register_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        email_check = findViewById(R.id.register_email_check);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                if (email_string == null || pwd_string == null || name_string == null
                        || birthday_string == null || (!male.isChecked() && !female.isChecked())) {
                    Toast.makeText(getApplicationContext(), "email_string: " + email_string + " pwd_string: " + pwd_string +
                            " name_string: " + name_string + "birthday_string: " + birthday_string, Toast.LENGTH_SHORT).show();
                    Log.d("testcase", "email_edit: " + email_edit + " pwd_edit: " + pwd_edit +
                            " name_edit: " + name_edit + "birthday.text: " + birthday.getText().toString());
                } else if (!pwd_check_string.equals(pwd_string)) {
                    Toast.makeText(getApplicationContext(), "비밀번호랑 비밀번호 확인란이 일치하지 않스니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    CustomTask customTask = new CustomTask();
                    customTask.execute();
                }
            }
        });
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        birthday_string = year_string + "." + month_string + "." + day_string;
        birthday.setText(birthday_string);
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

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", email_string);
                jsonObject.accumulate("password", pwd_string);
                jsonObject.accumulate("name", name_string);
                jsonObject.accumulate("birthday", birthday_string);
                jsonObject.accumulate("gender", gender);

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
                        result = "OK";
                    else
                        result = "BAD";
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    httpCon.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            return result;
        }
        @Override
        protected  void onPostExecute(String s){
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }
}
