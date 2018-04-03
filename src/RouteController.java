import java.util.concurrent.LinkedBlockingQueue;

public class RouteController {

    public static void main(String argv[]) {
        //Read the Input Configuration to determine network
        Config localConfig = new Config();
        //Print out local ASN values:
        System.out.println("My ASN Configuration:");
        System.out.println(localConfig.myASN);
        //Print out Address Book
        System.out.println("Address Book:");
        for (int j = 0; j < localConfig.getNoa(); j++) {
            System.out.println(localConfig.addressBook[j]);
        }
        //Initialize the Shared Queue for storing incoming RCU
        LinkedBlockingQueue<RCU> queue = new LinkedBlockingQueue<>();
        //Start the Listener Thread  to handle incoming Connections
        Listener RouteListen = new Listener(queue, localConfig);
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();
        //Start Consumer Thread to handle incoming RCU
        Consumer PacketMonster = new Consumer(queue, localConfig);
        Thread ConsumerThread = new Thread(PacketMonster);
        ConsumerThread.start();
        //Start RcUpdater to send periodic RCU every 3 minutes.
        RcUpdater RcuUpdater = new RcUpdater(localConfig);
        Thread RcuUpThread = new Thread(RcuUpdater);
        RcuUpThread.start();
        //Start RttUpdater to send periodic RTT Request every 2 minutes
        RttUpdater RttUpdate = new RttUpdater(localConfig);
        Thread RttThread = new Thread(RttUpdate);
        RttThread.start();
        //TODO: Route Advertisement and Updating new Routes
        //Ideas:
        // A) Add AddressBook to the RCU packet to send the address set with it?
        // B) Then compare the added cost and if the cost is lower, update next path?
        // C) Would have to change ASN to account for the next hop and have packets forward it?
        //Change IPA to next hop?
        //Create a second externalBook indicating addresses which require a next hop?
    }
}
