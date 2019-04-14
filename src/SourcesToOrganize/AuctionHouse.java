package SourcesToOrganize;

import AuctionProxy.AuctionProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class AuctionHouse implements AuctionProcess {


//    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
//    }

    private HashMap<Integer, Item> items;
//    private ObjectOutputStream os;
//    private ObjectInputStream is;
    private ServerSocket s = null;

    AuctionHouse(int port) {
        items = new HashMap<Integer, Item>();

        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        new Thread(this).start();
    }

    // TODO Research methods of socket storage
//    /**
//     * The general contract of the method run is that it may
//     * take any action whatsoever.
//     *
//     * @see Thread#run()
//     */
//    @Override
//    public void run() {
//        while (true) {
//            Socket s1 = s.accept();
//
//
//            s1.close();
//        }
//    }

    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elements
     * @param itemID Identifier of Item
     */
    @Override
    public void bid(Bid bid, int itemID) {
        if (items.containsKey(bid.getItemID())) {
            Item item = items.get(bid.getItemID());

//            if (bid.getAmount() > item.getAmount()) {
//                if (bank.lock(bid.getAccountNumber(), bid.getAmount())) {
//                    item.getBid().unlock(ID)
//                    item.setBid(bid);
//                }
//            }

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
