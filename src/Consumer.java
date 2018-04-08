import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;


public class Consumer implements Runnable{
    private LinkedBlockingQueue<RCU> queue;
    private RCU packet;

    public Consumer(LinkedBlockingQueue<RCU> q) {
        queue=q;
    }

    @Override
    public void run() {
        while(true) {
            try {
                packet = queue.take();
                System.out.println("Packet Being Consumed: \n" + packet + "\n\n");
                //If RTT_RESP for our RTT_REQ
                if (packet.getRttFlag()==2 && packet.getLinkID()== RouteController.LocalConfig.myASN.getASNID()){
                    //Update ASN Cost
                    for (int i = 0; i< RouteController.LocalConfig.getNoa(); i++){
                        if (RouteController.LocalConfig.addressBook[i].getRCID()==packet.getRCID()){
                            RouteController.LocalConfig.addressBook[i].setLinkCost(packet.getRoundTripTime());
                            System.out.println("Updating "+ RouteController.LocalConfig.addressBook[i].getRCID()+" cost to "+ RouteController.LocalConfig.addressBook[i].getLinkCost()+"\n");
                        }
                    }
                }
                    //If Target ID is Local ID -> Compare Advertised AddressBook costs with local AddressBook
                    else if (packet.getLinkID()== RouteController.LocalConfig.myASN.getASNID()) {
                            ASN[] AdSet = packet.getAd();
                            //For Each Address in the Advertisement, Compare Costs and Update Paths
                            for(ASN Ad:AdSet) {
                                if (Ad != null) {
                                    long netCost = Ad.getLinkCost() + packet.getLinkCost();
                                    //Check if ASN is known
                                    if (RouteController.LocalConfig.ASNinBook(Ad.getASNID()) && Ad.getASNID() != RouteController.LocalConfig.myASN.getASNID()) {
                                        //Update AddressBook Costs based on Advertisement
                                        for (ASN target : RouteController.LocalConfig.addressBook) {
                                            if (target != null) {
                                                if (Ad.getASNID() == target.getASNID() && target.getLinkCost() > netCost) {
                                                    target.setLinkCost(netCost);
                                                    //If this Hop is not yet considered
                                                    if (target.checkHop(RouteController.LocalConfig.getASNfromRC(packet.getRCID()))) {
                                                        target.setHop(RouteController.LocalConfig.getASNfromRC(packet.getRCID()));
                                                    }
                                                    target.setIpa(RouteController.LocalConfig.getIPAfromASN(RouteController.LocalConfig.getASNfromRC(packet.getRCID())));
                                                    System.out.println("Updating RC#" + target.getASNID() + " cost to " + netCost + ".\n");
                                                }
                                            }
                                        }
                                    } //Else Add ASN to network
                                    else if (Ad.getASNID() != RouteController.LocalConfig.myASN.getASNID()){
                                        RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa()] = Ad; //Set new ASN to the Ad
                                        RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa()].setLinkCost(netCost);
                                        RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa()].setIpa(RouteController.LocalConfig.getIPAfromASN(RouteController.LocalConfig.getASNfromRC(packet.getRCID()))); //Set IPA to incoming
                                        RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa()].setHop(RouteController.LocalConfig.getASNfromRC(packet.getRCID())); //Set hop from Packet source
                                        RouteController.LocalConfig.setNoa(RouteController.LocalConfig.getNoa() + 1); //Increase the total number of addresses by one
                                        System.out.println("Discovered new ASN: " + RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa() - 1]);
                                    }
                                }
                            }
                    }
                else {
                    //If packet's Target ID is not Local ID -> Forward packet to the ASNID
                    if (packet.getLinkID()!= RouteController.LocalConfig.myASN.getASNID()){
                        for (int j = 0; j< RouteController.LocalConfig.getNoa(); j++){
                            if (RouteController.LocalConfig.addressBook[j].getASNID()==packet.getLinkID()){
                                //If the Target ASN is a different RC -> Forward Packet to RC
                                if (RouteController.LocalConfig.addressBook[j].getIpa()!=null){
                                    packet.setTargetIP(InetAddress.getByName(RouteController.LocalConfig.addressBook[j].getIpa()));
                                    Launcher client = new Launcher();
                                    client.sendRCU(packet.getTargetIP(),1450, packet);
                                }
                                //If the ASN has no RC -> Print Total Cost and the ASNID it was delivered to
                                else{
                                    long totalCost = packet.getLinkCost() + RouteController.LocalConfig.addressBook[j].getLinkCost();
                                    System.out.println("Packet Delivered to "+packet.getLinkID()+"\n\t estimated Cost: " + totalCost);
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
