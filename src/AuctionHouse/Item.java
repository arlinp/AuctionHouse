package AuctionHouse;

import BankProxy.BankProxy;
import SourcesToOrganize.Bid;

public class Item {

    private BankProxy bank;
    private Bid bid;



    private ItemInfo itemInfo;
    private int itemID;

    public Item(BankProxy bank, ItemInfo itemInfo) {
        this.bank = bank;
        this.itemInfo = itemInfo;
        this.itemID = itemInfo.getItemID();

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
    public synchronized boolean setBid(Bid bid) {
        if (System.currentTimeMillis() > itemInfo.getTime()) {
            System.out.println("Auction is over!");
            return false;
        }

        if (bid.getAmount() > this.bid.getAmount()) {
            int lockID = bank.lockFunds(bid.getAccountNumber(), bid.getAmount());

            if (lockID == -1) {
                System.out.println("Not enough funds");
                return false;
            }

            bank.unlockFunds(this.bid.getAccountNumber(),this.bid.getLockID());

            synchronized (itemInfo) { itemInfo.setPrice(bid.getAmount()); }

            this.bid = bid;

            return true;
        } else {
            System.out.println("Bid is too low");
            return false;
        }
    }


    public int getItemID() {
        return itemID;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }
}
