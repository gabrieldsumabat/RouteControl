import java.net.InetAddress;
import java.net.UnknownHostException;

public class RttUpdater implements Runnable {
    private Config localConfig;
    private int counter;

    public RttUpdater(Config localconfig){
        localConfig = localconfig;
    }

    @Override
    public void run() {
        Launcher Launchpad = new Launcher();
        while (true) {
            for (int i=0; i < localConfig.noa;i++ ) {
                try {
                    if (localConfig.addressBook[i].getRCID() != -1) {
                        RCU packet = localConfig.addressBook[i].getRCU(localConfig.addressBook[i], 1);
                        InetAddress target = InetAddress.getByName(localConfig.addressBook[i].getIpa());
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

