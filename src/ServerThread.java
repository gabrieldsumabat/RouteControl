import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

class ServerThread implements Runnable
{
    private Socket connectionSocket;
    private ObjectInputStream inStream = null;
    private LinkedBlockingQueue<RCU> queue;
    volatile Config LocalConfig;

    public ServerThread(Socket s, LinkedBlockingQueue<RCU> q, Config localconfig){
        try{
            System.out.println("Client Socket Connected" );
            connectionSocket=s;
            queue = q;
            LocalConfig = localconfig;
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void run(){
        //Read incoming Object
        try{
            inStream = new ObjectInputStream(connectionSocket.getInputStream());
            RCU packet = (RCU) inStream.readObject();
            System.out.println("RCU received = " + packet);
            //Set Packet Return time if measuring RTT
            if (packet.getRttFlag() == 2) {
                packet.setRttReceived();
                System.out.println("Round Trip Time (ms): " + packet.getRoundTripTime()+"\n");
            }
            //Return RTT_REQUEST if targeted to local ID
            if (packet.getRttFlag() == 1 && packet.getLinkID()==LocalConfig.myASN.getASNID()) {
                Launcher RttRes = new Launcher();
                packet.setRttFlag(2);
                packet.setTargetIP(connectionSocket.getInetAddress());
                packet.setLinkID(LocalConfig.getASNfromRC(packet.getRCID()));
                packet.setRCID(LocalConfig.myASN.getRCID());
                //Limitation: All Route Controllers must be using port 1450
                RttRes.sendRCU(packet.getTargetIP(),1450, packet);
            }
            //ELSE: Store in LinkedBlockingQueue for processing
            else {
                try{
                    queue.put(packet);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
    }
}