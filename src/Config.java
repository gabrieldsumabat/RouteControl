import java.io.*;
import java.net.InetAddress;

public class Config {
    volatile  ASN myASN;
    volatile ASN[] externalRC = new ASN[10];
    volatile ASN[] directConnect = new ASN[10];

    public Config() {
        try{
            FileInputStream fstream = new FileInputStream("config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String[] splits = br.readLine().split(" ");
            myASN = new ASN(Integer.parseInt(splits[1]), -1, 0, Integer.parseInt(splits[0]), InetAddress.getByName(splits[2]));
            int nor = Integer.parseInt(br.readLine());
            for (int i=0; i<nor; i++) {
                splits = br.readLine().split(" ");
                externalRC[i] = new ASN(Integer.parseInt(splits[1]), 1, 1, Integer.parseInt(splits[0]), InetAddress.getByName(splits[2]));
            }
            int noa = Integer.parseInt(br.readLine());
            for (int i=0; i<noa; i++) {
                splits = br.readLine().split(" ");
                externalRC[i] = new ASN(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2]), -1, null);
            }
            fstream.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    public static void main(String argv[]) {
        Config localConfig = new Config();
        System.out.println(localConfig.externalRC[1].getASNID());
    }

}
