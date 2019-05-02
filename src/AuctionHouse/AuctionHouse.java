package AuctionHouse;

import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;
import SourcesToOrganize.NetworkDevice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionHouse implements AuctionProcess {


    public static final long ITEM_WAIT_TIME = 25000;


//    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
//    }

    private ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<Integer, Item>();
    private ArrayList<Item> itemsNotUpForAuction = new ArrayList<Item>();
    private ArrayList<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
    private BankProxy bank;
    private int auctionID;
    private BufferedReader bufferedReader;
    private static int counter = 0;


    public AuctionHouse(int port) {

        bank = new BankProxy("127.0.0.1", 42069, null);
        auctionID = bank.addAccount(8697);

        readInItems();

        ServerSocket ss = null;
        try {
            System.out.println(port);
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bank.openServer(new NetworkDevice("127.0.0.1",port));

        while (true) {
            try {
                System.out.println("Accepting connections");
                Socket s = ss.accept();
                AuctionCommunicator ac = new AuctionCommunicator(s,this);

                System.out.println("Starting to send");
//                Thread.sleep(200);

//                ac.notifyBid(BidInfo.OUTBID, 1002, 100.00);
//                ac.notifyBid(BidInfo.WINNER, 1001, 100.00);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void readInItems() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/items.txt"));
            bufferedReader = br;
            String line;
            String[] lineArr;

            int itemNum = 0;

//            for(int i = 0; i < 3; i++){
//                line = br.readLine();
//                if(!line.equals(null)) {
//                    lineArr = line.split(" ");
//
//                    ItemInfo itemInfo = new ItemInfo(lineArr[0], "", System.currentTimeMillis() + 40000, Integer.parseInt(lineArr[1]));
//                    itemInfos.add(itemInfo);
//                    System.out.println(itemInfo);
//
//                    Item item = new Item(bank, this, itemInfo, auctionID);
//                    items.put(item.getItemID(), item);
//                    item.startThread();
//                }
//            }
            while ((line = br.readLine()) != null){

                lineArr = line.split(" ");

                ItemInfo itemInfo = new ItemInfo(lineArr[0], "", System.currentTimeMillis()+40000, Integer.parseInt(lineArr[1]));
                Item item = new Item(bank, this, itemInfo, auctionID);

                //start the first three item threads
                if(itemNum < 3){
                    itemInfos.add(itemInfo);
                    System.out.println(itemInfo);
                    items.put(item.getItemID(), item);
                    item.startThread();
                    itemNum++;
                }
                else {
                    //itemsNotUpForAuction.put(item.getItemID(), item);
                    itemsNotUpForAuction.add(item);
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Got here");
    }

    // TODO check when to close an auction house and gracefully close it
    // TODO in proxy, check if we can leave an Auction house


    /**
     *
     * @param itemID
     * @return
     */
    public void removeItem(int itemID) {
        if (items.containsKey(itemID)) {
            int index = itemInfos.indexOf(items.get(itemID).getItemInfo());
            itemInfos.remove(items.get(itemID).getItemInfo());
            Item item = items.remove(itemID);

            // Add a new item to replace it
                if(!itemsNotUpForAuction.isEmpty()) {

                    Item itemUp = itemsNotUpForAuction.remove(0);
                    items.put(itemUp.getItemID(), itemUp);
                    itemUp.startThread();
                }else{
                    System.out.println("No more items in the Auction house!");}
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
        if (bid == null || !items.containsKey(bid.getItemID())
        ) {
            System.out.println("Reject with " + bid.getItemID() + " FOR " + items);
            return BidInfo.REJECTION;
        }

        // Get the item to be bid upon
        Item item = items.get(bid.getItemID());

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
