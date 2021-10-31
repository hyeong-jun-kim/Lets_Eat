package org.techtown.letseat.photo;

import android.graphics.Bitmap;

public class ReviewSearchItem {
    int rate;
    String title;
    String content;
    Bitmap bitmap;
    ReviewSearchItem(int rate, String title, String content, Bitmap bitmap){
        this.rate = rate;
        this.title = title;
        this.content = content;
        this.bitmap = bitmap;
    }
    public int getRate(){
        return rate;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
