import java.net.InetAddress;
import java.net.UnknownHostException;


public class RttUpdater implements Runnable {

    public RttUpdater(){
    }

    @Override
    public void run() {
        //Sends an RTT_REQ to each Route Controller known in Address Book
        Launcher Launchpad = new Launcher();
        while (true) {
            for (int i = 0; i < RouteController.LocalConfig.getNoa(); i++ ) {
                try {
                    if (RouteController.LocalConfig.addressBook[i].getRCID() != -1) {
                        RCU packet = RouteController.LocalConfig.addressBook[i].getRCU(RouteController.LocalConfig.addressBook[i], 1);
                        InetAddress target = InetAddress.getByName(RouteController.LocalConfig.addressBook[i].getIpa());
                        Launchpad.sendRCU(target, 1450, packet);
                    }
                } catch (UnknownHostException uhe) {
                    uhe.printStackTrace();
                }
            }
            try {
                Thread.sleep(120000); //Sleeps for 2 minutes before attempting to send again.
            }catch (InterruptedException ue) {
                ue.printStackTrace();
            }
        }
    }

}

