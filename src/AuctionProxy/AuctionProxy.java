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

    /**
     * Proxy design for the Auction House. Creates a socket from the passed parameters
     *
     * @param hostname
     * @param port
     */
    public AuctionProxy(String hostname, int port) {
        open = true;
        System.out.println("Creating the proxy");
        try {
            s = new Socket(hostname, port);
            System.out.println("Created the socket " + s.toString() + " " + s.getInputStream());

            os = new ObjectOutputStream(s.getOutputStream());
            System.out.println("Created the OS and IS");

            is = new ObjectInputStream(s.getInputStream());
            System.out.println("Created the IS + " + is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this).start();

        System.out.println("Created the proxy");
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

        try {
            os.writeObject(ar);
            waitOn(ar.getPacketID());

            AuctionRequest response = messages.get(ar.getPacketID());
            return response.getItems();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
//        AuctionRequest ar = new AuctionRequest(AuctionInfo.TEST);
//
//        try {
//            ar.setItemID(1000);
//            ar.setMessage(s);
//            os.writeObject(ar);
//            AuctionRequest response = (AuctionRequest) is.readObject();
//            System.out.println("I found the message: " + response + " AND " + response.getMessage() + " " + response.getType() + " " + response.getItemID());
//            return response.getMessage();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    /**
     * Close the connection
     */
    public void close() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                open = false;
                s.close();
                System.out.println("shut down!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }


    // Separate to handle notifies

    @Override
    public void run() {
        while (isOpen()) {
            AuctionRequest newAr = null;
            try {
                newAr = (AuctionRequest) is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (newAr.getAck()) {
//                System.out.println("Put in hashmap");
//                System.out.println(messages.containsKey(newAr.getPacketID()) + " " + newAr.getPacketID());
                messages.put(newAr.getPacketID(), newAr);
//                System.out.println(messages.containsKey(newAr.getPacketID()) + " " + newAr.getPacketID());

                synchronized (this) { notify(); }
            } else {
                System.out.println("Processing");
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
