package Network;

import java.io.Serializable;

/**
 * Packet that is extended and shared by AuctionRequest and BankRequest
 */
public class Packet implements Serializable {

    private Boolean status = false;
    private Boolean ack = true;
    private int packetID = 0;
    // Troubleshooting stuff goes here

    /**
     * Set status of the Packet
     *
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Gets the status of the packet
     *
     * @return Depends on type response
     */
    public Boolean getStatus() {
        return status;
    }


    /**
     * Get whether or not this is currently a response
     *
     * @return true if response, false if notification
     */
    public Boolean getAck() {
        return ack;
    }

    /**
     * Set the Acknowledgement receipt
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
