import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server1 {
    private static final int PORT = 5001; 
    private static final List<Subscriber> subscribers = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("Starting Server1 on port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server1 is running on port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error starting Server1: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            ) {
                Object request = input.readObject();

                if (request instanceof Subscriber) {
                    Subscriber subscriber = (Subscriber) request;
                    handleSubscriber(subscriber, output);
                } else if (request instanceof AdminCommand) {
                    AdminCommand command = (AdminCommand) request;
                    handleAdminCommand(command, output);
                } else {
                    output.writeObject("Invalid request type");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error handling client in Server1: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket in Server1: " + e.getMessage());
                }
            }
        }

        private void handleSubscriber(Subscriber subscriber, ObjectOutputStream output) throws IOException {
            synchronized (subscribers) {
                switch (subscriber.getStatus()) {
                    case "SUBS":
                        subscribers.add(subscriber);
                        subscriber.setId(subscribers.size());
                        subscriber.setLastAccessed(System.currentTimeMillis());
                        output.writeObject("Subscriber added in Server1: " + subscriber);
                        break;
                    case "DEL":
                        subscribers.removeIf(s -> s.getId() == subscriber.getId());
                        output.writeObject("Subscriber removed in Server1: ID " + subscriber.getId());
                        break;
                    default:
                        output.writeObject("Unknown status in Server1: " + subscriber.getStatus());
                }
            }
        }

        private void handleAdminCommand(AdminCommand command, ObjectOutputStream output) throws IOException {
            if ("CAPACITY".equals(command.getCommand())) {
                synchronized (subscribers) {
                    int size = subscribers.size();
                    AdminResponse response = new AdminResponse(size, System.currentTimeMillis());
                    output.writeObject(response);
                }
            } else {
                output.writeObject("Unknown admin command in Server1: " + command.getCommand());
            }
        }
    }
}

class Subscriber implements Serializable {
    private int id;
    private String name;
    private String status; // SUBS, DEL, etc.
    private long lastAccessed;

    public Subscriber(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", lastAccessed=" + lastAccessed +
                '}';
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
