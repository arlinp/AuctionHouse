package SourcesToOrganize;

import AuctionHouse.AuctionCommunicator;

import java.io.Serializable;

/**
 * A bid object created when an Agent places a bid
 */
public class Bid implements Serializable {

    private double amount;
    private int accountNumber;
    private int lockID;
    private int itemID;
    private AuctionCommunicator ac;

    /**
     * Constructor for a bid object
     * assigns the parameters to fields
     *
     * @param amount bid amount
     * @param accountNumber who placed the bid
     * @param itemID what was bid on
     */
    public Bid(double amount, int accountNumber, int itemID) {
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.itemID = itemID;
    }

    /**
     * @return bid amoun
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return Account number of Agent who sent bid
     */
    public int getAccount() {
        return accountNumber;
    }

    /**
     * @return ID number of the item bid on
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * @param itemID set item that bid is on
     */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * @return get lock
     */
    public int getLockID() {
        return lockID;
    }

    /**
     * @param lockID set lock
     */
    public void setLockID(int lockID) {
        this.lockID = lockID;
    }

    /**
     * @return get Auction this bid corresponds to
     */
    public AuctionCommunicator getAc() {
        return ac;
    }

    /**
     * @param ac set the Auction this bid corresponds to
     */
    public void setAc(AuctionCommunicator ac) {
        this.ac = ac;
    }

}
