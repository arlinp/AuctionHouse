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
    private String test = "";


    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public AuctionRequest(AuctionInfo type) {
        this.type = type;
        setStatus(true);
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


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
