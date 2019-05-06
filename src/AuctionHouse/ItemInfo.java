package AuctionHouse;

import java.io.Serializable;

/**
 * Stores all the information of an Item
 *
 * -Item Name
 * -Item prices
 * -Item ID
 */
public class ItemInfo implements Serializable {

    private String name;
    private double price;
    private int itemID;

    /**
     * Constructor for an Item's ino
     * @param name name of item
     * @param price item's price
     * @param count item ID
     */
    public ItemInfo(String name, double price, int count) {
        this.name = name;
        this.price = price;
        itemID = count;
    }

    /**
     * @return item's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return item's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return item's ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * @param price price of item
     */
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name +  " $" + price + " #" + itemID + " @" + super.toString();
    }

    /**
     * @return cloned ItemInfo
     */
    @Override
    public Object clone() {
        return new ItemInfo(name, price, itemID);
    }

    /**
     * @param o object
     * @return equality
     */
    @Override
    public boolean equals(Object o) {
        ItemInfo item = (ItemInfo) o;

        return itemID == item.getItemID();
    }
}
