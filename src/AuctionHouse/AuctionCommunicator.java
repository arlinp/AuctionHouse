package AuctionHouse;

import AuctionProxy.AuctionInfo;
import AuctionProxy.AuctionRequest;
import AuctionProxy.BidInfo;
import SourcesToOrganize.Bid;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionCommunicator implements Runnable{

    private Socket s;
    private AuctionHouse auctionHouse;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private static boolean auctionCommDebug = true;

    /**
     * Thread for communication with a single socket
     *
     * @param s Socket for communication
     * @param auctionHouse AuctionHouse reference
     */
    AuctionCommunicator(Socket s, AuctionHouse auctionHouse) {
        this.s = s;
        this.auctionHouse = auctionHouse;

        try {
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created auction communicator class for " +
                s.getRemoteSocketAddress());
        new Thread(this).start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Starting thread for " + s.getInetAddress() +
                " on thread: " + Thread.currentThread().getName());
        while(s.isConnected() && auctionHouse.isAlive()) {
            try {
                AuctionRequest ar = (AuctionRequest) is.readObject();
                processMessage(ar);
            } catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
                System.out.println("Agent " + s + " disconnected from ");
                break;
            }
        }
    }

    /**
     * Process the AuctionRequest and the respond appropriately
     *
     * @param ar AuctionRequest to handle
     */
    private void processMessage(AuctionRequest ar) {
        System.out.println("THE TYPE OF REQUEST IS: " + ar.getType());
        AuctionRequest resp =new AuctionRequest(ar.getType(),ar.getPacketID());

        try {
            // Switch on the types
            switch (ar.getType()) {
                case BID:
                    // Bid the item and print results
                    Bid bid = ar.getBid();
                    bid.setAc(this);
                    BidInfo status = auctionHouse.bid(bid);
                    System.out.println(status);
                    resp.setBidStatus(status);

                    System.out.println("\tBid on item #" + bid.getItemID() +
                            " with $" + bid.getAmount());
                    System.out.println("\tThe bid was " + status);
                    break;
                case GET:
                    // Get the items and display process
                    resp.setItemInfo(auctionHouse.getItemInfo(ar.getItemID()));
                    System.out.println("\tThe item #" + ar.getItemID() +
                            " was gotten");
                    break;
                case GETALL:
                    // Get the items and display process
                    resp.setItems(auctionHouse.getItems());
                    System.out.println("\tAll of the items were gotten");
                    break;
                case CLOSEREQUEST:
                    // Request to close the client
                    resp.setRequest(auctionHouse.closeRequest(ar.getItemID()));
                    System.out.println("\tChecked if " + ar.getItemID() +
                            " can leave");
                    break;
            }

            // Write out the object
            os.writeObject(resp);
            System.out.println("\tSent the response\n");
        } catch (IOException e) {
            System.out.println("Socket was disconnected");
        }
    }

    /**
     * Notify the recipient of bid status
     *
     * @param status bid status
     * @param info item bid on
     * @param amount Amount of the bid
     */
    public void notifyBid(BidInfo status, ItemInfo info, double amount) {
        AuctionRequest ar = new AuctionRequest(AuctionInfo.BID);
        ar.setAck(false);
        ar.setBidStatus(status);
        ar.setItemInfo(info);
        ar.setNewAmount(amount);

        try {
            os.writeObject(ar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
