package org.techtown.letseat.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class PhotoSave {
    /**
     * 사진 크기 조정
     */
    public static Bitmap resize(Bitmap bm, Resources res) {
        Configuration config = res.getConfiguration();
        bm = Bitmap.createScaledBitmap(bm, 800, 800, true);
        return bm;
    }

    public static Bitmap reviewResize(Bitmap bm, Resources res){
        Configuration config = res.getConfiguration();
        bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
        return bm;
    }
    /*
     * String형을 BitMap으로 변환시켜주는 함수
     * */
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /*
     * Bitmap을 String형으로 변환
     * */
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

}
