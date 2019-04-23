package SourcesToOrganize;

public class Packet {

    private Boolean status = false;
    private Boolean ack = true;
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
}
