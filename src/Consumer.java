import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable{
    private LinkedBlockingQueue<RCU> queue;
    private Config localConfig;
    private RCU packet;

    public Consumer(LinkedBlockingQueue<RCU> q, Config localconfig) {
        queue=q;
        localConfig=localconfig;
    }

    @Override
    public void run() {
        while(true) {
            try {
                packet = queue.take();
                System.out.println("Packet Being Consumed: \n" + packet + "\n\n");
                //If RTT_RESP for our RTT_REQ
                if (packet.getRttFlag()==2 && packet.getLinkID()==localConfig.myASN.getASNID()){
                    //Update ASN Cost 0.8*last_cost+0.2*current_cost, where cost is RTT
                    for (int i=0;i<localConfig.getNoa();i++){
                        if (localConfig.addressBook[i].getRCID()==packet.getRCID()){
                            localConfig.addressBook[i].setLinkCost((packet.getRoundTripTime()*2+localConfig.addressBook[i].getLinkCost()*8)/10);
                        }
                    }
                }
                    //If Target ID is Local ID -> Compare Advertised AddressBook costs with local AddressBook
                    else if (packet.getLinkID()==localConfig.myASN.getASNID()) {
                            ASN[] AdSet = packet.getAd();
                            //For Each Address in the Advertisement, Compare Costs and Update Paths
                            for(ASN Ad:AdSet){
                                //Check if ASN is known
                                if (localConfig.ASNinBook(Ad.getASNID())) {
                                    //Update AddressBook Costs based on Advertisement
                                    long netCost = Ad.getLinkCost() + packet.getLinkCost();
                                    for (ASN target : localConfig.addressBook) {
                                        if (Ad.getASNID() == target.getASNID() && target.getLinkCost() > netCost) {
                                            target.setLinkCost(netCost);
                                            target.addHop(packet.getLinkID());
                                            target.setIpa(localConfig.getIPAfromASN(localConfig.getASNfromRC(packet.getRCID())));
                                        }
                                    }
                                } //Else Add ASN to network
                                else {
                                    //ADD ASN TO AddressBook
                                }
                            }
                    }
                else {
                    //If packet's Target ID is not Local ID -> Forward packet to the ASNID
                    if (packet.getLinkID()!=localConfig.myASN.getASNID()){
                        for (int j=0;j<localConfig.getNoa();j++){
                            if (localConfig.addressBook[j].getASNID()==packet.getLinkID()){
                                //If the Target ASN is a different RC -> Forward Packet to RC
                                if (localConfig.addressBook[j].getIpa()!=null){
                                    packet.setTargetIP(InetAddress.getByName(localConfig.addressBook[j].getIpa()));
                                    Launcher client = new Launcher();
                                    client.sendRCU(packet.getTargetIP(),1450, packet);
                                }
                                //If the ASN has no RC -> Print Total Cost and the ASNID it was delivered to
                                else{
                                    long totalCost = packet.getLinkCost() + localConfig.addressBook[j].getLinkCost();
                                    System.out.println("Packet Delivered to "+packet.getLinkID()+"\n\t Total Cost: "+totalCost);
                                }
                            }
                        }
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
