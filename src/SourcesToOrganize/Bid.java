package SourcesToOrganize;

import java.io.Serializable;

public class Bid implements Serializable {

    double amount;
    int accountNumber;
    int itemID;

    Bid(double amount, int accountNumber, int itemID) {
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

}
