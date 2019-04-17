package Bank;

import Bank.Bank;
import BankProxy.BankRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BankCommunicator implements Runnable {

    private Socket s;
    private Bank bank;
    private ObjectInputStream is;
    private ObjectOutputStream os;

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
        System.out.println("Starting thread for " + s.getInetAddress() + " on thread: " + Thread.currentThread().getName());
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
        BankRequest response = new BankRequest(br.getType());
        // TODO Implement the rest of this
        switch (br.getType()) {
            case GETBALANCE:
                response.setAmount(bank.getBalance(br.getID()));
                break;
            case ADD:
                response.setStatus(bank.addFunds(br.getID(), br.getAmount()));
                break;
            case REMOVE:
                response.setStatus(bank.removeFunds(br.getID(),br.getAmount()));
                break;
            case LOCK:
                response.setLockNumber(bank.lockFunds(br.getID(), br.getAmount()));
                break;
            case UNLOCK:
                response.setStatus(bank.unlockFunds(br.getID(), br.getLockNumber()));
                break;
            case TRANSFER:
                response.setStatus(bank.transferFunds(br.getID(), br.getToID(), br.getAmount()));
                break;
            case TRANSFERFROMLOCK:
                response.setStatus(bank.transferFunds(br.getID(), br.getToID(), br.getLockNumber()));
                break;
        }
        try {

            os.writeObject(response);
            System.out.println("sent the message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
