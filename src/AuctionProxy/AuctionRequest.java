package AuctionProxy;
import AuctionHouse.Bid;
import AuctionHouse.ItemInfo;
import SourcesToOrganize.Packet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * An AuctionRequest object saves the
 * the status of an auction and serializes
 * the request.
 */
public class AuctionRequest extends Packet implements Serializable {

    // Things that are going to be send and returned
    private AuctionInfo type;
    private Bid bid = null;
    private ItemInfo itemInfo = null;
    private ArrayList<ItemInfo> items = null;
    private boolean contains = false;
    private int itemID = 0;
    private BidInfo bidStatus;
    private double newAmount = 0;


    /**
     * Constructor for AuctionRequest
     *
     * @param type Type of request
     */
    public AuctionRequest(AuctionInfo type) {
        this.type = type;
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        setStatus(true);
    }

    /**
     * Constructor for AuctionRequest
     *
     * @param type type of request
     * @param ID request ID
     */
    public AuctionRequest(AuctionInfo type, int ID) {
        this.type = type;
        setPacketID(ID);
        setStatus(true);
    }

    /**
     * Gets the item ID
     *
     * @return item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Sets the ItemID
     *
     * @param itemID ID to assign
     */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * @return type of auction request
     */
    public AuctionInfo getType() { return type; }

    /**
     * @return info of item being auctioned
     */
    public ItemInfo getItem() {
        return itemInfo;
    }

    /**
     * @return list of active items
     */
    public ArrayList<ItemInfo> getItems() {
        return items;
    }

    /**
     * @param bid bid to set
     */
    public void setBid(Bid bid) {
        this.bid = bid;
    }

    /**
     * @return bid
     */
    public Bid getBid() {
        return bid;
    }

    /**
     * @param contains boolean
     */
    public void setRequest(boolean contains) {
        this.contains = contains;
    }

    /**
     * @param itemInfo item info to save
     */
    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    /**
     * @param items List of items to save
     */
    public void setItems(ArrayList<ItemInfo> items) {
        if (items == null) this.items = null;
        else this.items = items;
    }

    /**
     * @return status of bid
     */
    public BidInfo getBidStatus() {
        return bidStatus;
    }

    /**
     * @param bidStatus set status of bid
     */
    public void setBidStatus(BidInfo bidStatus) {
        this.bidStatus = bidStatus;
    }

    /**
     * @return get new amount of bid
     */
    public double getNewAmount() {
        return newAmount;
    }

    /**
     * @param newAmount set the new amount of the bid
     */
    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }

    /**
     * @return checks if contains is true/false
     */
    public boolean isContains() {
        return contains;
    }
}
