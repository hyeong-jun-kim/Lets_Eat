package org.techtown.letseat.menu;

public class Menu {
    private String name;
    private String price;
    private int resId;

    public Menu(String name, String price, int resId){
        this.name = name;
        this.price = price;
        this.resId = resId;
    }
    public String getName(){
        return name;
    }
    public void setName(){
        this.name = name;
    }
    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public int getResId(){
        return resId;
    }
    public void setResId(int resId){
        this.resId = resId;
    }
}
