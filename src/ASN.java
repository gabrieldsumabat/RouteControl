import java.net.InetAddress;

public class ASN {
    private int ASNID;
    private int LinkCapacity;
    private int LinkCost;
    private int RCID;
    private InetAddress ipa;

    public ASN(int aSNID, int linkCapacity, int linkCost, int rCID, InetAddress Ipa) {
        setASNID(aSNID);
        setLinkCapacity(linkCapacity);
        setLinkCost(linkCost);
        setRCID(rCID);
        setIpa(Ipa);
    }

 /*   public RCU getRCU(ASN selfASN, ASN targetASN) {
        RCU packet = new RCU()

        return packet
    }
*/
    //SETTERS AND GETTERS

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

    public int getLinkCost() {
        return LinkCost;
    }

    public void setLinkCost(int linkCost) {
        LinkCost = linkCost;
    }

    public int getRCID() {
        return RCID;
    }

    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

    public InetAddress getIpa() {
        return ipa;
    }

    public void setIpa(InetAddress ipa) {
        this.ipa = ipa;
    }


}
