package org.techtown.letseat.Review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.CustomRemoteModel;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.linkfirebase.FirebaseModelSource;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;

import org.techtown.letseat.R;

public class ReviewImage2 extends AppCompatActivity{
    private ImageView imageView;
    private Button upload_btn;
    private Bitmap bitmap;
    public Bitmap image_file;
    public Uri selectedImageUri;
    private TextView menu_name, accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_image2);

        checkSelfPermission();
        menu_name = findViewById(R.id.menu_name);
        accuracy = findViewById(R.id.accuracy);
        imageView = findViewById(R.id.imageView);
        upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
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
                            //Labeling();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                            image_file = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public void LoadModel(){
        FirebaseModelSource firebaseModelSource =
                new FirebaseModelSource.Builder("model2").build();
        CustomRemoteModel remoteModel =
                new CustomRemoteModel.Builder(firebaseModelSource).build();

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        RemoteModelManager.getInstance().download(remoteModel, downloadConditions)
                .addOnSuccessListener(task -> {
                    // Success.
                    CustomImageLabelerOptions customImageLabelerOptions = new CustomImageLabelerOptions.Builder(remoteModel)
                            .setConfidenceThreshold(0.0f)  // Evaluate your model in the Cloud console
                            // to determine an appropriate value.
                            .build();
                    ImageLabeler labeler = ImageLabeling.getClient(customImageLabelerOptions);

                    RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
                            .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean isDownloaded) {
                                    CustomImageLabelerOptions.Builder optionsBuilder;
                                    if (isDownloaded) {
                                        optionsBuilder = new CustomImageLabelerOptions.Builder(remoteModel);
                                    } else {
                                        optionsBuilder = new CustomImageLabelerOptions.Builder(remoteModel);
                                    }
                                    CustomImageLabelerOptions options = optionsBuilder
                                            .setConfidenceThreshold(0.0f)  // Evaluate your model in the Cloud console
                                            // to determine an appropriate threshold.
                                            .build();

                                    ImageLabeler labeler = ImageLabeling.getClient(options);

                                    InputImage image =  InputImage.fromBitmap(image_file,0);
                                    labeler.process(image)
                                            .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                                                @Override
                                                public void onSuccess(List<ImageLabel> labels) {
                                                    // Task completed successfully
                                                    // ...
                                                    for (ImageLabel label : labels) {
                                                        String text = label.getText();
                                                        float confidence = label.getConfidence();
                                                        int index = label.getIndex();
                                                        String percent = String.valueOf(confidence);
                                                        menu_name.setText(text);
                                                        accuracy.setText(percent);
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                }
                                            });
                                }
                            });
                });
    }
}
