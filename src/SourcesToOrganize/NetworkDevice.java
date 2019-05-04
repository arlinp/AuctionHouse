package SourcesToOrganize;

import java.io.Serializable;

/**
 * NetworkDevice is an object with information
 * on a connection
 * -IP Address
 * -Port number
 */
public class NetworkDevice implements Serializable {

    private String ipAddress;
    private int port;

    /**
     * Constructs a new connection
     *
     * @param ipAddress ip of connection
     * @param port port number
     */
    public NetworkDevice(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * @return ip address of the connection
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress ip address to be assigned
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port port number to be set
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ipAddress + ":" + port;
    }

    /**
     * @param obj Checks for object equality
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        } else {
            NetworkDevice networkDevice = (NetworkDevice) obj;
            if (ipAddress.equals(networkDevice.getIpAddress()) &&
                port == networkDevice.getPort()) {
                return true;
            }
            return false;
        }
    }
}
