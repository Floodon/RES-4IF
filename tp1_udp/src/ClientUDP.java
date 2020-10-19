import java.io.*;
import java.net.*;
import java.util.*;


public class ClientUDP {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java ClientUDP <ServerUDP host> <ServerUDP port>");
            System.exit(1);
          }
        //asking the user his username
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String pseudo;
        System.out.print("pseudo: ");
        pseudo=stdIn.readLine();
    
        //starting the thread which receives the messages
        ClientReceiver receiver = new ClientReceiver(args[0], Integer.parseInt(args[1]));
        receiver.start();

        DatagramSocket socket = new DatagramSocket(); 
        byte[] buf = new byte[256]; 
        // Get server’s IP adress
        InetAddress groupAddr = InetAddress.getByName(args[0]);
        String line;

        // Tell everyone you're connected !
        line = pseudo+" s'est connecté !";
        buf = line.getBytes();
        // Create a datagram packet destined for the server
        DatagramPacket packet_login = new DatagramPacket(buf, buf.length, groupAddr, Integer.parseInt(args[1]));
        // Send datagram packet to server
        socket.send(packet_login); 
        while(true) {
            // Build a request
            line=stdIn.readLine();
            if (line.equals(".")) break;
            line = pseudo+": "+line;
            buf=line.getBytes();
            // Create a datagram packet destined for the server
            DatagramPacket packet = new DatagramPacket(buf, buf.length, groupAddr, Integer.parseInt(args[1]));
            // Send datagram packet to server
            socket.send(packet); 
        }
        // Tell everyone you're disconnected !
        line = pseudo+" s'est déconnecté !";
        buf = line.getBytes();
        DatagramPacket packet_logout = new DatagramPacket(buf, buf.length, groupAddr, Integer.parseInt(args[1]));
        socket.send(packet_logout); 
        socket.close();
        receiver.arreter();
    }
}
