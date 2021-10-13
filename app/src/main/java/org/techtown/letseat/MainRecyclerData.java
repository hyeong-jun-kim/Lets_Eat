package org.techtown.letseat;

public class MainRecyclerData {
    private String restNameTv;
    private int restIv;

    public MainRecyclerData(String restNameTv, int restIv) {
        this.restNameTv = restNameTv;
        this.restIv = restIv;
    }

    public String getRestNameTv() {
        return restNameTv;
    }

    public void setRestNameTv(String restNameTv) {
        this.restNameTv = restNameTv;
    }

    public int getRestIv() {
        return restIv;
    }

    public void setRestIv(int restIv) {
        this.restIv = restIv;
    }
}
