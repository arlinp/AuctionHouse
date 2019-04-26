package SourcesToOrganize;

import java.io.Serializable;

public class NetworkDevice implements Serializable {

    private String ipAddress;
    private int port;

    public NetworkDevice(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ipAddress + ":" + port;
    }

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
