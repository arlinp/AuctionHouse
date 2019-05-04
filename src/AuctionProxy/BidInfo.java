package AuctionProxy;

/**
 * Enumeration of bid statuses
 * Used by AuctionHouse and AuctionProxy
 * to manage information about bids
 */
public enum BidInfo {
    ACCEPTANCE, REJECTION, OUTBID, WINNER;
}
