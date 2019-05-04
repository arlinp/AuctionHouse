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

    /**
     * Item to be created from qualifying information
     *
     * @param bank Bank reference
     * @param auction
     * @param itemInfo
     * @param auctionID
     */
    public Item(BankProxy bank, AuctionHouse auction, ItemInfo itemInfo,
                int auctionID) {
        this.bank = bank;
        this.auction = auction;
        this.itemInfo = itemInfo;
        this.itemID = itemInfo.getItemID();
        this.auctionID = auctionID;
    }

    /**
     * Starts the thread of the item
     */
    public synchronized void startThread(){
        new Thread(this).start();
    }

    /**
     * Checks and then sets the bid.
     *
     * Synchronized because it can be called twice at the same time!
     *
     * @param bid Bid to be checked
     * @return Status of the bid
     */
    public synchronized BidInfo setBid(Bid bid) {
        if (!open) return BidInfo.REJECTION;
        else if (bid.getAmount() > itemInfo.getPrice()) {
            int lockID = bank.lockFunds(bid.getAccount(), bid.getAmount());

            if (lockID == -1) return BidInfo.REJECTION;
            else if (this.bid != null) {
                bank.unlockFunds(this.bid.getAccount(), this.bid.getLockID());
            }

            synchronized (itemInfo) { itemInfo.setPrice(bid.getAmount()); }

            // Check nullity
            if (this.bid != null) {
                AuctionCommunicator ac = this.bid.getAc();
                ac.notifyBid(BidInfo.OUTBID, itemID, bid.getAmount());
            }

            // Set values of the bid
            this.bid = bid;
            this.bid.setLockID(lockID);
            this.notify();
            return BidInfo.ACCEPTANCE;
        } else {
            // Reject if there is not enough money
            return BidInfo.REJECTION;
        }
    }

    /**
     * Gets the ItemID of the item
     *
     * @return Item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Gets the tied Item info of the item
     *
     * @return ItemInfo of item
     */
    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    /**
     * Override toString to provide better troubleshooting
     * @return string representation
     */
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
                synchronized (this) { wait(AuctionHouse.waitTime); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Checks if noone bid
            if (bid == null) {
                System.out.println("NOONE BID ON " + this);
                auction.removeItem(itemID);
                return;
            }

        } while (currentBid != bid);

        // End auction
        synchronized (bid) {
            if (bid == currentBid) {
                open = false;
                endAuction();
                return;
            }
        }
    }

    /**
     * Ends an individual item's auction if
     * it was sold.
     * Transfers the fund from the bank to the winning
     * account.
     * Removes item from Auction House.
     */
    private synchronized void endAuction() {
        synchronized (bid) {
            if (bid != null) {
                System.out.println("THE ITEM " + itemInfo + " WAS SOLD");
                bid.getAc().notifyBid(BidInfo.WINNER, itemID, bid.getAmount());
                bank.transferFunds(bid.getAccount(), auctionID, bid.getLockID());
                auction.removeItem(itemID);
                //auction.bids.remove(bid);
            }
        }
    }


    /**
     * Checks a bid's reference to an
     * Agent
     *
     * @param accountID The ID of the bidder
     * @return is it this account's bid?
     */
    public boolean contains(int accountID) {
        if (open && bid != null && bid.getAccount() == accountID) return true;
        else return false;
    }
}
