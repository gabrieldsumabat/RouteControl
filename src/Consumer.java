import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class Consumer implements Runnable{
    private LinkedBlockingQueue<RCU> queue;
    private Config localConfig;
    private RCU packet;

    public Consumer(LinkedBlockingQueue<RCU> q, Config localconfig)    {
        queue=q;
        localConfig=localconfig;
    }

    @Override
    public void run() {
        while(true) {
            try {
                //TODO: Need to determine what to do with the incoming RCU packets
                packet = queue.take();
                System.out.println("=====================\n" + packet + "\n\n=====================");

            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }


        }
    }
}
