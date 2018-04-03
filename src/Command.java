public class Command implements  Runnable {
    volatile Config LocalConfig;

    public Command(Config locale){
        LocalConfig = locale;
    }

    @Override
    public void run() {
        //Read User Input
        //Perform Operation

    }

    private void addASN (int aSNID, int linkCapacity, int linkCost, int rCID, String Ipa) {
        ASN newASN = new ASN(aSNID, linkCapacity, linkCost, rCID, Ipa);
        LocalConfig.setNoa(LocalConfig.getNoa()+1);
        LocalConfig.addressBook[LocalConfig.getNoa()]=newASN;
        System.out.println("Added a new ASN :" + newASN);
    }

   private void sendPacket(int aSNID, int RTT_Flag){
        //SEND PACKET
   }

}
