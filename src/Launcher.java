import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Launcher {
    //Socket Connection with Target IPA and sends the RCU
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
         try {
            RCU RCUsent = new RCU(2, 100, 1,7, 1, InetAddress.getByName("127.0.0.1"));
            //RCUsent.setAd();
            Launcher client = new Launcher();
            client.sendRCU(RCUsent.getTargetIP(),1450, RCUsent);
            }
            catch (UnknownHostException uhe) {
                uhe.printStackTrace();
        }
    }
}

