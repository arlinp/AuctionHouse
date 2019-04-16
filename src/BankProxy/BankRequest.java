package BankProxy;

import BankProxy.BankProxy;
import SourcesToOrganize.Packet;

import java.io.Serializable;

public class BankRequest extends Packet implements Serializable {

    private BankInfo type;

    public BankRequest(BankInfo type) {
        this.type = type;
    }

    // TODO Check usage
    public BankRequest() {

    }

    public int getLock() {
        return 0;
    }

    public BankInfo getType() {
        return type;
    }
}
