import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

class ServerThread implements Runnable
{
    private Socket connectionSocket;
    private ObjectInputStream inStream = null;
    private LinkedBlockingQueue<RCU> queue;

    public ServerThread(Socket s, LinkedBlockingQueue<RCU> q){
        try{
            System.out.println("Client Socket Connected" );
            connectionSocket=s;
            queue = q;
        }catch(Exception e){e.printStackTrace();}
    }

    public void run(){
        //Read the object
        try{
            inStream = new ObjectInputStream(connectionSocket.getInputStream());
            RCU packet = (RCU) inStream.readObject();
            System.out.println("RCU received = " + packet);
            //Set Packet Return time if measuring RTT
            if (packet.getRttFlag() == 2) {
                packet.setRttReceived();
                System.out.println("Round Trip Time (ms): " + packet.getRoundTripTime());
            }
            //Return RTT_REQUEST
            else if (packet.getRttFlag() == 1) {
                Launcher RttRes = new Launcher();
                //Update RCU and return
                packet.setRttFlag(2);
                //Limitation: All Route Controllers must be using port 1450
                RttRes.sendRCU(connectionSocket.getInetAddress(),1450, packet);
            }
            //Store in LinkedBlockingQueue for processing
            if (packet.getRttFlag() != 1) {
                try{
                    queue.put(packet);
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
            connectionSocket.close();
        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
    }
}