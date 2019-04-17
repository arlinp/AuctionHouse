package AuctionHouse;

import AuctionProxy.AuctionProcess;
import SourcesToOrganize.Bid;
import SourcesToOrganize.Item;
import SourcesToOrganize.ItemInfo;

import java.io.IOException;
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
    private ArrayList<ItemInfo> itemInfos;




    public AuctionHouse(int port) {
        items = new HashMap<Integer, Item>();

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
     * To place a bid
     *
     * @param bid    Bid object that contains elementss
     */
    @Override
    public void bid(Bid bid) {

        System.out.println("I theoretically bid");
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
