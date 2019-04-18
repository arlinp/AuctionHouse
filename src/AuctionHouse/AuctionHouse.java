package AuctionHouse;

import AuctionProxy.AuctionProcess;
import SourcesToOrganize.Bid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
//    private LinkedBlockingQueue<ItemInfo> itemInfos;

    private ArrayList<ItemInfo> itemInfos;





    public AuctionHouse(int port) {

        itemInfos = new ArrayList<ItemInfo>();
        items = new ConcurrentHashMap<Integer, Item>();
        readInItems();

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

    private void readInItems() {


        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/items.txt"));
            String line;
            String[] lineArr;
            Item item;
            ItemInfo itemInfo;

            int itemNum = 0;

            while ((line = br.readLine()) != null){

                lineArr = line.split(" ");

                itemInfo = new ItemInfo(lineArr[0], "", 0, Integer.parseInt(lineArr[1]));
                itemInfos.add(itemInfo);

                item = new Item(null, itemInfo);
                items.put(itemNum, item);

            }


        } catch (IOException e){
            e.printStackTrace();
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
        return itemInfos;
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
