import java.util.concurrent.LinkedBlockingQueue;

public class RouteController {

    public static void main(String argv[]) {
        //Read the Input Configuration
        Config localConfig = new Config();
        //Print out local ASN values:
        System.out.println("My ASN Configuration:");
        System.out.println(localConfig.myASN);
        //Print out Address Book
        System.out.println("Address Book:");
        for (int j = 0; j < localConfig.noa; j++) {
            System.out.println(localConfig.addressBook[j]);
        }
        //Initialize the Shared Queue
        LinkedBlockingQueue<RCU> queue = new LinkedBlockingQueue<>();
        //Start the Listener Thread
        Listener RouteListen = new Listener(queue, localConfig);
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();
        //Start Consumer Thread
        Consumer PacketMonster = new Consumer(queue, localConfig);
        Thread ConsumerThread = new Thread(PacketMonster);
        ConsumerThread.start();
        //SPAWN RcUpdater
        //TODO Create RcUpdater
    }
}
