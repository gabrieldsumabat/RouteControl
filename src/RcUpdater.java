import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Sends periodic RCU Updates to all known Route Controllers every 3 minutes.
 */
public class RcUpdater implements Runnable {

    /**
     * Creates a new RcUpdater
     */
    public RcUpdater(){
    }

    @Override
    public void run() {
        //Sends a RCU to each known RC in the network
        Launcher Launchpad = new Launcher();
        while (true) {
                for (int i = 0; i < RouteController.LocalConfig.getNoa(); i++ ) {
                    try {
                        if (RouteController.LocalConfig.addressBook[i].getRCID() != -1) {
                            RCU packet = RouteController.LocalConfig.addressBook[i].getRCU(RouteController.LocalConfig.addressBook[i], 0);
                            InetAddress target = InetAddress.getByName(RouteController.LocalConfig.addressBook[i].getIpa());
                            Launchpad.sendRCU(target, 1450, packet);
                        }
                    } catch (UnknownHostException uhe) {
                        uhe.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(180000); //Sleeps for 3 minutes before attempting to send again.
                }catch (InterruptedException ue) {
                    ue.printStackTrace();
                }
        }
    }

}

