import java.util.Scanner;

public class Command implements  Runnable {

    public Command(){
    }

    @Override
    public void run() {
        while(true) {
            Scanner in = new Scanner(System.in);

            System.out.println("\n User Commands: \n\t  addRC [aSNID] [Capacity] [Cost] [RCID] [IPA]" +
                               "\n\t  sendRCU [aSNID] \n\t  sendRTT [aSNID] \n\t  print");
            String words = in.nextLine();
            String[] args = words.split(" ");
            if (args.length > 0) {
                switch (args[0]) {
                    case "addRC":
                        if (args.length == 6) {
                            addRC(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5]);
                        } else {
                            System.out.println("Not enough arguments.");
                        }
                    case "sendRCU":
                        if (args.length == 2) {
                            sendRCU(Integer.parseInt(args[1]));
                        } else {
                            System.out.println("Please specify ASNID.");
                        }
                    case "sendRTT":
                        if (args.length == 2) {
                            sendRTT(Integer.parseInt(args[1]));
                        } else {
                            System.out.println("Please specify ASNID");
                        }
                    case "print":
                        print();
                    default:
                        System.out.println("");

                }
            }
        }
    }

    private void addRC (int aSNID, int linkCapacity, int linkCost, int rCID, String Ipa) {
        ASN newASN = new ASN(aSNID, linkCapacity, linkCost, rCID, Ipa);
        RouteController.LocalConfig.setNoa(RouteController.LocalConfig.getNoa()+1);
        RouteController.LocalConfig.addressBook[RouteController.LocalConfig.getNoa()]=newASN;
        System.out.println("Added a new ASN :" + newASN);
    }

    private void sendRCU(int aSNID){
        for (ASN id: RouteController.LocalConfig.addressBook) {
            if (id != null) {
                if (id.getASNID() == aSNID) {
                    RCU RCpacket = id.getRCU(id, 0);
                    Launcher rcuLaunch = new Launcher();
                    rcuLaunch.sendRCU(RCpacket.getTargetIP(), 1450, RCpacket);
                    System.out.println("Sending RCU to " + aSNID);
                }
            }
        }
    }

   private void sendRTT (int aSNID){
       for (ASN id: RouteController.LocalConfig.addressBook) {
           if (id != null) {
               if (id.getASNID() == aSNID) {
                   RCU RtPacket = id.getRCU(id, 1);
                   Launcher rcuLaunch = new Launcher();
                   rcuLaunch.sendRCU(RtPacket.getTargetIP(), 1450, RtPacket);
                   System.out.println("Sending RTT to " + aSNID);
               }
           }
       }
   }

   private void print(){
       System.out.println("Local ASN Values: ");
       System.out.println(RouteController.LocalConfig.myASN);
       System.out.println("Address Book: ");
       for (int j=0; j<RouteController.LocalConfig.getNoa();j++) {
           System.out.println(RouteController.LocalConfig.addressBook[j]);
       }
   }

}
