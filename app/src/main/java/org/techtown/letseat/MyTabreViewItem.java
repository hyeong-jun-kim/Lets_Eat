package org.techtown.letseat;

public class MyTabreViewItem {

    private String id;
    private String date, MenuTv;

    public MyTabreViewItem(String id, String date, String MenuTv) {

        this.id= id;
        this.date = date;
        this.MenuTv = MenuTv;

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

    public String getMenuTv() {
        return MenuTv;
    }

    public void setMenuTv(String menuTv) {
        MenuTv = menuTv;
    }
}

