package BankProxyTest;

import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

public class BankProxyTest {

    public static void main(String[] args) {
        System.out.println("Starting proxy");

        BankProxy bank = new BankProxy("127.0.0.1", 42069);

        System.out.println("Getting the balance of Account 100");
        System.out.println(bank.getBalance(100));

        System.out.println("Adding the funds 434.67");
        System.out.println(bank.addFunds(100,434.67));

        System.out.println("Removing the funds 267.75");
        System.out.println(bank.removeFunds(100, 267.75));

        System.out.println("Transfer $100 from 100 to 101");
        System.out.println(bank.transferFunds(100,100,101.00));

        System.out.println("I am done!");

        bank.close();
    }

}
