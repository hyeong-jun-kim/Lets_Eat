package org.techtown.letseat.pay_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.letseat.R;
import org.techtown.letseat.order.Fragment_order;
import org.techtown.letseat.restaurant.info.res_info_fragment1;

public class Kakao_pay_test extends AppCompatActivity {

    Button button;
    TextView Price_tv;
    String Price;
    int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay_test);

        Price_tv = findViewById(R.id.Price_textView);
        button = findViewById(R.id.kakaopay_test_button);

        Intent intent = getIntent();
        resId = intent.getIntExtra("resId",0);

        //가격 받아오기
        try {
            intent = getIntent();
            Price = intent.getStringExtra("Price");
            Price_tv.setText(Price+"원 입니다.");
        }catch (Resources.NotFoundException e){
            Log.d("에러",e.toString());
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주문내역에 resId값 전달(주문 성공시 주문내역에 가게 리사이클러뷰를 나타내기위해서)
                Fragment fragment_order = new Fragment_order();
                Bundle bundle = new Bundle();
                bundle.putInt("resId",resId);
                fragment_order.setArguments(bundle);
                PayActivity payActivity = new PayActivity(Price, "");
                Intent intent = new Intent(getApplicationContext(), payActivity.getClass());
                startActivity(intent);

            }
        });
    }
}