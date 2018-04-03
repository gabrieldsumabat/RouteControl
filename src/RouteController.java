import java.util.concurrent.LinkedBlockingQueue;

public class RouteController {

    public static void main(String argv[]) {
        //Read the Input Configuration to determine network
        Config LocalConfig = new Config();
        //Initialize the Shared Queue for storing incoming RCU
        LinkedBlockingQueue<RCU> queue = new LinkedBlockingQueue<>();
        //Start the Listener Thread  to handle incoming Connections
        Listener RouteListen = new Listener(queue, LocalConfig);
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();
        //Start Consumer Thread to handle incoming RCU
        Consumer PacketMonster = new Consumer(queue, LocalConfig);
        Thread ConsumerThread = new Thread(PacketMonster);
        ConsumerThread.start();
        //Start RcUpdater to send periodic RCU every 3 minutes.
        RcUpdater RcuUpdater = new RcUpdater(LocalConfig);
        Thread RcuUpThread = new Thread(RcuUpdater);
        RcuUpThread.start();
        //Start RttUpdater to send periodic RTT Request every 2 minutes
        RttUpdater RttUpdate = new RttUpdater(LocalConfig);
        Thread RttThread = new Thread(RttUpdate);
        RttThread.start();
        //Start User Commands Thread
        Command userCommand = new Command(LocalConfig);
        Thread userInput = new Thread(userCommand);
        userInput.start();
        //Periodically Print Local Config and Address Book
        while(true){
            System.out.println("Local ASN Values: ");
            System.out.println(LocalConfig.myASN);
            System.out.println("Address Book: ");
            for (int j=0; j<LocalConfig.getNoa();j++) {
                System.out.println(LocalConfig.addressBook[j]);
            }
            try {
                Thread.sleep(120000); //Waits 2 minutes.
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

