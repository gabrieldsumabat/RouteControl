import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ASN Class to represent each Node in the Network
 * Implements Serializable
 * @see #ASN(int, int, int, int, String)
 * @see #getRCU(ASN, int)
 */
public class ASN implements Serializable {
    //Object storing information about each possible address
    private int ASNID;          //RC+ASN
    private int LinkCapacity;   //ASN
    private long LinkCost;       //ASN
    private int RCID;           //RC
    private String ipa;         //RC, May be address of next hop!
    private int hop;            //0 if direct connection

    /**
     * ASN Constructor, if no Route Controller set rcID to -1.
     * @param aSNID ID of ASN Node
     * @param linkCapacity Deprecated, Socket currently implicitly determines link capacity.
     * @param linkCost Round Trip Transit Time.
     * @param rCID ID of Route Controller, -1 if it does not contain a Route Controller.
     * @param Ipa IP Address of ASN. 'null' if direct connection with no Route Controller.
     */

    public ASN(int aSNID, int linkCapacity, int linkCost, int rCID, String Ipa) {
        setASNID(aSNID);
        setLinkCapacity(linkCapacity);
        LinkCost = linkCost;
        setRCID(rCID);
        setIpa(Ipa);
        hop = 0;
    }

    /**
     * Populates a RCU packet ready to be delivered to the Target ASN
     * @param targetASN ASN to be delivered RCU
     * @param RTTFlag RTTFlag 0 indicates RCU Update, RTTFlag 1 indicates RTT_REQ
     * @return Returns RCU, else returns null if host is unknown.
     */

    public RCU getRCU(ASN targetASN, int RTTFlag) {
        try {
            InetAddress ipaddress =InetAddress.getByName(targetASN.getIpa());
            RCU packet = new RCU(RouteController.LocalConfig.myASN.getRCID(), targetASN.getASNID(),2,targetASN.getLinkCost(),RTTFlag,ipaddress);
            if (targetASN.getRCID() != -1) {
                packet.setLinkType(1);
            }
            return packet;
        } catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the current next hop, if any, to reach the ASN
     * @param hopID ASNID, 0 if a direct connection.
     */
    public void setHop(int hopID){
        hop = hopID;
    }

    /**
     * Checks if the hopID is the ASNID of the current next hop
     * @param hopID ASNID to be compared against the current hopID
     * @return returns 'true' if current hopID is equal to input ASNID, false otherwise
     */
    public boolean checkHop (int hopID) {
        return (hop == hopID);
    }

    /**
     *  Returns the ASNID
     * @return ASNID as an int
     */
    public int getASNID() {
        return ASNID;
    }

    /**
     * Set ASNID
     * @param ASNID Change ASNID to this value
     */
    public void setASNID(int ASNID) {
        this.ASNID = ASNID;
    }

    /**
     * Returns Link Capacity, NOTE: Currently depreciated, Capacity obtained implicitly through Socket connection
     * @return LinkCapacity value
     */
    public int getLinkCapacity() {
        return LinkCapacity;
    }

    /**
     * Sets the Link Capacity Value for ASN, currently depreciated
     * @param linkCapacity New int value for Link Capacity
     */
    public void setLinkCapacity(int linkCapacity) {
        LinkCapacity = linkCapacity;
    }

    /**
     * Returns current Link Cost Value
     * @return LinkCost as a long.
     */
    public long getLinkCost() {
        return LinkCost;
    }

    /**
     * Sets Link Cost, if the new LinkCost is smaller by 100 or more, it will replace the old Link Cost.
     * Else, LinkCost = (new_cost*2+old_cost*8)/10
     * @param linkCost Link Cost Update
     */
    public void setLinkCost(long linkCost) {
        if ((LinkCost - linkCost) > 100) {
            LinkCost = linkCost;
        } else {
            LinkCost = (linkCost*2 + getLinkCost()*8)/10;
        }
    }

    /**
     * Forces Link Cost to be the new updated value. Used in discovering new ASNs.
     * @param linkCost Link Cost discovered
     */
    public void forceLinkCost(long linkCost) {
        LinkCost = linkCost;
    }

    /**
     * Returns current RCID
     * @return RCID as an int
     */
    public int getRCID() {
        return RCID;
    }

    /**
     * Change RCID to input int
     * @param RCID New RCID
     */
    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

    /**
     * Returns current IP Address as a String
     * @return IP Address as a String
     */
    public String getIpa() {
        return ipa;
    }

    /**
     * Sets current IP Address to input String
     * @param ipa New IP Address, ex. 192.2.1.1
     */
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
                "\n\t hop:                     "+ hop +
                "\n";

    }
}
