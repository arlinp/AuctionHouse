package AuctionHouse;

import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;

public class Item implements Runnable {

    private BankProxy bank;
//    private Bank bank;
    private Bid bid;
    private int auctionID;
    private ItemInfo itemInfo;
    private int itemID;
    private boolean open = true;

    public Item(BankProxy bank, ItemInfo itemInfo, int auctionID) {
        this.bank = bank;
        this.itemInfo = itemInfo;
        this.itemID = itemInfo.getItemID();
        this.auctionID = auctionID;

        new Thread(this).start();
    }

    /**
     * Checks and then sets the bid.
     *
     * Synchronized because it can be called twice at the same time!
     * TODO Maybe synchronize on bid?
     *
     * @param bid
     * @return
     */
    public synchronized BidInfo setBid(Bid bid) {
        if (!open) {
            System.out.println("Auction is over!");
            return BidInfo.REJECTION;
        }

        if (bid.getAmount() > itemInfo.getPrice()) {
            System.out.println(bank + " " +
                    bid.getAccountNumber());
            int lockID = bank.lockFunds(bid.getAccountNumber(), bid.getAmount());

            if (lockID == -1 || lockID == 0) {
                System.out.println("Not enough funds");
                return BidInfo.REJECTION;
            }

            if (this.bid != null) {
                bank.unlockFunds(this.bid.getAccountNumber(), this.bid.getLockID());
            }

            synchronized (itemInfo) { itemInfo.setPrice(bid.getAmount()); }

            if (this.bid != null) {
                this.bid.getAc().notifyBid(BidInfo.OUTBID, itemID, bid.getAmount());
            }

            this.bid = bid;
            this.bid.setLockID(lockID);
            this.notify();

            return BidInfo.ACCEPTANCE;
        } else {
            System.out.println("Bid is too low");
            return BidInfo.REJECTION;
        }
    }


    public int getItemID() {
        return itemID;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    @Override
    public String toString() {
        return itemInfo.toString();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        itemTimer(null);
    }

    // TODO Synch on setting and checking of bid
    private synchronized void itemTimer(Bid currentBid) {
        currentBid = bid;
        try {
            synchronized (this) { wait(AuctionHouse.ITEM_WAIT_TIME); }
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        if (bid == null) {
            System.out.println("Noone bid on " + this);
            return;
        }
        System.out.println("AUCTION CONTINUING");
        synchronized (bid) {
            if (bid == currentBid) {
                System.out.println("ENDING AUCTION WITH ITEM WINNING BLAH");
                open = false;
                endAuction();
            } else {
                currentBid = bid;
                itemTimer(currentBid);
            }
        }
    }

    private synchronized void endAuction() {

        synchronized (bid) {
            if (bid != null) {
                System.out.println("THE ITEM " + itemInfo + " WAS SOLD");
                bid.getAc().notifyBid(BidInfo.WINNER, itemID, bid.getAmount());
                bank.transferFunds(bid.getAccountNumber(), auctionID, bid.getLockID());
            }
        }
    }


}
