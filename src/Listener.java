import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Listener implements Runnable{


    public void Listener(){

    }

    public void run() {
        try {
            System.out.println("Listener is Running  ");
            ServerSocket mainSocket = new ServerSocket(4445);
            while (true) {
                Socket sock = mainSocket.accept();
                ServerThread server = new ServerThread(sock);

                Thread serverThread = new Thread(server);
                serverThread.start();
            }
        }catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
