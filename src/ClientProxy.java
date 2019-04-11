import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProxy {

    public static void main(String[] args) throws IOException {

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);


        try (Socket socket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String fromServer = in.readLine();
            while (fromServer != null) {

                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")) {
                    break;
                }

                String fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
                fromServer = in.readLine();
            }
        }
    }
}
