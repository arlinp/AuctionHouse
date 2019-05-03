package AuctionProxyTest;

import AuctionHouse.AuctionHouse;
import SourcesToOrganize.AgentApp;

import static SourcesToOrganize.AgentApp.auctionPort;
import static SourcesToOrganize.AgentApp.bankPort;

public class AuctionHouseTest {

    private static final boolean COMMANDLINE_TEST = true;

    public static void main(String[] args) {
        System.out.println("Starting auction house on port: 42072");
        if(COMMANDLINE_TEST) {
            AuctionHouse ah = new AuctionHouse(auctionPort, "localhost", bankPort);
        }else {

            AuctionHouse ah = new AuctionHouse(auctionPort, "localhost", bankPort);
        }

    }
}
