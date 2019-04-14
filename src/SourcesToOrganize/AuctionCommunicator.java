package SourcesToOrganize;

import AuctionProxy.AuctionRequest;

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
    public AuctionCommunicator(Socket s, AuctionHouse auctionHouse) {
        this.s = s;
        this.auctionHouse = auctionHouse;

        try {
            is = new ObjectInputStream(s.getInputStream());
            os = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (auctionCommDebug) System.out.println("Created auction communicator class for " + s.getRemoteSocketAddress());
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
        if (auctionCommDebug) System.out.println("Starting thread for " + s.getInetAddress() + " on thread: " + Thread.currentThread().getName());
        while(s.isConnected() && auctionHouse.isAlive()) {
            try {
                AuctionRequest ar = (AuctionRequest) is.readObject();
                processMessage(ar);
            } catch (IOException | ClassNotFoundException e) {
                System.out.print("OH NO");
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
        AuctionRequest newAR = new AuctionRequest(ar.getType());
        switch (ar.getType()) {
            case BID:
                auctionHouse.bid(ar.getBid());
                break;
            case GET:
                newAR.setItemInfo(auctionHouse.getItemInfo(ar.getItemID()));
                break;
            case GETALL:
                newAR.setItems(auctionHouse.getItems());
                break;
            case TEST:
                String s = auctionHouse.helloInternet(ar.getTest());
                System.out.println(s + " was the response");
                newAR.setTest(s);
        }
        try {
            System.out.println("CHECK FOR SETTING " + newAR.getTest());
            newAR.setItemID(1234);
            os.writeObject(newAR);

            System.out.println("sent the message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
