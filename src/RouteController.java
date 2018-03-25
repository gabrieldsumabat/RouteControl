import java.util.concurrent.LinkedBlockingQueue;

public class RouteController {

    public static void main(String argv[]) {
        Config localConfig = new Config();
        LinkedBlockingQueue<RCU> queue = new LinkedBlockingQueue<>();
        //Start the Listener Thread
        Listener RouteListen = new Listener(queue, localConfig);
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();

        for (int j=0; j<4;j++) {
            System.out.println(localConfig.addressBook[j]);
        }

        //main timer here:
            try{
                System.out.println("=====================\n\n"+ queue.take() + "\n\n=====================");
                }  catch (InterruptedException ie) {
                ie.printStackTrace();
                }

    }

}
