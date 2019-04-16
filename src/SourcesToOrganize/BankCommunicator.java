package SourcesToOrganize;

import BankProxy.BankRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import static BankProxy.BankInfo.GETBALANCE;

public class BankCommunicator implements Runnable {

    private Socket s;
    private Bank bank;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private static boolean auctionCommDebug = true;

    /**
     * Thread for communication with a single socket
     *
     * @param s Socket for communication
     * @param bank AuctionHouse reference
     */
    public BankCommunicator(Socket s, Bank bank) {
        this.s = s;
        this.bank = bank;

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
        while(s.isConnected() && bank.isAlive()) {
            try {
                BankRequest br = (BankRequest) is.readObject();
                processMessage(br);
            } catch (IOException | ClassNotFoundException e) {
                System.out.print("OH NO");
                break;
            }
        }
    }

    /**
     * Process the AuctionRequest and the respond appropriately
     *
     * @param br AuctionRequest to handle
     */
    private void processMessage(BankRequest br) {
        System.out.println("THE TYPE OF REQUEST IS: " + br.getType());
        BankRequest newAR = new BankRequest(br.getType());
        // TODO Implement the rest of this
        switch (br.getType()) {
            case GETBALANCE:
                break;
            case ADD:
                break;
            case REMOVE:
                break;
        }
        try {


//            System.out.println("CHECK FOR SETTING " + newAR.getTest() + " " + newAR.getType() + " " + br.getType());
//            newAR.setItemID(1234);
//            newAR.setTest("Testing12");
            os.writeObject(newAR);

            System.out.println("sent the message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
