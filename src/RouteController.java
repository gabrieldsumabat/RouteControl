import java.util.concurrent.LinkedBlockingQueue;

public class RouteController {

    public static void main(String argv[]) {
        LinkedBlockingQueue<RCU> queue = new LinkedBlockingQueue<>();
        //Start the Listener Thread
        Listener RouteListen = new Listener(queue);
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();
        //main timer here:
            try{
                System.out.println("=====================\n\n"+ queue.take() + "\n\n=====================");
                }  catch (InterruptedException ie) {
                ie.printStackTrace();
                }



    }

}
