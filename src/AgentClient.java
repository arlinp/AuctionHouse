import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AgentClient implements Runnable{

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    //    private State state = State.WAITING;
    private int currentJoke = 0;

    public AgentClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    public void run() {
        String inputLine = null;
        String outputLine;
        do {

            try {
                inputLine = in.readLine();
            } catch (IOException ex) {
                inputLine = null;
            }
        } while (inputLine != null);
    }
}
