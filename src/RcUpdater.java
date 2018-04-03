import java.net.InetAddress;
import java.net.UnknownHostException;

public class RcUpdater implements Runnable {
    private Config localConfig;
    private int counter;

    public RcUpdater(Config localconfig){
        localConfig = localconfig;
    }

    @Override
    public void run() {
        //Sends a RCU to each known RC in the network
        Launcher Launchpad = new Launcher();
        while (true) {
                for (int i=0; i < localConfig.getNoa();i++ ) {
                    try {
                        if (localConfig.addressBook[i].getRCID() != -1) {
                            RCU packet = localConfig.addressBook[i].getRCU(localConfig.addressBook[i], 0);
                            InetAddress target = InetAddress.getByName(localConfig.addressBook[i].getIpa());
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

