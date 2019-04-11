import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProxy {

    public static void main(String[] args) throws IOException {

        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portNumber);//  Listen  for  new  clients  forever
        while (true) {//  Create  new  thread  to  handle  each  clientSocket
            Socket clientProxy = serverSocket.accept();

            AgentClient kk = new AgentClient(clientProxy);
            Thread t = new Thread(kk);
            t.start();
        }
    }
}
