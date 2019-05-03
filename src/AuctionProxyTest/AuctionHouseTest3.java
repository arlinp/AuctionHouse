package AuctionProxyTest;

import AuctionHouse.AuctionHouse;

import static SourcesToOrganize.AgentApp.auctionPort;
import static SourcesToOrganize.AgentApp.bankPort;

public class AuctionHouseTest3 {

    public static void main(String[] args) {
        System.out.println("Starting auction house on port: 2001");
        AuctionHouse ah = new AuctionHouse(auctionPort+2, "localhost", bankPort);


    }
}
