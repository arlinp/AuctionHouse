package AuctionHouse;

import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionHouse implements AuctionProcess {


    public static final long ITEM_WAIT_TIME = 10000;


//    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
//    }

    private ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<Integer, Item>();
    private ArrayList<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
    private BankProxy bank;
    private int auctionID;


    public AuctionHouse(int port) {

        bank = new BankProxy("127.0.0.1", 42069, null);
        auctionID = bank.addAccount(8697);

        readInItems();

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bank.openServer("127.0.0.1", port);

        while (true) {
            try {
                Socket s = ss.accept();
                AuctionCommunicator ac = new AuctionCommunicator(s,this);

                System.out.println("Starting to send");
                Thread.sleep(200);

//                ac.notifyBid(BidInfo.OUTBID, 1002, 100.00);
//                ac.notifyBid(BidInfo.WINNER, 1001, 100.00);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void readInItems() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/items.txt"));
            String line;
            String[] lineArr;

            int itemNum = 0;

            while ((line = br.readLine()) != null){

                lineArr = line.split(" ");

                ItemInfo itemInfo = new ItemInfo(lineArr[0], "", System.currentTimeMillis()+40000, Integer.parseInt(lineArr[1]));
                itemInfos.add(itemInfo);
                System.out.println(itemInfo);
                Item item = new Item(bank, this, itemInfo, auctionID);
                items.put(item.getItemID(), item);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Got here");
    }

    /**
     *
     * @param item
     * @return
     */
    private int addItem(Item item) {
        items.put(item.getItemID(), item);
        itemInfos.add(item.getItemInfo());

        return item.getItemID();
    }


    /**
     *
     * @param itemID
     * @return
     */
    public void removeItem(int itemID) {
        if (items.containsKey(itemID)) {
            itemInfos.remove(items.get(itemID).getItemInfo());
            items.remove(itemID);
        }
    }

    // TODO implement?
    /**
     *
     *
     * @param itemID
     */
    private void updateItem(int itemID) {

    }

    /**
     * To place a bid, with the completed bid object
     *
     * @param bid    Bid object that contains elements
     */
    @Override
    public BidInfo bid(Bid bid) {
        // Sanity check
        if (bid == null) return BidInfo.REJECTION;

        // Get the item to be bid upon
        Item item = items.get(bid.getItemID());

        // Set the bid and return the result
        return item.setBid(bid);
    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        if (items.get(itemID) != null) {
            return items.get(itemID).getItemInfo();
        }
        else {
            return null;
        }
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

    public static void main(String[] args) {

        AuctionHouse ah = new AuctionHouse(42070);
    }
}
