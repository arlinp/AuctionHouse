package AuctionProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import SourcesToOrganize.Bid;
import SourcesToOrganize.ItemInfo;

public class AuctionProxy implements AuctionProcess {

    private ObjectInputStream is;
    private ObjectOutputStream os;

    /**
     * Proxy design for the Auction House. Creates a socket from the passed parameters
     *
     * @param hostname
     * @param port
     */
    AuctionProxy(String hostname, int port) {
        Socket s = null;
        try {
            s = new Socket(hostname, port);

            is = new ObjectInputStream(s.getInputStream());
            os = new ObjectOutputStream(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

//        while(!s.isClosed()) {
//
//            processMessage();
//
//        }
    }

//    private void processMessage() {
//
//    }


    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elements
     * @param itemID Identifier of Item
     */
    @Override
    public void bid(Bid bid, int itemID) {
        // TODO itemID is redundant, as already contained in bid
        AuctionRequest ar = new AuctionRequest(AuctionInfo.BID);
        ar.setBid(bid);

        try {
            os.writeObject(ar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        AuctionRequest ar = new AuctionRequest(AuctionInfo.GET);
        ar.setItemID(itemID);

        try {
            os.writeObject(ar);
            AuctionRequest newAr = (AuctionRequest) is.readObject();
            return newAr.getItem();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public ArrayList<ItemInfo> getItems() {
        AuctionRequest ar = new AuctionRequest(AuctionInfo.GETALL);

        try {
            os.writeObject(ar);
            AuctionRequest newAr = (AuctionRequest) is.readObject();
            return newAr.getItems();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
