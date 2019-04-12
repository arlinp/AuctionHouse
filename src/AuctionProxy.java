import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class AuctionProxy implements AuctionProcess {

    private ObjectInputStream is;
    private ObjectOutputStream os;

    AuctionProxy(String hostname, int port) {
        Socket s = null;
        try {
            s = new Socket(hostname, port);

            is = new ObjectInputStream(s.getInputStream());
            os = new ObjectOutputStream(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!s.isClosed()) {

            processMessage();

        }
    }

    private void processMessage() {

    }


    /**
     * To place a bid
     *
     * @param bid    Bid object that contains elements
     * @param itemID Identifier of Item
     */
    @Override
    public void bid(Bid bid, int itemID) {

    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        return null;
    }

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public ArrayList<ItemInfo> getItems() {
        return null;
    }
}
