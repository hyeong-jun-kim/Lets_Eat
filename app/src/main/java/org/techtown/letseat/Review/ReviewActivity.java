package org.techtown.letseat.Review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.util.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.login.Login;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import com.google.gson.JsonObject;

import org.techtown.letseat.R;
import org.techtown.letseat.util.PhotoSave;
import org.tensorflow.lite.Interpreter;

public class ReviewActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private int userId = MainActivity.userId;
    private int resId;
    private Intent intent;
    private ImageView imageView;
    private RatingBar review_grade;
    private EditText edit_review_text;
    private Button upload_photo_btn, save_review_btn, cancel_btn;
    private Bitmap bitmap;
    private String accuracy_score;
    private String review_text;
    private String save_image;
    private String menu_label;
    private String star_score;
    private String content;
    private String menu = "삼겹살";
    private TextView menu_name, accuracy;
    private Interpreter interpreter;
    private static final String TAG = "ReviewActivity";
    public Bitmap image_file;
    public ByteBuffer input, modelOutput;
    public PhotoSave photoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resId = bundle.getInt("resId");
        setContentView(R.layout.order_review);

        checkSelfPermission();
        bitmap = null;
        image_file = null;
        input = null;
        modelOutput = null;
        edit_review_text = findViewById(R.id.review_text);
        review_grade = findViewById(R.id.review_grade);
        menu_name = findViewById(R.id.menu_name);
        accuracy = findViewById(R.id.accuracy);
        imageView = findViewById(R.id.imageView1);
        save_review_btn = findViewById(R.id.save_review_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
        upload_photo_btn = findViewById(R.id.upload_photo_btn);
        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        upload_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                LoadTFliteModel();
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        save_review_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                registerReview();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Image 파일 입력 onActivityResult까지
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ReviewActivity", "권한 허용" + permissions[i]);
                }
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + "";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + "";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else {
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    // Image 입력 및 Image 분석 AI함수 실행
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri selectedImageUri;
        MultiTransformation option = new MultiTransformation(new CenterCrop(), new RoundedCorners(8));
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            switch (requestCode) {
                case GET_GALLERY_IMAGE:
                    selectedImageUri = data.getData();
                    Glide.with(getApplicationContext()).load(selectedImageUri).apply(RequestOptions.bitmapTransform(option)).into(imageView);
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source= ImageDecoder.createSource(getApplicationContext().getContentResolver(), selectedImageUri);
                        try {
                            bitmap= ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true);
                            image_file = bitmap;
                            Image_Compile();
                            ByteBuffer();
                            Labeling();
                            Resources res = getResources();
                            Bitmap change_image_size = PhotoSave.resize(image_file,res);
                            save_image = PhotoSave.BitmapToString(change_image_size);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                            image_file = bitmap;
                            Image_Compile();
                            ByteBuffer();
                            Labeling();
                            Resources res = getResources();
                            Bitmap change_image_size = PhotoSave.resize(image_file,res);
                            save_image = PhotoSave.BitmapToString(change_image_size);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public void LoadTFliteModel(){
        // 모델 배포(기기에 모델 다운로드 및 TFLite 인터프리터 초기화)
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("Food-Detector", DownloadType.LOCAL_MODEL, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        // Download complete. Depending on your app, you could enable the ML
                        // feature, or switch from the local model to the remote model, etc.
                        Log.d("TFLite Model Download","Model Success!");
                        // The CustomModel object contains the local path of the model file,
                        // which you can use to instantiate a TensorFlow Lite interpreter.
                        FirebaseModelDownloader interpreter_test = FirebaseModelDownloader.getInstance();
                        File modelFile = model.getFile();
                        if (modelFile != null || interpreter_test != null) {
                            interpreter = new Interpreter(modelFile);
                            Log.d("TFLite Model Download","Model Success!");
                        }
                        else{
                            Log.d("TFLite Model Download","Model Fail!");
                        }
                    }
                });
    }

    // 이미지 전처리
    public void Image_Compile(){
        Bitmap bitmap = Bitmap.createScaledBitmap(image_file, 224, 224, true);
        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int px = bitmap.getPixel(x, y);

                // Get channel values from the pixel value.
                int r = Color.red(px);
                int g = Color.green(px);
                int b = Color.blue(px);

                // Normalize channel values to [-1.0, 1.0]. This requirement depends
                // on the model. For example, some models might require values to be
                // normalized to the range [0.0, 1.0] instead.
                float rf = r / 255.0f;
                float gf = g / 255.0f;
                float bf = b / 255.0f;

                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }
        this.input = input;
    }

    public void ByteBuffer(){
        int bufferSize = 152 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
        ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        interpreter.run(input, modelOutput);
        this.modelOutput = modelOutput;
    }

    // 이미지 라벨링
    public void Labeling(){
        modelOutput.rewind();
        FloatBuffer probabilities = modelOutput.asFloatBuffer();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("label.txt")));
            ArrayList<String> labels = new ArrayList<>();
            float max = 0f;
            int idx = 0;
            for (int i = 0; i < probabilities.capacity(); i++) {
                String label = reader.readLine();
                labels.add(label);
                float probability = probabilities.get(i);
                if(max < probability){
                    max = probability;
                    idx = i;
                }
                Log.i(TAG, String.format("%s: %1.4f", label, probability));
            }
            float probability = Math.round((probabilities.get(idx) * 1000)/10.0);
            String label = labels.get(idx);
            String percent = String.valueOf(probability);
            menu_name.setText(label);
            accuracy.setText(percent);
        } catch (IOException e) {
            // File not found?
        }
    }

    public void registerReview(){
        String url = "http://125.132.62.150:8000/letseat/review/register";
        String menuName = menu_name.getText().toString();
        String content = edit_review_text.getText().toString();
        float rate = review_grade.getRating();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = PhotoSave.resize(bitmap, getResources());
        String image = PhotoSave.BitmapToString(bitmap);

        JSONObject postData = new JSONObject();
        JSONObject resData = new JSONObject();
        JSONObject userData = new JSONObject();
        try {
            postData.put("menuName", menuName);
            postData.put("content", content);
            postData.put("rate", rate);
            postData.put("image", image);
            resData.put("resId", resId);
            userData.put("userId", userId);
            postData.put("restaurant",resData);
            postData.put("user",userData);
        }catch (JSONException e){
            Log.d("error",e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        Toast.makeText(getApplicationContext(), "성공적으로 리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewActivity.this, "비정상적인 오류로 인해 리뷰가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}