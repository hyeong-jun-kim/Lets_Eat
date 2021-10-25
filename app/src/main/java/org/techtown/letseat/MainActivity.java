package org.techtown.letseat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.mytab.MyTab;
import org.techtown.letseat.order.OrderActivity;
import org.techtown.letseat.pay_test.Kakao_pay_test;
import org.techtown.letseat.photo.OnPhotoItemClickListener;
import org.techtown.letseat.photo.PhotoData;
import org.techtown.letseat.photo.PhotoFragment;
import org.techtown.letseat.photo.PhotoList;
import org.techtown.letseat.photo.PhotoRecyclerAdapter;
import org.techtown.letseat.restaurant.info.RestInfoMain;
import org.techtown.letseat.restaurant.list.RestListMain;
import org.techtown.letseat.restaurant.qr.qr_restActivity;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.GpsTracker;
import org.techtown.letseat.util.ImageSliderAdapter;
import org.techtown.letseat.util.PhotoSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private IntentIntegrator qrScan;
    private double latitude;
    static public MainActivity mainActivity;
    private double longitude;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MainRecyclerData> list = new ArrayList<>();
    int resId;
    ArrayList<Integer> resIdList = new ArrayList<>();
    MainRecyclerAdapter adapter = new MainRecyclerAdapter();
    ArrayList<MainRecyclerData> items = new ArrayList<>();
    ArrayList<Double> differList = new ArrayList<>();
    ArrayList<MainRecyclerData> arrayList = new ArrayList<>();
    private PhotoRecyclerAdapter adapter2;
    ArrayList listResId = new ArrayList<>();
    static public PhotoList photoList;
    public boolean check = false;
    PhotoFragment photoFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    public static int userId = 0;
    private int ownerId = 1;
    int currentPage = 0;
    private String content, res_name;
    private Double get_rate;
    private float rate;
    Timer timer;

    private int[] images = new int[]{
            R.drawable.image1,R.drawable.image2,R.drawable.image3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        get_Review();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        checkRunTimePermission();
        if (AppHelper.requestQueue != null) { //RequestQueue 생성
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }



        FloatingActionButton btnQR = findViewById(R.id.btnQR);
        ImageButton btnRest = findViewById(R.id.btnRest);
        ImageButton btnPhoto = findViewById(R.id.btnPhoto);
        ImageButton btnMY = findViewById(R.id.btnMY);
        ImageButton btnOrder = findViewById(R.id.btnOrder);
        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        qrScan = new IntentIntegrator(this);

        recyclerView = (RecyclerView) findViewById(R.id.mainRestRecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), RestInfoMain.class);
                int rId = resIdList.get(pos);
                intent.putExtra("send_resId",rId);
                intent.putExtra("text","All");
                startActivity(intent);
            }
        });

        gpsTracker = new GpsTracker(MainActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        get_ResData();

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
        // 유저아이디 가져오기
        getUserId();
        // QR 스캔
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
                //현재위치
                gpsTracker = new GpsTracker(MainActivity.this);
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                //현재위치 전달
                Intent intent = new Intent(getApplicationContext(), RestListMain.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
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
                intent.putExtra("userId",userId);
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
                    int resId = obj.getInt("resId");
                    int tableNumber = obj.getInt("tableNumber");
                    // qrRest 액티비티 실행
                    Intent intent = new Intent(getApplicationContext(), qr_restActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("resId", resId);
                    bundle.putInt("tableNumber",tableNumber);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "씨발", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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



    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }
    private void get_ResData(){
        String url = "http://125.132.62.150:8000/letseat/store/findAll";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String resName,location,image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                resName = jsonObject.getString("resName");
                                location = jsonObject.getString("location");
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);
                                resId = jsonObject.getInt("resId");


                                String searchText = location;
                                Geocoder geocoder = new Geocoder(getBaseContext());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocationName(searchText, 3);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Address address = addresses.get(0);
                                LatLng place = new LatLng(address.getLatitude(), address.getLongitude());
                                double lat = place.latitude;
                                double lon = place.longitude;
                                if ((latitude < lat + 0.05 && lat - 0.05 < latitude) || (longitude < lon + 0.07 && lon - 0.07 < longitude)) {
                                    Double differ = Math.abs(latitude - lat) + Math.abs(longitude - lon);    //현재위치와의 거리차이
                                    MainRecyclerData item = new MainRecyclerData(differ,resName, bitmap);
                                    items.add(item);
                                    resIdList.add(resId);
                                    differList.add(differ);
                                }
                            }

                            Collections.sort(differList);   //거리기준 오름차순 정렬
                            for(int p = 0; p < differList.size(); p++) {
                                for(int q = 0; q < items.size(); q++){
                                    if(differList.get(p).equals(items.get(q).getDiffer())){
                                        arrayList.add(items.get(q));        // 거리기준으로 새로만들어진 arraylist
                                    }
                                }
                            }
                                        adapter.setItems(arrayList);
                                        adapter.notifyDataSetChanged();




                            Log.d("응답", response.toString());
                        } catch (JSONException e) {
                            Log.d("예외", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }
    public void getUserId(){
        SharedPreferences pref = getSharedPreferences("email",MODE_PRIVATE);
        String email = pref.getString("email","");
        String url = "http://125.132.62.150:8000/letseat/find/userId?email="+email;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        userId = Integer.parseInt(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
    // 처음 시작 시 리사이클러뷰 세팅하기
    private void init() {
        RecyclerView recyclerView2 = findViewById(R.id.mainphotoRecycler);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 3);
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new PhotoRecyclerAdapter();
        recyclerView2.setAdapter(adapter2);
        getData();
    }

    // 처음 시작 시 리사이클러뷰 불러오기
    private void getData() {
        for (int i = 0; i < listResId.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId((Bitmap) listResId.get(i));
            adapter2.addItem(data);
        }
        adapter2.setOnItemClicklistener(new OnPhotoItemClickListener() {
            @Override
            public void onItemClick(PhotoRecyclerAdapter.ItemViewHolder holder, View view,
                                    int position) {
                if (!check) {
                    check = true;
                    photoFragment = new PhotoFragment();
                    ft = fm.beginTransaction();
                    // 여기에 데이터베이스 정보 넣어야 함
                    photoFragment.setresId((Bitmap) listResId.get(holder.getAdapterPosition()));
                    photoFragment.setTitle(res_name);
                    photoFragment.setReview(content);
                    photoFragment.setRate(rate);
                    ft.add(R.id.photoFragment, photoFragment);
                    ft.commit();
                }
            }
        });
    }

    void get_Review() {
        String url = "http://125.132.62.150:8000/letseat/review/load/res?resId=1";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                JSONObject res_jsonObject = jsonObject.getJSONObject("restaurant");

                                res_name = res_jsonObject.getString("resName");
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);
                                content = jsonObject.getString("content");
                                get_rate = jsonObject.getDouble("rate");
                                rate = get_rate.floatValue();

                                listResId.add(bitmap);
                                Log.d("ds","ds");
                                init();
                            }

                            Log.d("응답", response.toString());
                        } catch (JSONException e) {
                            Log.d("예외", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}