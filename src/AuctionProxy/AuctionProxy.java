package AuctionProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import SourcesToOrganize.Bid;
import AuctionHouse.ItemInfo;

public class AuctionProxy implements AuctionProcess,Runnable {

    private LinkedBlockingQueue<AuctionRequest> messages = new LinkedBlockingQueue<>();
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private Socket s;
    boolean open;

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

        System.out.println("Created the proxy");
    }

//    private void processMessage() {
//
//    }


    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elements
     */
    @Override
    public BidInfo bid(Bid bid) {
        // TODO itemID is redundant, as already contained in bid
        AuctionRequest ar = new AuctionRequest(AuctionInfo.BID);
        ar.setBid(bid);

        // TODO Need to reimplement BID to project specification (e.g. Returns various decisions)
        try {
            os.writeObject(ar);
            AuctionRequest newAr = (AuctionRequest) is.readObject();
            return newAr.getBidStatus();
        } catch (IOException | ClassNotFoundException e) {
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

            AuctionRequest response = messages.take();
            return response.getItem();

        } catch (IOException | InterruptedException e) {
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

    /**
     * Test String hello world
     *
     * @param s String to say
     * @return String as a response
     */
    @Override
    public String helloInternet(String s) {
        AuctionRequest ar = new AuctionRequest(AuctionInfo.TEST);

        try {
            ar.setItemID(1000);
            ar.setMessage(s);
            os.writeObject(ar);
            AuctionRequest newAr = (AuctionRequest) is.readObject();
            System.out.println("I found the message: " + newAr + " AND " + newAr.getMessage() + " " + newAr.getType() + " " + newAr.getItemID());
            return newAr.getMessage();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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
                messages.add(newAr);
                synchronized (this) {
                    notify();
                }
            } else {
                System.out.println(newAr.getMessage());
            }


        }
    }

    private boolean isOpen() {
        return open;
    }
}
