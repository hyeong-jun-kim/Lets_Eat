package org.techtown.letseat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.login.Login;
import org.techtown.letseat.login.RegisterActivity;
import org.techtown.letseat.mytab.MyTab;
import org.techtown.letseat.order.OrderActivity;
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
import org.techtown.letseat.waiting.WaitingActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar progressBar;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private IntentIntegrator qrScan;
    public double latitude;
    static public MainActivity mainActivity;
    public double longitude;
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
    ArrayList reviewImageList = new ArrayList<>();
    ArrayList reviewNameList = new ArrayList<>();
    ArrayList contentList = new ArrayList<>();
    ArrayList rateList = new ArrayList<>();
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
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
        get_Review();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        checkRunTimePermission();
        if (AppHelper.requestQueue != null) { //RequestQueue ??????
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
        // ????????? ????????????
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
        // ??????????????? ????????????
        getUserId();
        // QR ??????
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); //QR????????? ??????
                integrator.setCameraId(0); //??????????????? ??????
                integrator.setPrompt("QR ????????? ??????????????????");
                integrator.initiateScan(); // QR?????? ????????? ??????
            }
        });


        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????
                gpsTracker = new GpsTracker(MainActivity.this);
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                //???????????? ??????
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
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
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
            if (result.getContents().contains("resId") && result.getContents().contains("tableNumber")) {   //?????? qr??????
                Toast.makeText(MainActivity.this, "????????????" + result.getContents(), Toast.LENGTH_SHORT).show();
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    int resId = obj.getInt("resId");
                    int tableNumber = obj.getInt("tableNumber");
                    // qrRest ???????????? ??????
                    Intent intent = new Intent(getApplicationContext(), qr_restActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("resId", resId);
                    bundle.putInt("tableNumber",tableNumber);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else if(result.getContents().contains("resId")){      //????????? qr??????
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    int resId = obj.getInt("resId");
                    Intent intent = new Intent(getApplicationContext(), WaitingActivity.class);
                    intent.putExtra("resId",resId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
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
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //?????? ?????? ????????? ??? ??????
                ;
            } else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


            // 3.  ?????? ?????? ????????? ??? ??????



        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(MainActivity.this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
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
                                    Double differ = Math.abs(latitude - lat) + Math.abs(longitude - lon);    //?????????????????? ????????????
                                    MainRecyclerData item = new MainRecyclerData(differ,resName, bitmap);
                                    items.add(item);
                                    resIdList.add(resId);
                                    differList.add(differ);
                                }
                            }

                            Collections.sort(differList);   //???????????? ???????????? ??????
                            for(int p = 0; p < differList.size(); p++) {
                                for(int q = 0; q < items.size(); q++){
                                    if(differList.get(p).equals(items.get(q).getDiffer())){
                                        arrayList.add(items.get(q));        // ?????????????????? ?????????????????? arraylist
                                    }
                                }
                            }
                                        adapter.setItems(arrayList);
                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.INVISIBLE);

                            Log.d("??????", response.toString());
                        } catch (JSONException e) {
                            Log.d("??????", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("??????", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue ?????????
        AppHelper.requestQueue.add(request);
    }


    public String getCurrentAddress( double latitude, double longitude) {

        //????????????... GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";

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
                        FBuser();
                    }

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
    public void FBuser(){   // ????????????????????? ????????????
        DatabaseReference myRef = database.getReference("userId_"+userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // ????????? ?????? ????????? ????????? ??????, text ?????? ????????? ????????? ???????????? ?????????
                try{
                    int num = dataSnapshot.getValue(Integer.class);
                    Log.d("ds","ds");
                    if(num != 0){
                        //????????????
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,
                                new Intent(getApplicationContext(),MainActivity.class),0);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle("??????");
                        builder.setContentText("?????? ???????????????.");
                        builder.setContentIntent(contentIntent);
                        builder.setAutoCancel(true);
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(new NotificationChannel("default", "?????? ??????", NotificationManager.IMPORTANCE_DEFAULT));
                        }
                        notificationManager.notify(1, builder.build());
                        myRef.setValue(0);
                    }
                    else {

                    }
                }catch(Exception e){
                    myRef.setValue(0);      //???????????????????????????
                    Log.d("ds","ds");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ????????? ??? ??? ??????
            }
        });
    }
    // ?????? ?????? ??? ?????????????????? ????????????
    private void init() {
        RecyclerView recyclerView2 = findViewById(R.id.mainphotoRecycler);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 3);
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new PhotoRecyclerAdapter();
        recyclerView2.setAdapter(adapter2);
        getData();
    }
    // ?????? ?????? ??? ?????????????????? ????????????
    private void getData() {
        for (int i = 0; i < reviewImageList.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId((Bitmap) reviewImageList.get(i));
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
                    // ????????? ?????????????????? ?????? ????????? ???
                    photoFragment.setresId((Bitmap) reviewImageList.get(holder.getAdapterPosition()));
                    photoFragment.setTitle((String) reviewNameList.get(holder.getAdapterPosition()));
                    photoFragment.setReview((String) contentList.get(holder.getAdapterPosition()));
                    photoFragment.setRate((Float) rateList.get(holder.getAdapterPosition()));
                    ft.add(R.id.photoFragment, photoFragment);
                    ft.commit();
                }
            }
        });
    }
    void get_Review() {
        String url = "http://125.132.62.150:8000/letseat/review/load/all";
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
                                if(reviewNameList.size() > 8)
                                    break;
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                JSONObject res_jsonObject = jsonObject.getJSONObject("restaurant");
                                res_name = res_jsonObject.getString("resName");
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);
                                content = jsonObject.getString("content");
                                get_rate = jsonObject.getDouble("rate");
                                rate = get_rate.floatValue();
                                reviewNameList.add(res_name);
                                reviewImageList.add(bitmap);
                                contentList.add(content);
                                rateList.add(rate);
                                Log.d("ds","ds");
                            }
                            init();
                            Log.d("??????", response.toString());
                        } catch (JSONException e) {
                            Log.d("??????", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("??????", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // ?????? ?????? ????????? ?????? ????????? ????????? ?????????
        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext()); // requsetQueue ?????????
        AppHelper.requestQueue.add(request);
    }
}