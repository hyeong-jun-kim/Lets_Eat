package org.techtown.letseat;

public class Order_recycle_item {

    private int src;
    private String price;
    private String name;
    private String menuName;
    private String order;

    public Order_recycle_item(int src, String price, String name, String menuName,String order) {

        this.src = src;
        this.price = price;
        this.name = name;
        this.menuName = menuName;
        this.order = order;

    }

    public int getSrc() {
        return src;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getOrder() {return order; }
}

