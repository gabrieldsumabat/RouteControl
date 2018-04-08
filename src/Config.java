import java.io.*;

/**
 * Initializes the Starting State of the Route Controller
 * Populates the Address Book with the Immediate Neighbors
 */
public class Config {
    volatile   ASN myASN;
    private ASN[] externalRC = new ASN[25];
    volatile ASN[] addressBook = new ASN[25];
    private int nor;
    private volatile int noa;

    /**
     *  Populates myASN with local configuration values and Address Book with all known direct connections
     *  Reads from file config.txt
     *  First line: RCID ASNID IPA  //Of local ASN
     *  Second line: R[Number of Known Directly Connected Route Controllers]
     *  Third line to 3+R : RCID ASNID IPA //of each Directly Connected Route Controller
     *  (3+R)+1 line: A [Number of directly connected ASN]
     *  (3+R)+2 to EoF: ASNID LinkCapacity LinkCost
     *
     * EX:
     * 1 100 192.168.2.179
     * 1
     * 2 200 192.168.2.17
     * 3
     * 10 2 5
     * 20 5 5
     * 200 10 500
     */
    public Config() {
        try{
            FileInputStream fstream = new FileInputStream("config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String[] splits = br.readLine().split(" ");
            myASN = new ASN(Integer.parseInt(splits[1]), -1, 0, Integer.parseInt(splits[0]), splits[2]);
            nor = Integer.parseInt(br.readLine());
            for (int i=0; i<nor; i++) {
                splits = br.readLine().split(" ");
                //Link Cost is initiated to 500 to ensure the first setLinkCost will override the value.
                externalRC[i] = new ASN(Integer.parseInt(splits[1]), 1, 500, Integer.parseInt(splits[0]), splits[2]);
            }
            noa = Integer.parseInt(br.readLine());
            for (int i=0; i<noa; i++) {
                splits = br.readLine().split(" ");
                addressBook[i] = new ASN(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2]), -1, null);
            }
            //Combine addressBook with externalRC
            for (int j=0; j<noa;j++) {
                for(int k=0; k<nor; k++){
                    if (addressBook[j].getASNID()==externalRC[k].getASNID()) {
                        addressBook[j].setIpa(externalRC[k].getIpa());
                        addressBook[j].setRCID(externalRC[k].getRCID());
                    }
                }
            }
            fstream.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    /**
     * Returns the ASN ID containing the input Route Controller ID
     * @param RCID ID of Route Controller
     * @return ASNID if Route Controller exists in known ASN, else 0
     */
    public int getASNfromRC(int RCID){
        for (int l=0; l<noa;l++){
            if (addressBook[l].getRCID() == RCID) {
                return addressBook[l].getASNID();
            }
        }
        return (0);
    }

    /**
     * Returns IP Address of ASN
     * @param ASN ASN ID as an int
     * @return IPA of ASN, if ASN does not exist it will return null
     */
    public String getIPAfromASN(int ASN){
        for (int a=0;a<noa;a++){
            if (addressBook[a].getASNID()==ASN){
                return addressBook[a].getIpa();
            }
        }
        return null;
    }

    /**
     * Checks if the ASN is known to Route Controller
     * @param ASN ASNID of advertised network
     * @return true if the ASN is already known, false if it does not exist in address book
     */
    public Boolean ASNinBook(int ASN){
        for (int a=0;a<noa;a++){
            if (addressBook[a].getASNID()==ASN){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns Number of Addresses known to Local Route Controller
     * @return int value representing number of known addresses
     */
    public int getNoa() {
        return noa;
    }

    /**
     * Sets the Number of Addresses known to the Local Route Controller
     * @param noadd int value of the new total number of addresses known
     */
    public void setNoa(int noadd){
        noa = noadd;
    }
}
