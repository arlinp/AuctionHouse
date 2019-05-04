package Bank;

import BankProxy.BankInfo;
import BankProxy.BankRequest;
import SourcesToOrganize.NetworkDevice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Dedicated thread for a socket connection
 */
public class BankCommunicator implements Runnable {

    private Socket s;
    private Bank bank;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    private boolean auctionCommunicator = false;

    /**
     * Thread for communication with a single socket
     *
     * @param s Socket for communication
     * @param bank AuctionHouse reference
     */
    public BankCommunicator(Socket s, Bank bank) {

        // Create sockets that are passed
        this.s = s;
        this.bank = bank;

        // Create new ObjectDataStreams
        try {
            is = new ObjectInputStream(s.getInputStream());
            os = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start new thread for processing messages
        new Thread(this).start();
    }

    /**
     * Runs thread and processes messages
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (Bank.BANKCOMMDEBUG) System.out.println("Starting thread for " + s.getInetAddress() + " on thread: " + Thread.currentThread().getName());

        // Check for connection and aliveness
        while(s.isConnected() && bank.isAlive()) {
            try {
                // Process BankRequest from input stream
                BankRequest br = (BankRequest) is.readObject();

                // Throw error if br is not processable
                if (br == null) throw new ClassNotFoundException();

                // Process messages
                processMessage(br);
            } catch (IOException | ClassNotFoundException e) {
                if (Bank.BANKCOMMDEBUG) {
                    System.out.println("Error occured!");
                    System.out.println("Handle closing better");
                    //e.printStackTrace();

                    try {
                        s.close();
                    } catch (IOException e1) {
                        System.out.println("WASN'T ABLE TO CLOSE PROPERLY");
                        e1.printStackTrace();
                    }


                }
                break;
            }
        }

        if (Bank.BANKCOMMDEBUG) System.out.println("Connection broke for " + s.getInetAddress());
    }

    /**
     * Process the AuctionRequest and the respond appropriately
     *
     * @param br AuctionRequest to handle
     */
    private void processMessage(BankRequest br) {
        if (Bank.BANKCOMMDEBUG) System.out.println("THE TYPE OF REQUEST IS: " + br.getType());

        // Create a response for br
        BankRequest response = new BankRequest(br.getType(), br.getPacketID());

        // Check type and map appropriate actions
        switch (br.getType()) {
            case NEWACCOUNT: // Create a new account
//                int accountID = bank.addAccount(br.getID());
                int accountID = bank.addAccount();

                response.setID(accountID);

                if (Bank.BANKCOMMDEBUG) System.out.println("\tNew Account Created: " + accountID);
                break;
            case GETBALANCE: // Get the balance of the given account
                double balance = bank.getBalance(br.getID());
                response.setAmount(balance);

                if (Bank.BANKCOMMDEBUG) System.out.println("\tBalance for Account#: " + br.getID() + " is $" + balance);
                break;
            case ADD: // AddFunds to a given Account
                double bal1 = bank.getBalance(br.getID());
                response.setStatus(bank.addFunds(br.getID(), br.getAmount()));
                double bal2 = bank.getBalance(br.getID());

                if (Bank.BANKCOMMDEBUG) System.out.println("\tAdded funds to Account#: " + br.getID() + " | Amount Added: $" + br.getAmount() + " | Old Balance: $" + bal1 + " | New Balance: $" + bal2);
                break;
            case REMOVE: // RemoveFunds from given account
                double bal3 = bank.getBalance(br.getID());
                response.setStatus(bank.removeFunds(br.getID(),br.getAmount()));
                double bal4 = bank.getBalance(br.getID());

                if (Bank.BANKCOMMDEBUG) System.out.println("\tAdded funds to Account#: " + br.getID() + " | Amount Added: $" + br.getAmount() + " | Old Balance: $" + bal3 + " | New Balance: $" + bal4);
                break;
            case LOCK: // Lock funds of the given account
                int lockNumber = bank.lockFunds(br.getID(), br.getAmount());
                response.setLockNumber(lockNumber);

                if (Bank.BANKCOMMDEBUG) System.out.println("\tLocked funds to Account#: " + br.getID() + " | Amount locked: $" + br.getAmount() + " | Lock Number: $" + lockNumber);
                break;
            case UNLOCK: // Unlock funds of the given account
                response.setStatus(bank.unlockFunds(br.getID(), br.getLockNumber()));

                if (Bank.BANKCOMMDEBUG) System.out.println("\tUnlocked funds for Account#: " + br.getID());
                break;
            case TRANSFER: // Transfer funds from account1 to account2
                response.setStatus(bank.transferFunds(br.getID(), br.getToID(), br.getAmount()));

                if (Bank.BANKCOMMDEBUG) System.out.println("\tTransferred $" + br.getAmount() + " from Account#: " + br.getID() + " to Account#: " + br.getToID());
                break;
            case TRANSFERFROMLOCK: // Transfer funds based on a lock
                response.setStatus(bank.transferFunds(br.getID(), br.getToID(), br.getLockNumber()));

                if (Bank.BANKCOMMDEBUG) System.out.println("\tTransferred $" + br.getLockNumber() + " from Account#: " + br.getID() + " to Account#: " + br.getToID());
                break;
            case OPENAUCTION:
                auctionCommunicator = true;
                response.setStatus(true);
                bank.openServer(br.getNetworkDevice());

                bank.notifyAuction(br.getNetworkDevice());

                if (Bank.BANKCOMMDEBUG) System.out.println("\tNew Server on " + br.getNetworkDevice());
                break;
            case CLOSEAUCTION:
                response.setStatus(true);
                bank.closeServer(br.getNetworkDevice());

                if (Bank.BANKCOMMDEBUG) System.out.println("\tStopped distributing the server of " + br.getNetworkDevice());
                break;
            case GETAUCTIONS:
                LinkedBlockingQueue<NetworkDevice> list = bank.getServers();

                if (Bank.BANKCOMMDEBUG) {
                    System.out.print("\tSending the following servers: \n\t");
                    for (NetworkDevice nd : list) {
                        System.out.print(nd + " ");
                    }
                    System.out.println();
                }
                break;
        }

        try {
            // Write a response back
            os.writeObject(response);
            if (Bank.BANKCOMMDEBUG) System.out.println("\tSENT MESSAGE\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void notifyNewAuction(NetworkDevice networkDevice) {
        BankRequest ar = new BankRequest(BankInfo.OPENAUCTION);
        ar.setAck(false);
        ar.addNetworkDevices(networkDevice);

        try {
            os.writeObject(ar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
