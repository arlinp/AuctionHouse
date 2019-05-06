package AuctionHouse;

import java.io.Serializable;
import AuctionProxy.AuctionProxy;

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
    private transient AuctionProxy proxy;

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
     * Get the name of the item
     *
     * @return item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current price
     *
     * @return item's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Get the Item ID
     *
     * @return item's ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Set the price of the ItemInfo
     *
     * @param price price of item
     */
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name +  " $" + price + " #" + itemID + " @" + super.toString();
    }

    /**
     * Clone an object
     *
     * @return cloned ItemInfo
     */
    @Override
    public Object clone() {
        return new ItemInfo(name, price, itemID);
    }

    /**
     * Overwrite equals to check for equality
     *
     * @param o object
     * @return equality
     */
    @Override
    public boolean equals(Object o) {
        ItemInfo item = (ItemInfo) o;

        return itemID == item.getItemID() && proxy == item.getProxy();
    }

    /**
     * Gets the auction proxy
     *
     * @return Auction proxy
     */
    public AuctionProxy getProxy() {
        return proxy;
    }

    /**
     * Sets the auction proxy
     *
     * @param proxy Auction Proxy
     */
    public void setProxy(AuctionProxy proxy) {
        this.proxy = proxy;
    }
}
