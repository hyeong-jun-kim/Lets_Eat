package org.techtown.letseat.restaurant.review;

public class RestItemReviewItem {

    private int src;
    private String id;
    private String date;

    public RestItemReviewItem(int src, String id, String date) {

        this.src = src;
        this.id= id;
        this.date = date;

    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

