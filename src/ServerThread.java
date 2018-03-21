import java.io.*;
import java.net.*;

class ServerThread implements Runnable
{
    Socket connectionSocket;
    private ObjectInputStream inStream = null;

    public ServerThread(Socket s){
        try{
            System.out.println("Client Socket Connected" );
            connectionSocket=s;
        }catch(Exception e){e.printStackTrace();}
    }

    public void run(){
        //Need to still add RCU update math
        try{
            inStream = new ObjectInputStream(connectionSocket.getInputStream());
            RCU packet = (RCU) inStream.readObject();
            System.out.println("RCU received = " + packet);
            if (packet.getRttFlag() == 2) {
                System.out.println("Round Trip Time (ms): " + packet.getRoundTripTime());
            }
            else if (packet.getRttFlag() == 1) {

                Launcher RttRes = new Launcher();
                //Update RCU and return
                packet.setRttFlag(2);
                //NEED TO SET FUNCTION TO PROPERLY RETRIEVE HOSTNAME + PORT
                RttRes.sendRCU("localhost",4445, packet);
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