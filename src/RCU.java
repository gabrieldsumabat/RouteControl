import java.io.Serializable;
import java.lang.System;
import java.net.InetAddress;

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

    public RCU(int Source, int NextHop, int HopType, long Cost, int RTT_Update, InetAddress target) {

        setRCID(Source);
        setLinkID(NextHop);
        setLinkType(HopType);
        setLinkCost(Cost);
        setTargetIP(target);
        setRttFlag(RTT_Update);
        if (RTT_Update == 1) {
            setRttSent();
        }
    }

    public void setAd(ASN[] ad){
        advertisement = ad;
    }

    public ASN[] getAd(){
        return advertisement;
    }

    public int getRCID() {
        return RCID;
    }

    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

    public int getLinkID() {
        return LinkID;
    }

    public void setLinkID(int linkID) {
        LinkID = linkID;
    }

    public int getLinkType() {
        return LinkType;
    }

    public void setLinkType(int linkType) {
        LinkType = linkType;
    }

    public long getLinkCost() {
        return LinkCost;
    }

    public void setLinkCost(long linkCost) {
        LinkCost = linkCost;
    }

    public int getRttFlag() {
        return RttFlag;
    }

    public void setRttFlag(int rttFlag) {
        RttFlag = rttFlag;
    }

    public long getRttSent() {
        return RttSent;
    }

    public void setRttSent() {
        RttSent = System.currentTimeMillis();
    }

    public long getRttReceived() {
        return RttReceived;
    }

    public void setRttReceived() {
        RttReceived = System.currentTimeMillis();
    }

    public InetAddress getTargetIP() {
        return targetIP;
    }

    public void setTargetIP(InetAddress targetIP) {
        this.targetIP = targetIP;
    }
//Package Methods
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
