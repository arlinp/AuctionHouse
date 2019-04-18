package AuctionHouse;

import AuctionProxy.AuctionProcess;
import SourcesToOrganize.Bid;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class AuctionHouse implements AuctionProcess {


//    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
//    }

    private ConcurrentHashMap<Integer, Item> items;
//    private ObjectOutputStream os;
//    private ObjectInputStream is;
    private ServerSocket s = null;
    private LinkedBlockingQueue<ItemInfo> itemInfos;




    public AuctionHouse(int port) {
        items = new ConcurrentHashMap<Integer, Item>();

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket s = ss.accept();
                AuctionCommunicator ac = new AuctionCommunicator(s,this);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param item
     * @return
     */
    private int addItem(Item item) {
        items.put(item.getItemID(), item);
        itemInfos.add(item.getItemInfo());

        return 0;
    }

    /**
     *
     * @param itemID
     * @return
     */
    private boolean removeItem(int itemID) {
        return false;
    }

    /**
     *
     * @param itemID
     */
    private void updateItem(int itemID) {

    }


    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elementss
     */
    @Override
    public void bid(Bid bid) {

        Item item = items.get(bid.getItemID());

        item.setBid(bid);

//        System.out.println("I theoretically bid");
//        if (items.containsKey(bid.getItemID())) {
//            Item item = items.get(bid.getItemID());
//
////            if (bid.getAmount() > item.getAmount()) {
////                if (bank.lock(bid.getAccountNumber(), bid.getAmount())) {
////                    item.getBid().unlock(ID)
////                    item.setBid(bid);
////                }
////            }
//
//        }
    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        System.out.println("Returning Null for getItemInfo");
        return null;
    }

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public ArrayList<ItemInfo> getItems() {
        System.out.println("Returning Null for getItems");
        return null;
    }

    /**
     * Test String hello world
     *
     * @param s String to say
     * @return String as a response
     */
    @Override
    public String helloInternet(String s) {
        System.out.println("OMG he said: " + s);

        String sss = "Got ur msg";
        return sss;
    }


    public boolean isAlive() {
        return true;
    }
}
