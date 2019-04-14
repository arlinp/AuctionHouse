package BankProxy;

import BankProxy.BankProxy;
import SourcesToOrganize.Packet;

import java.io.Serializable;

public class BankRequest extends Packet implements Serializable {
    public int getLock() {
        return 0;
    }

}
