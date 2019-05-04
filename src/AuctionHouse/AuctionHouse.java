package AuctionHouse;

import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import SourcesToOrganize.AgentApp;
import SourcesToOrganize.Bid;
import SourcesToOrganize.NetworkDevice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static SourcesToOrganize.AgentApp.auctionPort;
import static SourcesToOrganize.AgentApp.bankPort;

public class AuctionHouse implements AuctionProcess {


    public static long waitTime = 50000;


//    public AuctionHouse(ClientProxy bankProxy, ServerProxy auctionHouseServer) {
//    }

    private ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<Integer, Item>();
    private ArrayList<Item> itemsNotUpForAuction = new ArrayList<Item>();
    private ArrayList<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
    private ArrayList<Bid> bids;
    private BankProxy bankProxy;
    private int auctionID;
    private BufferedReader bufferedReader;
    private static int counter = 0;
    private boolean alive = true;


    public AuctionHouse(int operatingPort, String bankHostname, int bankPort) {

        bankProxy = new BankProxy(bankHostname, bankPort, null);
//        auctionID = bankProxy.addAccount(8697);
        bankProxy.addAccount();

        readInItems();

        ServerSocket ss = null;
        try {
            System.out.println(operatingPort);
            ss = new ServerSocket(operatingPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bankProxy.openServer(new NetworkDevice("127.0.0.1",operatingPort));

        while (true) {
            try {
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

            while ((line = br.readLine()) != null){

                lineArr = line.split(" ");

                ItemInfo itemInfo = new ItemInfo(lineArr[0], Integer.parseInt(lineArr[1]), counter++);
                Item item = new Item(bankProxy, this, itemInfo, auctionID);

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
    public synchronized void removeItem(int itemID) {
        if (items.containsKey(itemID)) {

            itemInfos.remove(items.get(itemID).getItemInfo());
            items.remove(itemID);

            // Add a new item to replace it
            if(!itemsNotUpForAuction.isEmpty()) {

                Item itemUp = itemsNotUpForAuction.remove(0);
                ItemInfo itemInfo = itemUp.getItemInfo();
                itemInfos.add(itemInfo);
                System.out.println(itemInfo);
                items.put(itemUp.getItemID(), itemUp);
                itemUp.startThread();

            }else{
//                if(items.isEmpty()){
//
//                    alive = false;
//                }
                System.out.println("Auction House says: No more items in the Auction house!");}
        }
    }


    /**
     * To place a bid, with the completed bid object
     *
     * @param bid    Bid object that contains elements
     */
    @Override
    public BidInfo bid(Bid bid) {
        // Sanity check
        if (bid == null || !items.containsKey(bid.getItemID())) {
            return BidInfo.REJECTION;
        }

        // Get the item to be bid upon
        Item item = items.get(bid.getItemID());
        BidInfo info = item.setBid(bid);
        addBid(bid);

        return info;
    }

    public synchronized void addBid(Bid bid){
        bids.add(bid);
    }

    public synchronized void removeBid(Bid bid){
        bids.remove(bid);
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
     * Check to see if the close is allowed
     *
     * @param accountID Account ID to be used for checking
     * @return True if no active bids, false if active bids
     */
    @Override
    public boolean closeRequest(int accountID) {
        System.out.println("DID IT EVER EVEN CHECK?");
        for (Item item : items.values()) {
            if (item.contains(accountID)) {
                System.out.println("False on" +item);
                return false;
            }
        }
        System.out.println("returned true");
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

    public static void main(String[] args) {
        if (args.length == 4) {

            int operatingPort;
            int bankPort;

            try {
                operatingPort = Integer.parseInt(args[0]);
                bankPort = Integer.parseInt(args[2]);
                waitTime = Long.parseLong(args[3]);

            } catch (NumberFormatException e) {
                System.out.println("Input not correct:\n Correct usage: Auct" +
                        "ionHouse <Operating Port> <Bank Host> <Bank Port> <" +
                        "Wait Time>");
                return;
            }

            AuctionHouse ah = new AuctionHouse(operatingPort,args[1],bankPort);
        } else {
            AuctionHouse ah = new AuctionHouse(auctionPort, "localhost", bankPort);
        }



    }
}
