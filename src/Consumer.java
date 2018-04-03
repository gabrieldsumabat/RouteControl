import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

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
                packet = queue.take();
                System.out.println("Packet Being Consumed: \n" + packet + "\n\n");
                //Packet Handling:
                //If RTT_RESP for our RTT_REQ
                if (packet.getRttFlag()==2 && packet.getLinkID()==localConfig.myASN.getASNID()){
                    //Update ASN Cost 0.8*last_cost+0.2*current_cost, where cost is RTT
                    for (int i=0;i<localConfig.getNoa();i++){
                        if (localConfig.addressBook[i].getRCID()==packet.getRCID()){
                            localConfig.addressBook[i].setLinkCost((packet.getRoundTripTime()*2+localConfig.addressBook[i].getLinkCost()*8)/10);
                        }
                    }
                }
                    //If Target ID is Local ID -> Update AddressBook and Cost
                    else if (packet.getLinkID()==localConfig.myASN.getASNID()) {
                        // Update ASN values
                        // TODO: FIGURE OUT HOW TO UPDATE LINK BW AND USAGE
                        // Update Associated costs
                    }
                else {
                    //If packet's Target ID is not Local ID -> Forward packet to the ASNID
                    if (packet.getLinkID()!=localConfig.myASN.getASNID()){
                        for (int j=0;j<localConfig.getNoa();j++){
                            if (localConfig.addressBook[j].getASNID()==packet.getLinkID()){
                                packet.setTargetIP(InetAddress.getByName(localConfig.addressBook[j].getIpa()));
                            }
                        }
                        //Forward the packet to the next address
                        Launcher client = new Launcher();
                        client.sendRCU(packet.getTargetIP(),1450, packet);
                    }
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }  catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            }


        }
    }
}
