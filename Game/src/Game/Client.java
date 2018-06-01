package Game;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

/**
 *
 * @author Rahil
 */
public class Client extends Thread {

    private static float volume;
    private static float soundVolume;
    public static int id = 0;
    private static DataInputStream is;
    private static DataOutputStream os;
    private static GameContainer gc;
    private static Socket socket;
    private static int numPlayers;
    public static String recievedData;
    public static boolean isCLOSED;
    private static ExecutorService pool;
    public static boolean inGame;
    public static String character = "none";
    public static boolean Alive;
    public static int numLives;

    public static void main(String s, GameContainer gc) throws IOException {
        Client.gc = gc;
        socket = new Socket(s, 10000);
        System.out.println(socket.toString());
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        pool = Executors.newFixedThreadPool(2);
        inGame = false;
        update();
        if (recievedData == "") {
            id = 2;
        } else {
            id = Integer.parseInt(recievedData);
        }
        System.out.println("id == " + id);
        numPlayers = id;
        pool.execute(new t2());
        pool.execute(new t3());
    }

    public static int getID() {
        return id;
    }

    public static synchronized void update() throws IOException {
        if (is.available() > 1) {
            recievedData = is.readUTF();
            if (recievedData != null) {
                System.out.println(recievedData);
            }
        }
    }

    public static int getNumPlayers() {
        return numPlayers;
    }

    public static void discon() throws IOException {
        is.close();
        os.close();
        socket.close();
        isCLOSED = true;
    }

    public synchronized static void send(String x, int y) throws IOException {
        os.writeUTF(x + y);
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

    static class t3 implements Runnable {

        @Override
        public void run() {
            if (recievedData != null) {
                System.out.println("Client data: " + recievedData);
                if (recievedData.contains("Number of Players:")) {
                    System.out.println("BIG L " + recievedData);
                    numPlayers = Integer.parseInt(recievedData.substring(recievedData.length() - 1));
                }
            }
        }
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
