package AuctionProxyTest;

import AuctionProxy.AuctionProxy;

public class AuctionProxyTest {

    public static void main(String[] args) {
        System.out.println("Starting proxy");
        AuctionProxy auction = new AuctionProxy("127.0.0.1", 42069);

        System.out.println("Bidding a null value");
        auction.bid(null);

        System.out.println("Getting the item:");
        System.out.println(auction.getItems());

        System.out.println("Getting the items");
        System.out.println(auction.getItemInfo(127));

        System.out.println("Transmitting message");
        System.out.println(auction.helloInternet("Hello bruh"));
        System.out.println("I said hello");

//        auction.close();
    }
}
