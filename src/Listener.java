import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Listener binds port 1450 and spawns a new Server Thread for each incoming Socket connection.
 */
public class Listener implements Runnable{
    //Listens to incoming connections and spawns a ServerThread to handle the new connection
    private LinkedBlockingQueue<RCU> queue;

    /**
     * Creates new Listener connected to LinkedBlockingQueue
     * @param q Input LinkedBlockingQueue for Server Thread to produce to
     */
    public Listener(LinkedBlockingQueue<RCU> q){
        queue = q;
    }

    @Override
    public void run() {
        try {
            System.out.println("Listener is Running  ");
            ServerSocket mainSocket = new ServerSocket(1450);
            while (true) {
                Socket sock = mainSocket.accept();
                ServerThread server = new ServerThread(sock, queue);
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
