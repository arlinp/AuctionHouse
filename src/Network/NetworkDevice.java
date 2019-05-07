package Network;

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
     * Gets the IP address of the device
     *
     * @return ip address of the connection
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets the port of the device
     *
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Make string representation
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return ipAddress + ":" + port;
    }

    /**
     * Checks the equality of the object
     *
     * @param obj Checks for object equality
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        // Check the equality of the objects
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
