package AuctionHouse;

import java.io.Serializable;

public class ItemInfo implements Serializable {

//    private static transient int counter = 1000;
    private String name;
    private double price;
    private int itemID;

    public ItemInfo(String name, double price, int count) {
        this.name = name;
        this.price = price;
        itemID = count;
    }

    public String getName() {
        return name;
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
    public Object clone() {
        return new ItemInfo(name, price, itemID);
    }

    @Override
    public boolean equals(Object o) {
        ItemInfo item = (ItemInfo) o;

        return itemID == item.getItemID();
    }
}
