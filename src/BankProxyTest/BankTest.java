package BankProxyTest;

import AuctionHouse.AuctionHouse;
import Bank.Bank;

public class BankTest {

    public static void main(String[] args) {
        System.out.println("Starting auction house on port: 42069");
        Bank ah = new Bank(2000);
    }
}
