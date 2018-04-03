import java.net.InetAddress;
import java.net.UnknownHostException;

public class RcUpdater implements Runnable {
    volatile Config LocalConfig;

    public RcUpdater(Config localconfig){
        LocalConfig = localconfig;
    }

    @Override
    public void run() {
        //Sends a RCU to each known RC in the network
        Launcher Launchpad = new Launcher();
        while (true) {
                for (int i = 0; i < LocalConfig.getNoa(); i++ ) {
                    try {
                        if (LocalConfig.addressBook[i].getRCID() != -1) {
                            RCU packet = LocalConfig.addressBook[i].getRCU(LocalConfig.addressBook[i], 0);
                            packet.setAd(LocalConfig.addressBook);
                            InetAddress target = InetAddress.getByName(LocalConfig.addressBook[i].getIpa());
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

