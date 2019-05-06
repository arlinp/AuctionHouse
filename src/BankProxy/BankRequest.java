package BankProxy;

import SourcesToOrganize.NetworkDevice;
import SourcesToOrganize.Packet;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Saves a bank request as an object with
 * information about the request
 */
public class BankRequest extends Packet implements Serializable {

    private BankInfo type;
    private int ID;
    private int ID1;
    private double amount;
    private int lockNumber;
    private LinkedBlockingQueue<NetworkDevice> networkDevices;

    /**
     * Constructs a request with a packetID
     * and list of network devices
     *
     * @param type type of request
     */
    public BankRequest(BankInfo type) {
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        networkDevices = new LinkedBlockingQueue<>();
        this.type = type;
    }

    /**
     * Constructs a request with a given packetID
     * and list of network devices
     *
     * @param type type of request
     */
    public BankRequest(BankInfo type, int ID) {
        setPacketID((int)(Math.random()*Integer.MAX_VALUE));
        this.type = type;
        setPacketID(ID);
    }

    /**
     * @return type of request
     */
    public BankInfo getType() {
        return type;
    }

    /**
     * @return request ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID set this request's ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return ID of who the message is going to
     */
    public int getToID() {
        return ID1;
    }

    /**
     * @param ID1 set ID of who the message is going to
     */
    public void setToID(int ID1) {
        this.ID1 = ID1;
    }

    /**
     * @return get the amount that is part of some account requests
     */
    public double getAmount() {
        return amount;
    }

    /**
     * set amount requested
     * @param amount amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return get the lock
     */
    public int getLockNumber() {
        return lockNumber;
    }

    /**
     * @param lockNumber set the lock ID
     */
    public void setLockNumber(int lockNumber) {
        this.lockNumber = lockNumber;
    }

    /**
     * @return list of network devices
     */
    public LinkedBlockingQueue<NetworkDevice> getNetworkDevices() {
        return networkDevices;
    }

    /**
     * @param networkDevices set the network devices
     */
    public void setNetworkDevices(LinkedBlockingQueue<NetworkDevice>
                                          networkDevices) {
        this.networkDevices = networkDevices;
    }

    /**
     * @return get all network devices
     */
    public NetworkDevice getNetworkDevice() {
        if (networkDevices.size() > 0) return networkDevices.peek();
        else return null;
    }


    /**
     * Add a new network device object to list
     *
     * @param networkDevice network device to add
     */
    public void addNetworkDevices(NetworkDevice networkDevice) {
        if (networkDevices == null) {
            networkDevices = new LinkedBlockingQueue<>();
        }
        networkDevices.add(networkDevice);
    }

}
