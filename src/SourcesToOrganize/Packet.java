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
     * Gets the status
     *
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }


    /**
     * Get the ack
     *
     * @return ack
     */
    public Boolean getAck() {
        return ack;
    }

    /**
     * Set the ack
     *
     * @param ack Acknowledgement required
     */
    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    /**
     * Get the packet ID
     *
     * @return Packet ID
     */
    public int getPacketID() {
        return packetID;
    }

    /**
     * Set the packet ID
     *
     * @param packetID PacketID
     */
    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }
}
