package BankProxy;

import BankProxy.BankProxy;
import SourcesToOrganize.NetworkDevice;
import SourcesToOrganize.Packet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class BankRequest extends Packet implements Serializable {

    private BankInfo type;

    private int ID;
    private int ID1;
    private double amount;
    private int lockNumber;
    private boolean isSuccess;
    private LinkedBlockingQueue<NetworkDevice> networkDevices;

    public BankRequest(BankInfo type) {
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        networkDevices = new LinkedBlockingQueue<>();
        this.type = type;
    }

    // TODO Check usage
    public BankRequest() {
            setStatus(true);
    }

    public BankRequest(BankInfo type, int ID) {
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        this.type = type;
        setPacketID(ID);
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

    public LinkedBlockingQueue<NetworkDevice> getNetworkDevices() {
        return networkDevices;
    }

    public void setNetworkDevices(LinkedBlockingQueue<NetworkDevice> networkDevices) {
        this.networkDevices = networkDevices;
    }

    public NetworkDevice getNetworkDevice() {
        if (networkDevices.size() > 0) return networkDevices.peek();
        else return null;
    }

    public void addNetworkDevices(String ipAddress, int port) {
        NetworkDevice networkDevice = new NetworkDevice(ipAddress, port);
        networkDevices.add(networkDevice);
    }

    public void addNetworkDevices(NetworkDevice networkDevice) {
        networkDevices.add(networkDevice);
    }



}
