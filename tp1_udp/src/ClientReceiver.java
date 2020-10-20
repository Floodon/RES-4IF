import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Exemple d'un client supportant un chat utilisant des connexions de type UDP
 * 
 *  Le ClientReceiver est un thread lancé par le client qui gère l'attente de
 *  la réception des messages en provenance du MulticastSocket via l'adresse IP
 *  donnée en argument "host" de la classe ClientUDP
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */



public class ClientReceiver extends Thread {
    /**
	 * 
	 * @see arreter
	 * @see run
     * 
     * @att

	 */
    protected volatile boolean running = true;
    /**
     * Adresse ("host") du multicast
     */
    String addr;
    /**
     * Port du multicast
     */
    int port;
    /**
     * Socket relié au multicast
     */
    MulticastSocket socket;

    /**
     * Le constructeur de la classe initialise les attributs addr et port
     * 
     * @param addr
     * @param port
     */
    ClientReceiver(String addr, int port) {
        this.addr = addr;
        this.port = port;

    }
    /**
     * La fonction arreter() permet de mettre fin au thread et de briser la boucle
     * infinie.
     * 
     * Cette méthode a dû être implémentée car la méthode stop() de la classe Thread
     * est maintenant depreciated
     * 
     */
    public void arreter() {
        this.running = false;
    }

    /**
     * Fonction run héritée de thread
     * La boucle attend infiniment de recevoir des packets, et les affiche immédiatement
     * à l'utilisateur sur la sortie standard
     * 
     */
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
