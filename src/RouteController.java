public class RouteController {

    public static void main(String argv[]) {
        //Start the Listener Thread
        Listener RouteListen = new Listener();
        Thread listenerThread = new Thread(RouteListen);
        listenerThread.start();
        //main timer here:

    }

}
