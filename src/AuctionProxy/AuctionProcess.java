package AuctionProxy;
import SourcesToOrganize.Bid;
import SourcesToOrganize.ItemInfo;

import java.util.ArrayList;

public interface AuctionProcess {

    /**
     * To place a bid
     *
     * @param bid Bid object that contains elements
     * @param itemID Identifier of Item
     */
    void bid(Bid bid, int itemID);

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


}
