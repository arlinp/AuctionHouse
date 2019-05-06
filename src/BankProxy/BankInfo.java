package BankProxy;

/**
 * This is an enumeration of possible
 * actions that are being taken.
 * Used by bank to handle all actions.
 */
public enum BankInfo {
    GETBALANCE,GETTOTALBALANCE,ADD,REMOVE,LOCK,UNLOCK,TRANSFER,TRANSFERFROMLOCK,NEWACCOUNT,OPENAUCTION,CLOSEAUCTION,GETAUCTIONS;
}
