package org.techtown.letseat.restaurant.info;

public class RestInfoRecycleItem {

    private int src;
    private String title;
    private String subtitle;
    private String place;
    private String phonenum;
    private String time;
    private String parking;

    public RestInfoRecycleItem(int src, String title, String subtitle, String place, String phonenum, String time, String parking){

        this.src = src;
        this.title = title;
        this.subtitle = subtitle;
        this.place = place;
        this.phonenum = phonenum;
        this.time = time;
        this.parking = parking;
    }

    public int getSrc() {
        return src;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPlace() {
        return place;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getTime() {
        return time;
    }

    public String getParking() {
        return parking;
    }
}
