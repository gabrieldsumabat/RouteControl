import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Launcher {

    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;


    public Launcher() {

    }

    public void sendRCU(InetAddress hostname, int portNum, RCU updatePacket) {

        while (!isConnected) {
            try {
                socket = new Socket(hostname, portNum);
                System.out.println("Connected");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Object to be written = \n\n" + updatePacket);
                outputStream.writeObject(updatePacket);
            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     public static void main(String[] args) {
        RCU RCUsent = new RCU(1, 2, 3, 4, 5, 6, 7, 1);
        Launcher client = new Launcher();
        try {
            InetAddress target = InetAddress.getByName("127.0.0.1");
            client.sendRCU(target,1450, RCUsent);
        }
        catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
    }
}

