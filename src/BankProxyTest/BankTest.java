package BankProxyTest;

import AuctionHouse.AuctionHouse;
import Bank.Bank;

import static SourcesToOrganize.AgentApp.bankPort;

public class BankTest {

    public static void main(String[] args) {
        System.out.println("Starting auction house on port: 42073");
        Bank ah = new Bank(bankPort);


    }
}
