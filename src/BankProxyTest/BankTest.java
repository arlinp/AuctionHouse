package BankProxyTest;

import AuctionHouse.AuctionHouse;

public class BankTest {

    public static void main(String[] args) {
        System.out.println("Starting auction house on port: 42069");
        AuctionHouse ah = new AuctionHouse(42069);
    }
}
