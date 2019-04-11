import java.io.IOException;
import java.net.Socket;

public class ClientProxy {

    ClientProxy(String hostName, int portNumber) throws IOException {

        Socket socket = new Socket(hostName, portNumber);
    }
}
