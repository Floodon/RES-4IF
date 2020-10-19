import java.net.*;
import java.io.*;
import java.util.*;

public class ClientReceiver extends Thread {
    protected volatile boolean running = true;
    String addr;
    int port;
    MulticastSocket socket;
    ClientReceiver(String addr, int port) {
        this.addr = addr;
        this.port = port;

    }

    public void arreter() {
        this.running = false;
    }

    public void run(){ 
        try {
            // Create a multicast socket and register to it
            socket = new MulticastSocket(this.port);
            InetAddress group = InetAddress.getByName(this.addr);
            socket.joinGroup(group);

            while (running == true) {
                byte[] buf = new byte[256];
                // Create a datagram packet
                DatagramPacket packet = new DatagramPacket(buf, buf.length); 
                // Wait for a packet
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
