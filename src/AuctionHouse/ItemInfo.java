package AuctionHouse;

import java.io.Serializable;

public class ItemInfo implements Serializable {

    private static transient int counter = 1000;
    private String name;
    private String desc;
    private long time;
    private double price;
    private int itemID;

    public ItemInfo(String name, String desc, long time, double price) {
        this.name = name;
        this.desc = desc;
        this.time = time;
        this.price = price;
        itemID = counter;
        counter++;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public long getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name + " " + desc + " " + System.currentTimeMillis() + " " + time + " " + price + " " + itemID ;
    }
}
