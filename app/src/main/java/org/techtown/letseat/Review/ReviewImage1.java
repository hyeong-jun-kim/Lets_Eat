package org.techtown.letseat.Review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.techtown.letseat.R;
import org.tensorflow.lite.Interpreter;


public class ReviewImage1 extends AppCompatActivity {
    private Intent intent;
    private ImageView imageView;
    private Button upload_btn, save_btn;
    private Bitmap bitmap;
    private String photoPath, foodName, argmax;
    private TextView menu_name, accuracy;
    private SharedPreferences sharedPreferences;
    private Interpreter interpreter;
    private static final String TAG = "ReviewActivity";
    private Uri filePath;
    public Bitmap image_file;
    public ByteBuffer input, modelOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_image1);

        checkSelfPermission();
        bitmap = null;
        image_file = null;
        input = null;
        modelOutput = null;
        imageView = findViewById(R.id.imageView);
        save_btn = findViewById(R.id.save_btn);
        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
                LoadTFliteModel();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

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

            ActivityCompat.requestPermissions(this, temp.trim().split(""), 1);
        } else {
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                    Uri selectedImageUri;
                    MultiTransformation option = new MultiTransformation(new CenterCrop(), new RoundedCorners(8));
                    super.onActivityResult(requestCode, resultCode, data);
                    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                        switch (requestCode) {
                            case 101:
                                selectedImageUri = data.getData();
                    Glide.with(getApplicationContext()).load(selectedImageUri).apply(RequestOptions.bitmapTransform(option)).into(imageView);
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source= ImageDecoder.createSource(getApplicationContext().getContentResolver(), selectedImageUri);
                        try {
                            bitmap= ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true);
                            image_file = bitmap;
                            Image_Compile();
                            ByteBuffer();
                            //Labeling();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                            image_file = bitmap;
                            Image_Compile();
                            ByteBuffer();
                            //Labeling();
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
                .getModel("Food-Detector", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
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


    public void Image_Compile(){
        Bitmap bitmap = Bitmap.createScaledBitmap(image_file, 224, 224, true);
        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int px = bitmap.getPixel(x, y);

                // Get channel values from the pixel value.
                int r = Color.red(px);
                int g = Color.green(px);
                int b = Color.blue(px);

                // Normalize channel values to [-1.0, 1.0]. This requirement depends
                // on the model. For example, some models might require values to be
                // normalized to the range [0.0, 1.0] instead.
                float rf = (r - 127) / 255.0f;
                float gf = (g - 127) / 255.0f;
                float bf = (b - 127) / 255.0f;

                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }
        this.input = input;
    }

    public void ByteBuffer(){
        int bufferSize = 150 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
        ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        interpreter.run(input, modelOutput);
    }

    public void Labeling(){
        modelOutput.rewind();
        FloatBuffer probabilities = modelOutput.asFloatBuffer();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("labels.txt")));
            for (int i = 0; i < probabilities.capacity(); i++) {
                String label = reader.readLine();
                float probability = probabilities.get(i);
                Log.i(TAG, String.format("%s: %1.4f", label, probability));
                String percent = String.valueOf(probability);
                menu_name.setText(label);
                accuracy.setText(percent);
            }
        } catch (IOException e) {
            // File not found?
        }
    }
}