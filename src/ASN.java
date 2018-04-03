import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class ASN {
    //Object storing information about each possible address
    private int ASNID;          //RC+ASN
    private int LinkCapacity;   //ASN
    private long LinkCost;       //ASN
    private int RCID;           //RC
    private String ipa;         //RC, May be address of next hop!

    //MAYBE?
    private List<Integer> Hops; //Next Hop in Network

    public ASN(int aSNID, int linkCapacity, int linkCost, int rCID, String Ipa) {
        setASNID(aSNID);
        setLinkCapacity(linkCapacity);
        setLinkCost(linkCost);
        setRCID(rCID);
        setIpa(Ipa);
        Hops = new LinkedList<>();
    }

    public RCU getRCU(ASN targetASN, int RTTFlag) {
        try {
            InetAddress ipaddress =InetAddress.getByName(targetASN.getIpa());
            RCU packet = new RCU(this.getRCID(), targetASN.getASNID(),2,targetASN.getLinkCost(),RTTFlag,ipaddress);
            if (targetASN.getRCID() != -1) {
                packet.setLinkType(1);
            }
            return packet;
        } catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }
        return null;
    }

    //SETTERS AND GETTERS

    public void addHop(int hopID){
        Hops.add(hopID);
    }

    public boolean checkHop (int hopID) {
        return (Hops.contains(hopID));
    }

    public int getASNID() {
        return ASNID;
    }

    public void setASNID(int ASNID) {
        this.ASNID = ASNID;
    }

    public int getLinkCapacity() {
        return LinkCapacity;
    }

    public void setLinkCapacity(int linkCapacity) {
        LinkCapacity = linkCapacity;
    }

    public long getLinkCost() {
        return LinkCost;
    }

    public void setLinkCost(long linkCost) {
        LinkCost = linkCost;
    }

    public int getRCID() {
        return RCID;
    }

    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    //METHODS TO ALLOW SERIALIZATION OF THE OBJECT
    @Override
    public int hashCode() {
        return ASNID;
    }

    @Override
    public String toString() {

        return  "\n\n\t ASN ID:                  " + getASNID() +
                "\n\t RC ID:                   " + getRCID() +
                "\n\t Link Capacity:           " + getLinkCapacity() +
                "\n\t Link Cost:               " + getLinkCost() +
                "\n\t IP:                      " + getIpa() +
                "\n";

    }
}
