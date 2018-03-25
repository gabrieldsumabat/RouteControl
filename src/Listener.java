import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;


public class Listener implements Runnable{
    private LinkedBlockingQueue<RCU> queue;
    private Config LocalConfig;

    public Listener(LinkedBlockingQueue<RCU> q, Config localconfig){

        queue = q;
        LocalConfig = localconfig;
    }

    public void run() {
        try {
            System.out.println("Listener is Running  ");
            ServerSocket mainSocket = new ServerSocket(1450);
            while (true) {
                Socket sock = mainSocket.accept();
                ServerThread server = new ServerThread(sock, queue, LocalConfig);
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
