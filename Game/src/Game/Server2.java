package Game;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Server2 {

    private static float volume;
    private static float soundVolume;
    public static int id;
    private static DataInputStream is;
    private static DataOutputStream os;
    private static GameContainer gc;
    private static Socket socket;
    public static String recievedData = "";
    private static int port;
    private static ServerSocket serverSocket;
    private static List<Conn> connectionList;
    private static List<Integer> occupiedIds;
    private static ExecutorService pool;
    public static boolean inGame;
    public static String character = "none";
    public static int numAlive;
    public static boolean Alive;
    public static int numLives =1;

    public static void main(String[] args, GameContainer gc2) {
        try {
            port = 10000;                                                       //port the game runs on
            serverSocket = new ServerSocket(port);
            connectionList = new ArrayList<Conn>();
            occupiedIds = new ArrayList<Integer>();

            pool = Executors.newFixedThreadPool(2);                             //creates execution thread
            socket = new Socket("localhost", 10000);                            //creates socket to connect to self
            Thread trying = new Thread(new Runnable() {

                Socket s;
                Conn c;
                Integer tempId;

                @Override
                public void run() {
                    try {
                        s = serverSocket.accept();
                        tempId = getAvailableId();
                        c = new Conn(s, tempId);
                    } catch (IOException e) {
                    }
                    connectionList.add(c);
                    occupiedIds.add(tempId);
                    c.start();

                    System.out.println("Player " + tempId + " Joined.");
                    id = tempId;
                }
            });
            trying.start();                                                     //runs thread to connect to self IMPORTANT: must use thread otherwise code breaks
            System.out.println(socket.toString());
            pool.execute(new serve());
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            gc = gc2;
            pool.execute(new t2());                                             //running client as well for player 1

            System.out.println("Server Started. " + "IP: " + InetAddress.getLocalHost().getHostAddress() + " Port: " + port + "\nType Help for the commands");
        } catch (IOException e) {
        }
    }

    public static synchronized void send(String message, int fromId) {
        for (Conn i : connectionList) {
            //if (i.getConnectionId() != fromId) 
                i.send(message, fromId);                                        //sends to all using conn's send method
            
        }
    }

    public static synchronized void sendTo(String message, int toId) {
        for (Conn i : connectionList) {
            if (i.getConnectionId() == toId) {
                i.send(message);                                                //sends to only one client
            }
        }
    }

    public static synchronized void removeFromLists(Conn c) {
        if (c == null || !occupiedIds.contains(c.getConnectionId())) {
            return;
        }
        connectionList.remove(c);
        occupiedIds.remove((Integer) c.getConnectionId());
        send("Player " + c.getConnectionId() + " Disconnected.", -1);
        System.out.println("Player " + c.getConnectionId() + " Disconnected.");
    }

    public static synchronized void stopServer() throws IOException {
        System.out.println("Stopping Server...");
        if (connectionList.size() > 0) {
            send("Server Stopping", -1);
            while (connectionList.size() > 0) {
                connectionList.get(0).disconnect();
            }
        }
        System.out.println("Server Stopped.");
        //     System.exit(0);
    }

    public static synchronized void reboot() throws IOException {                //reboots server
        System.out.println("Rebooting...");

        if (connectionList.size() > 0) {
            send("Server Rebooting", 0);
            while (connectionList.size() > 0) {
                connectionList.get(0).disconnect();                             //disconnects all players
            }
        }
        discon();
        connectionList = new ArrayList<Conn>();
        occupiedIds = new ArrayList<Integer>();

        pool.shutdown();                                                     //stops all running threads
        System.out.println("Server Restarted. " + "IP: " + Server2.getIP() + " Port: " + port);
    }

    public static String getIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();                     //returns your IP
    }

    private static Integer getAvailableId() {
        Integer available = 1;
        while (occupiedIds.contains(available)) {
            available++;
        }
        return available;                                                       //gets an ID that was not previously assigned for a new player
    }

    public static int getNumPlayers() {
        try {
            return connectionList.size();
        } catch (NullPointerException n) {
            return -1;
        }
    }

    static class serve implements Runnable {

        @Override
        public void run() {
            while (true) {                                                      //keep server running forever
                try {
                    Socket s = serverSocket.accept();                           //accepts connection to server
                    if (s != null) {
                        Integer tempId = getAvailableId();
                        Conn c = new Conn(s, tempId);

                        connectionList.add(c);
                        occupiedIds.add(tempId);
                        c.start();

                        sendTo("" + tempId + "", tempId);                            //sends player ID to player who connected
                        send("Player " + tempId + " Joined.", tempId);
                        send("Number of Players:" + tempId, tempId);
                        System.out.println("Player " + tempId + " Joined.");
                    }
                } catch (IOException ex) {
                }
            }
        }
    }

    static class t2 implements Runnable {

        @Override
        public synchronized void run() {
            try {
                while (true) {
                    update();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public static int getID() {
        return id;
    }

    public synchronized static void update() throws IOException {
        if (is.available() > 0) {
            recievedData = is.readUTF();
            System.out.println("Server data: " + recievedData);
        }
    }

    public static void discon() throws IOException {
        is.close();
        os.close();
        socket.close();
    }

    public static void setVolume(float f) {
        volume = f;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setSoundVolume(float f) {
        soundVolume = f;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }
}