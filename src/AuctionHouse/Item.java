package AuctionHouse;

import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;

public class Item implements Runnable {

    private BankProxy bank;
    private AuctionHouse auction;
    private Bid bid;
    private int auctionID;
    private ItemInfo itemInfo;
    private int itemID;
    private boolean open = true;

    public Item(BankProxy bank, AuctionHouse auction, ItemInfo itemInfo,
                int auctionID) {
        this.bank = bank;
        this.auction = auction;
        this.itemInfo = itemInfo;
        this.itemID = itemInfo.getItemID();
        this.auctionID = auctionID;

    }

    public synchronized void startThread(){
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
            int lockID = bank.lockFunds(bid.getAccountNumber(), bid.getAmount());

            if (lockID == -1) return BidInfo.REJECTION;
            else if (this.bid != null) {
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
        itemTimer();
    }

    /**
     * Timer that resets upon each bid. Works off a notify/wait structure.
     *
     * When it has be approximately the specified time and the bid has not
     * changed then the auction will conclude.
     */
    private synchronized void itemTimer() {
        Bid currentBid;

        // Wait specified time for a bid to occur.
        do {
            currentBid = bid;
            try {
                synchronized (this) { wait(AuctionHouse.ITEM_WAIT_TIME); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Checks if noone bid
            if (bid == null) {
                // Noone bid on item Blah!
                System.out.println("Noone bid on " + this);
                // TODO Keep track on items not being bid on
                //auction.addItem();
                auction.removeItem(itemID);
                return;
            }

        } while (currentBid != bid);

        // End auction
        synchronized (bid) {
            if (bid == currentBid) {
                System.out.println("ENDING AUCTION WITH ITEM WINNING BLAH");
                open = false;
                endAuction();
                return;
            }
        }
    }

    private synchronized void endAuction() {
        synchronized (bid) {
            if (bid != null) {
                System.out.println("THE ITEM " + itemInfo + " WAS SOLD");
                bid.getAc().notifyBid(BidInfo.WINNER, itemID, bid.getAmount());
                bank.transferFunds(bid.getAccountNumber(), auctionID, bid.getLockID());
                auction.removeItem(itemID);
                //auction.bids.remove(bid);
            }
        }
    }


}
