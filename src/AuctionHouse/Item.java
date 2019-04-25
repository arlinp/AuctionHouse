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
        if (System.currentTimeMillis() > itemInfo.getTime()) {
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


            this.bid = bid;
            this.bid.setLockID(lockID);

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
        // Calculate time plus offset due to imprecision of wait
        Long timeLeft = itemInfo.getTime() - System.currentTimeMillis() + 100;


        try {
            synchronized (this) {
                wait(timeLeft);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (System.currentTimeMillis() > itemInfo.getTime()) {
            System.out.println("Auction Ended!");
            endAuction();
        } else {
            System.out.println("Running again!");
            run();
        }

//        while(noNewBid) {
//            wait 30 seconds
//                wake on new bid
//
//
//        }


    }

    private void endAuction() {

        if (bid != null) {
            bank.transferFunds(bid.getAccountNumber(), auctionID, bid.getLockID());

        }

    }


}
