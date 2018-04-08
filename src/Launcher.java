import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


public class Launcher {
    //Socket Connection with Target IPA and sends the RCU
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;

    /**
     * Template Class for sending RCU to target Server Socket
     * @see #sendRCU(InetAddress, int, RCU)
     */
    public Launcher() {

    }
    /**
     * Send RCU to target Server
     *
     * @param hostname InetAddress of target server socket
     * @param portNum Port of target socket
     * @param updatePacket RCU object to be transferred
     */
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
}
