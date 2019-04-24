package Agent;

import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

public class Agent {

    BankProxy bankProxy = null;
    AuctionProxy auctionProxy = null;

    public Agent(BankProxy bankProxy, AuctionProxy auctionProxy){

        this.auctionProxy = auctionProxy;
        this.bankProxy = bankProxy;
    }

    public int addAccount(int accountID){

        return bankProxy.addAccount(accountID);
    }
}
