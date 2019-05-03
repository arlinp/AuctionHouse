package AuctionProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import SourcesToOrganize.Bid;
import AuctionHouse.ItemInfo;

public class AuctionProxy implements AuctionProcess, Runnable {

    private ConcurrentHashMap<Integer, AuctionRequest> messages = new ConcurrentHashMap<Integer, AuctionRequest>();
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private Socket s;
    private boolean open;

    private String hostname;
    private int port;

    /**
     * Proxy design for the Auction House. Creates a socket from the passed parameters
     *
     * @param hostname
     * @param port
     */
    public AuctionProxy(String hostname, int port) {
        open = true;
        System.out.println("Creating the proxy");

        this.hostname = hostname;
        this.port = port;

        connectToServer(hostname, port);

        new Thread(this).start();

        System.out.println("Created the proxy");
    }

    private void connectToServer(String hostname, int port) {
        try {
            s = new Socket(hostname, port);
            System.out.println("Created the socket " + s.toString() + " " + s.getInputStream());

            os = new ObjectOutputStream(s.getOutputStream());
            System.out.println("Created the OS and IS");

            is = new ObjectInputStream(s.getInputStream());
            System.out.println("Created the IS + " + is);
//
//            os = new ObjectOutputStream(s.getOutputStream());
//            is = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            connectToServer(hostname, port);
            e.printStackTrace();
        }
    }

    /**
     * To place a bid
     *
     * @param bid Bid object that contains elements
     */
    @Override
    public BidInfo bid(Bid bid) {
        // TODO itemID is redundant, as already contained in bid
        AuctionRequest ar = new AuctionRequest(AuctionInfo.BID);
        ar.setBid(bid);

        // TODO Need to reimplement BID to project specification (e.g. Returns various decisions)
        try {
            os.writeObject(ar);
            waitOn(ar.getPacketID());

            AuctionRequest response = messages.get(ar.getPacketID());
            messages.remove(ar.getPacketID());
            return response.getBidStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BidInfo.REJECTION;
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
            waitOn(ar.getPacketID());

            AuctionRequest response = messages.get(ar.getPacketID());
            messages.remove(ar.getPacketID());

            ItemInfo itemInfo = (ItemInfo) response.getItem().clone();
            System.out.println(itemInfo);
            response.setItems(null);
            return response.getItem();
        } catch (IOException e) {
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
        ar.setItems(null);

        try {
            os.writeObject(ar);

            waitOn(ar.getPacketID());

            AuctionRequest response = messages.get(ar.getPacketID());
            messages.remove(ar.getPacketID());

            return response.getItems();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Check to see if the close is allowed
     *
     * @param accountID Account ID to be used for checking
     * @return True if no active bids, false if active bids
     */
    @Override
    public boolean closeRequest(int accountID) {
        System.out.println("Checking request");
        AuctionRequest ar = new AuctionRequest(AuctionInfo.CLOSEREQUEST);
        ar.setItemID(accountID);

        try {
            os.writeObject(ar);

            waitOn(ar.getPacketID());

            AuctionRequest response = messages.get(ar.getPacketID());
            messages.remove(response.getPacketID());
            return response.isContains();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Close the connection
     */
    public void close() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                open = false;
//                s.close();
//                System.out.println("shut down!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            System.out.println("shut down!");
        }));
    }


    // Separate to handle notifies

    @Override
    public void run() {
        while (isOpen()) {
            // Attempt to read in an AR from the input stream
            AuctionRequest newAr = null;
            try {
                newAr = (AuctionRequest) is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Either notify or process immediately
            if (newAr.getAck()) {
                messages.put(newAr.getPacketID(), newAr);
                synchronized (this) { notify(); }
            } else {
                processMessage(newAr);
            }
        }
    }

    private void processMessage(AuctionRequest newAr) {
        switch(newAr.getType()) {
            case BID:
                switch (newAr.getBidStatus()) {
                    case OUTBID:
                        int itemID = newAr.getItemID();
                        double amount = newAr.getNewAmount();

                        System.out.println("You were outbid on item# " +
                                itemID + ". The new bid is " + amount);
                        break;
                    case WINNER:
                        int itemWon = newAr.getItemID();
                        double amountPaid = newAr.getNewAmount();

                        System.out.println("You won item# " + itemWon + ". T" +
                                "here was $" + amountPaid + " transferred fr" +
                                "om your bank account. Please allow 6-8 week" +
                                "s in delivery for your item to arrive");

                        break;
                }
                break;
        }
    }

    public void closeDown(){
        if(messages.isEmpty()){
            close();
        }
    }

    private boolean isOpen() {
        return open;
    }

    /**
     * Wait on a given packetID
     *
     * @param packetID Wait until messages contains key
     */
    public void waitOn(int packetID) {
        synchronized (this) {
            while (!messages.containsKey(packetID)) {
                try {
                    System.out.println("Waiting on " + packetID);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
