package SourcesToOrganize;

import java.io.Serializable;

public class Packet implements Serializable {

    private Boolean status = false;
    private Boolean ack = true;
    private int packetID = 0;
    // Troubleshooting stuff goes here

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }


    public Boolean getAck() {
        return ack;
    }

    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    public int getPacketID() {
        return packetID;
    }

    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }
}
