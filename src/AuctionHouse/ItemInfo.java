package AuctionHouse;

import java.io.Serializable;

public class ItemInfo implements Serializable {

    private static transient int counter = 1000;
    private String name;
    private String desc;
    private double price;
    private int itemID;

    public ItemInfo(String name, String desc, long time, double price) {
        this.name = name;
        this.desc = desc;
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

    public double getPrice() {
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name +  " $" + price + " #" + itemID + " @" + super.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ItemInfo(name, desc, 0, price);
    }

    @Override
    public boolean equals(Object o) {
        ItemInfo item = (ItemInfo) o;

        return itemID == item.getItemID();
    }
}
