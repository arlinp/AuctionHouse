package SourcesToOrganize;

import java.io.Serializable;

/**
 *
 */
public class Packet implements Serializable {

    private Boolean status = false;
    private Boolean ack = true;
    private int packetID = 0;
    // Troubleshooting stuff goes here

    /**
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return
     */
    public Boolean getStatus() {
        return status;
    }


    /**
     * @return
     */
    public Boolean getAck() {
        return ack;
    }

    /**
     * @param ack
     */
    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    /**
     * @return
     */
    public int getPacketID() {
        return packetID;
    }

    /**
     * @param packetID
     */
    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }
}
