package AuctionProxy;
import SourcesToOrganize.Bid;
import AuctionHouse.ItemInfo;
import SourcesToOrganize.Packet;

import java.io.Serializable;
import java.util.ArrayList;

public class AuctionRequest extends Packet implements Serializable {

    // Things that are going to be send and returned
    private AuctionInfo type;
    private Bid bid = null;
    private ItemInfo itemInfo = null;
    private ArrayList<ItemInfo> items = null;
    private boolean contains = false;
    private int itemID = 0;
    private String message = "";
    private BidInfo bidStatus;
    private double newAmount = 0;


    public AuctionRequest(AuctionInfo type) {
        this.type = type;
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        setStatus(true);
    }

    public AuctionRequest(AuctionInfo type, int ID) {
        this.type = type;
        setPacketID(ID);
        setStatus(true);
    }


    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public AuctionInfo getType() { return type; }

    public ItemInfo getItem() {
        return itemInfo;
    }

    public ArrayList<ItemInfo> getItems() {
        return items;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public Bid getBid() {
        return bid;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public void setItems(ArrayList<ItemInfo> items) {
        this.items = items;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BidInfo getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(BidInfo bidStatus) {
        this.bidStatus = bidStatus;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }
}
