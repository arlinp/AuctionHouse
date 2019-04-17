package AuctionProxy;
import SourcesToOrganize.Bid;
import AuctionHouse.ItemInfo;

import java.util.ArrayList;

public interface AuctionProcess {

    /**
     * To place a bid
     *
     * @param bid Bid object that contains elements
     */
    void bid(Bid bid);

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    ItemInfo getItemInfo(int itemID);

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    ArrayList<ItemInfo> getItems();

    /**
     * Test String hello world
     *
     * @param s String to say
     * @return String as a response
     */
    String helloInternet(String s);
}
