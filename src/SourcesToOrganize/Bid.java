package SourcesToOrganize;

import AuctionHouse.AuctionCommunicator;

import java.io.Serializable;

public class Bid implements Serializable {

    private double amount;
    private int accountNumber;
    private int lockID;
    private int itemID;
    private AuctionCommunicator ac;

    public Bid(double amount, int accountNumber, int itemID) {
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.itemID = itemID;
    }

    public double getAmount() {
        return amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getLockID() {
        return lockID;
    }

    public void setLockID(int lockID) {
        this.lockID = lockID;
    }

    public AuctionCommunicator getAc() {
        return ac;
    }

    public void setAc(AuctionCommunicator ac) {
        this.ac = ac;
    }
}
