package BankProxy;

import BankProxy.BankProxy;
import SourcesToOrganize.Packet;

import java.io.Serializable;

public class BankRequest extends Packet implements Serializable {

    private BankInfo type;

    private int ID;
    private int ID1;
    private double amount;
    private int lockNumber;
    private boolean isSuccess;

    public BankRequest(BankInfo type) {
        this.type = type;
    }

    // TODO Check usage
    public BankRequest() {

    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getLock() {
        return 0;
    }

    public BankInfo getType() {
        return type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getToID() {
        return ID1;
    }

    public void setToID(int ID1) {
        this.ID1 = ID1;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getLockNumber() {
        return lockNumber;
    }

    public void setLockNumber(int lockNumber) {
        this.lockNumber = lockNumber;
    }
}
