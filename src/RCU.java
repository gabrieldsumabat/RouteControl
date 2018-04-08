import java.io.Serializable;
import java.lang.System;
import java.net.InetAddress;

/**
 * RCU Class is a serializable object to be communicated between Route Controllers through Socket connections.
 * The RCU implements both Advertisement updates and RTT packets.
 */

public class RCU implements Serializable {
    //Serialized Object to be exchanged over Socket Connections

    private int RCID;            //  Source RCID
    private int LinkID;          //  Target ASNID
    private InetAddress targetIP;// Target IP Address
    private int LinkType;        // (1)Overlay (2) Network
    private long LinkCost;        //  RTT Cost Function
    //RTT Variables
    private int RttFlag;         //  (0) Not in use (1) RTT_REQ (2) RTT_RES
    private long RttSent;        // Time packet was sent
    private long RttReceived;    // Time packet finished round trip
    //Route Advertisement
    private ASN[] advertisement;

    /**
     *  Creates new RCU Packet
     * @param Source RCID of Source ASN
     * @param NextHop ASNID of target ASN
     * @param HopType 2 if target ASN has a RCID, 1 if ASN has no RCID
     * @param Cost Sender's Link Cost
     * @param RTT_Update RTT Flag, 1 for RTT_REQ and 0 for RCU Advertisement
     * @param target IP Address of target ASN
     */
    public RCU(int Source, int NextHop, int HopType, long Cost, int RTT_Update, InetAddress target) {

        setRCID(Source);
        setLinkID(NextHop);
        setLinkType(HopType);
        setLinkCost(Cost);
        setTargetIP(target);
        setRttFlag(RTT_Update);
        if (RTT_Update == 1) {
            setRttSent();
        } else if (RTT_Update == 0) {
            advertisement = RouteController.LocalConfig.addressBook;
        }
    }

    /**
     * Gets the Address Book advertised by ASN
     * @return Array of ASN known by sender
     */
     public ASN[] getAd(){
        return advertisement;
    }

    /**
     * Returns RCID of sending ASN
     * @return int RC ID
     */
    public int getRCID() {
        return RCID;
    }

    /**
     * Updates RCU Sending RC ID value
     * @param RCID New int RC ID
     */
    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

    /**
     * Return ASNID of Target ASN
     * @return ASNID of Target ASN
     */
    public int getLinkID() {
        return LinkID;
    }

    /**
     * Set Target ASN ID for RCU Packet
     * @param linkID ASNID of Target Network
     */
    public void setLinkID(int linkID) {
        LinkID = linkID;
    }

    /**
     * Return Link Type
     * @return 1 if ASN with no RC, 2 if ASN with RC
     */
    public int getLinkType() {
        return LinkType;
    }

    /**
     * Set the Link Type
     * @param linkType int representing the linkType. 1 if ASN without RC, 2 if ASN with RC
     */
    public void setLinkType(int linkType) {
        LinkType = linkType;
    }

    /**
     * Returns Link Cost as a long
     * @return long LinkCost
     */
    public long getLinkCost() {
        return LinkCost;
    }

    /**
     * Set RCU Link Cost
     * @param linkCost new long LinkCost value
     */
    public void setLinkCost(long linkCost) {
        LinkCost = linkCost;
    }

    /**
     * Returns RttFlag
     * @return 0 if RCU Advertisement, 1 if RTT_REQ, 2 if RTT_RESP
     */
    public int getRttFlag() {
        return RttFlag;
    }

    /**
     * Sets RttFlag to desired value. 0 for RCU Advertisement, 1 for RTT_REQ, 2 for RTT_RESP
     * @param rttFlag int from 0 to 2
     */
    public void setRttFlag(int rttFlag) {
        RttFlag = rttFlag;
    }

    /**
     * Return Time when RTT was Sent
     * @return time when RTT_REQ was sent
     */
    public long getRttSent() {
        return RttSent;
    }

    /**
     * Sets the RttSent value to current millisecond
     */
    public void setRttSent() {
        RttSent = System.currentTimeMillis();
    }

    /**
     * Returns RTT Receive Time
     * @return long time when RTT is received
     */
    public long getRttReceived() {
        return RttReceived;
    }

    /**
     * Set RttReceive time to current millisecond
     */
    public void setRttReceived() {
        RttReceived = System.currentTimeMillis();
    }

    /**
     * Returns RCU destination ASN InetAddress
     * @return
     */
    public InetAddress getTargetIP() {
        return targetIP;
    }

    /**
     * Set destination InetAddress for packet
     * @param targetIP InetAddress of target ASN
     */
    public void setTargetIP(InetAddress targetIP) {
        this.targetIP = targetIP;
    }

    /**
     * Returns the Round Trip Time
     * @return long Round Trip Time, -1 if RCU is an Advertisement update
     */
    public long getRoundTripTime()
    {
        if (getRttFlag() != 0) {
            return getRttReceived() - getRttSent();
        }
        else {
            return (-1);
        }
    }

//METHODS TO ALLOW SERIALIZATION OF THE OBJECT
    @Override
    public int hashCode() {
        return RCID;
    }


    @Override
    public String toString() {

        return  "\n\n\t RC ID:                " + getRCID() +
                "\n\t Link ID:              " + getLinkID() +
                "\n\t Link Type:            " + getLinkType() +
                "\n\t Link Cost:            " + getLinkCost() +
                "\n\t RTT Flag:             " + getRttFlag() +
                "\n";

    }

}
