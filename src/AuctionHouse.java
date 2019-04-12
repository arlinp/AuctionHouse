import java.util.ArrayList;

public class AuctionHouse implements AuctionProcess{


    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
    }

    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elements
     * @param itemID Identifier of Item
     */
    @Override
    public void bid(Bid bid, int itemID) {

    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        return null;
    }

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public ArrayList<ItemInfo> getItems() {
        return null;
    }
}
