import sun.java2d.cmm.lcms.LcmsServiceProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable{
    private LinkedBlockingQueue<RCU> queue;
    volatile Config LocalConfig;
    private RCU packet;

    public Consumer(LinkedBlockingQueue<RCU> q, Config localconfig) {
        queue=q;
        LocalConfig =localconfig;
    }

    @Override
    public void run() {
        while(true) {
            try {
                packet = queue.take();
                System.out.println("Packet Being Consumed: \n" + packet + "\n\n");
                //If RTT_RESP for our RTT_REQ
                if (packet.getRttFlag()==2 && packet.getLinkID()== LocalConfig.myASN.getASNID()){
                    //Update ASN Cost 0.8*last_cost+0.2*current_cost, where cost is RTT
                    for (int i = 0; i< LocalConfig.getNoa(); i++){
                        if (LocalConfig.addressBook[i].getRCID()==packet.getRCID()){
                            LocalConfig.addressBook[i].setLinkCost((packet.getRoundTripTime()*2+ LocalConfig.addressBook[i].getLinkCost()*8)/10);
                            System.out.println("Updating "+ LocalConfig.addressBook[i].getRCID()+" cost to "+ LocalConfig.addressBook[i].getLinkCost()+"\n");
                        }
                    }
                }
                    //If Target ID is Local ID -> Compare Advertised AddressBook costs with local AddressBook
                    else if (packet.getLinkID()== LocalConfig.myASN.getASNID()) {
                            ASN[] AdSet = packet.getAd();
                            //For Each Address in the Advertisement, Compare Costs and Update Paths
                            for(ASN Ad:AdSet){
                                //Check if ASN is known
                                if (LocalConfig.ASNinBook(Ad.getASNID())) {
                                    //Update AddressBook Costs based on Advertisement
                                    long netCost = Ad.getLinkCost() + packet.getLinkCost();
                                    for (ASN target : LocalConfig.addressBook) {
                                        if (Ad.getASNID() == target.getASNID() && target.getLinkCost() > netCost) {
                                            target.setLinkCost(netCost);
                                            //If this Hop is not yet considered
                                            if (target.checkHop(packet.getLinkID())) {
                                                target.addHop(packet.getLinkID());
                                            }
                                            target.setIpa(LocalConfig.getIPAfromASN(LocalConfig.getASNfromRC(packet.getRCID())));
                                            System.out.println("Updating "+target.getASNID()+" cost to "+netCost+".\n");
                                        }
                                    }
                                } //Else Add ASN to network
                                else {
                                    LocalConfig.setNoa(LocalConfig.getNoa() + 1); //Increase the total number of addresses by one
                                    LocalConfig.addressBook[LocalConfig.getNoa()] = Ad; //Set new ASN to the Ad
                                    LocalConfig.addressBook[LocalConfig.getNoa()].addHop(LocalConfig.getASNfromRC(packet.getRCID())); //Add hop from Packet source
                                    System.out.println("Discovered new ASN: "+LocalConfig.addressBook[LocalConfig.getNoa()]);
                                }
                            }
                    }
                else {
                    //If packet's Target ID is not Local ID -> Forward packet to the ASNID
                    if (packet.getLinkID()!= LocalConfig.myASN.getASNID()){
                        for (int j = 0; j< LocalConfig.getNoa(); j++){
                            if (LocalConfig.addressBook[j].getASNID()==packet.getLinkID()){
                                //If the Target ASN is a different RC -> Forward Packet to RC
                                if (LocalConfig.addressBook[j].getIpa()!=null){
                                    packet.setTargetIP(InetAddress.getByName(LocalConfig.addressBook[j].getIpa()));
                                    Launcher client = new Launcher();
                                    client.sendRCU(packet.getTargetIP(),1450, packet);
                                }
                                //If the ASN has no RC -> Print Total Cost and the ASNID it was delivered to
                                else{
                                    long totalCost = packet.getLinkCost() + LocalConfig.addressBook[j].getLinkCost();
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
