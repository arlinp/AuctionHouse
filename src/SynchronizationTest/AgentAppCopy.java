package SynchronizationTest;

import static SourcesToOrganize.AgentApp.auctionPort;
import static SourcesToOrganize.AgentApp.bankPort;

public class AgentAppCopy{


    public static void main(String[] args) {


        //Start the bidders with their accounts
        Bidder Bidder1 = new Bidder(1, bankPort, auctionPort);
        Bidder Bidder3 = new Bidder(3, bankPort, auctionPort);
//        Bidder Bidder4 = new Bidder(4, 42069, 42070);
//        Bidder Bidder5 = new Bidder(5, 42069, 42070);
//        Bidder Bidder6 = new Bidder(6, 42069, 42070);
//        Bidder Bidder7 = new Bidder(7, 42069, 42070);
//        Bidder Bidder8 = new Bidder(8, 42069, 42070);
//        Bidder Bidder9 = new Bidder(9, 42069, 42070);
//        Bidder Bidder10 = new Bidder(10, 42069, 42070);
//        Bidder Bidder11 = new Bidder(11, 42069, 42070);
//        Bidder Bidder12 = new Bidder(12, 42069, 42070);
//        Bidder Bidder13 = new Bidder(13, 42069, 42070);
//        Bidder Bidder14 = new Bidder(14, 42069, 42070);

        new Thread(Bidder1).start();
//        new Thread(Bidder2).start();
        new Thread(Bidder3).start();
//        new Thread(Bidder4).start();
//        new Thread(Bidder5).start();
//        new Thread(Bidder6).start();
//        new Thread(Bidder7).start();
//        new Thread(Bidder8).start();
//        new Thread(Bidder9).start();
//        new Thread(Bidder10).start();
//        new Thread(Bidder11).start();
//        new Thread(Bidder12).start();
//        new Thread(Bidder13).start();
//        new Thread(Bidder14).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bidder Bidder2 = new Bidder(2, bankPort, auctionPort);
        new Thread(Bidder2).start();

    }

}
