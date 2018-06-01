package Game;

import java.io.*;
import java.net.*;

public class Conn extends Thread {

    private int id;
    private final DataInputStream is;
    private final DataOutputStream os;
    public final Socket socket;
  

    public Conn(Socket socket, int id) throws IOException {
        this.id = id;
        this.socket = socket;
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (checkConnection()) {
            String data = recieve();
            if (data != null) {
                Server2.recievedData = data;
            }
            Server2.send(data, id);
  
        }
    }

    private String recieve() {
        String j = "";
        try {
            j = is.readUTF();
        } catch (IOException ex) {
        }
        return j;
    }

    public synchronized void send(String j, int fromId) {
        j = j.concat(((Integer) fromId).toString());
        try {
            os.writeUTF(j);
            os.flush();
        } catch (IOException e) {
            System.out.println(e + " This is unfortunate");
        }

    }

    public void send(String j) {
        try {
            os.writeUTF(j);
            os.flush();
        } catch (IOException | NullPointerException e) {
        }
    }

    private boolean isValid(String data) {
        //checks if x = VelX + the previous x value and same for y
        //return checkConnection() && (((Byte)data[0]).doubleValue() == ((Byte)lastData[0]).doubleValue() + ((Byte)data[2]).doubleValue()) && (((Byte)data[1]).doubleValue() == ((Byte)lastData[1]).doubleValue() + ((Byte)data[3]).doubleValue());
        return true;
    }

    public synchronized void disconnect() {
        Server2.removeFromLists(this);

        try {
            is.close();
            os.close();
            socket.close();
        } catch (IOException ex) {
        }
    }

    private boolean checkConnection() {
        return !socket.isClosed();
    }

    public int getConnectionId() {
        return id;
    }

    public void setConnectionId(int id) {
        this.id = id;
    }
}