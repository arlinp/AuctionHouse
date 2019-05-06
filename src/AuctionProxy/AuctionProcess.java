package AuctionProxy;

import AuctionHouse.Bid;
import AuctionHouse.ItemInfo;
import java.util.ArrayList;

/**
 * Functions for the Auction
 */
public interface AuctionProcess {

    /**
     * To place a bid
     *
     * @param bid Bid object that contains elements
     */
    BidInfo bid(Bid bid);

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
     * Check to see if the close is allowed
     *
     * @param accountID Account ID to be used for checking
     * @return True if no active bids, false if active bids
     */
    boolean closeRequest(int accountID);
}
