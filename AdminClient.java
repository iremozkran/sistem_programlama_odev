import java.io.*;
import java.net.*;
import java.util.*;

public class AdminClient {
    private static final List<Integer> SERVER_PORTS = Arrays.asList(5001, 5002, 5003);

    public static void main(String[] args) {
        for (int port : SERVER_PORTS) {
            try (Socket socket = new Socket("localhost", port);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                // Send capacity command
                AdminCommand command = new AdminCommand("CAPACITY");
                output.writeObject(command);

                // Receive and print response
                Object response = input.readObject();
                if (response instanceof AdminResponse) {
                    System.out.println("Server on port " + port + " capacity: " + response);
                } else {
                    System.out.println("Invalid response from server on port " + port + ": " + response);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error communicating with server on port " + port + ": " + e.getMessage());
            }
        }
    }
}

class AdminCommand implements Serializable {
    private String command;

    public AdminCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}

class AdminResponse implements Serializable {
    private int subscriberCount;
    private long timestamp;

    public AdminResponse(int subscriberCount, long timestamp) {
        this.subscriberCount = subscriberCount;
        this.timestamp = timestamp;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AdminResponse{" +
                "subscriberCount=" + subscriberCount +
                ", timestamp=" + timestamp +
                '}';
    }
}
