import java.io.*;


public class Config {
    volatile   ASN myASN;
    private ASN[] externalRC = new ASN[25];
    volatile ASN[] addressBook = new ASN[25];
    private int nor;
    private volatile int noa;

    public Config() {
        try{
            FileInputStream fstream = new FileInputStream("config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String[] splits = br.readLine().split(" ");
            myASN = new ASN(Integer.parseInt(splits[1]), -1, 0, Integer.parseInt(splits[0]), splits[2]);
            nor = Integer.parseInt(br.readLine());
            for (int i=0; i<nor; i++) {
                splits = br.readLine().split(" ");
                //Link Cost is initiated to 5000 to ensure the first setLinkCost will override the value.
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

    public int getASNfromRC(int RCID){
        for (int l=0; l<noa;l++){
            if (addressBook[l].getRCID() == RCID) {
                return addressBook[l].getASNID();
            }
        }
        return (-1);
    }

    public String getIPAfromASN(int ASN){
        for (int a=0;a<noa;a++){
            if (addressBook[a].getASNID()==ASN){
                return addressBook[a].getIpa();
            }
        }
        return null;
    }

    public Boolean ASNinBook(int ASN){
        for (int a=0;a<noa;a++){
            if (addressBook[a].getASNID()==ASN){
                return true;
            }
        }
        return false;
    }

    public int getNoa() {
        return noa;
    }

    public void setNoa(int noadd){
        noa = noadd;
    }

    public static void main(String argv[]) {
        Config localConfig = new Config();
        System.out.println(localConfig.myASN);
        for (int j=0; j<4;j++) {
            System.out.println(localConfig.addressBook[j]);
        }
    }
}
