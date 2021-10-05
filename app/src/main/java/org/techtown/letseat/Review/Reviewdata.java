package org.techtown.letseat.Review;

public class Reviewdata {
    private String name;
    private String text;
    private int resId;

    public Reviewdata(String name, String text, int resId){
        this.name = name;
        this.text = text;
        this.resId = resId;
    }
    public String getName(){
        return name;
    }
    public void setName(){
        this.name = name;
    }
    public String getText(){
        return text;
    }
    public void setText(String price){
        this.text = text;
    }
    public int getResId(){
        return resId;
    }
    public void setResId(int resId){
        this.resId = resId;
    }
}
