import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Exemple d'un client supportant un chat utilisant des connexions de type UDP
 * 
 * Le multicast permet de ne pas avoir besoin de serveur, en effet chaque utilisateur
 * "abonné" à l'adresse multicast donnée peut envoyée et recevra les messages envoyés
 * par les autres "abonnés"
 * 
 *  ClientUDP est le client de base à lancer pour se connecter au chat
 *  celui-ci demande un host ainsi qu'un numéro de port
 *  
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */


public class ClientUDP {
    /**
     * 
     * Main method
     * 
     *  
     *      - Demande à l'utilisateur son pseudo
     *      - Lance le thread ClientReceiver chargé d'être en attente des messages en 
     *         provenance de l'adresse multicast et les afficher à l'utilisateur
     *      - Envoie un message à tous le monde indiquant qu'il s'est connecté au chat
     *      - Se met indéfiniment en attente de message en provenance de l'utilisateur
     *        sur la sortie standard et les envois aux autres utilisateurs formatés avec 
     *        son pseudo
     *      - Indique à tous le monde lorsqu'il quitte le chat
     * 
     * 
     *  @param host est une adresse IPv4, ne peut pas être "localhost" et doit 
     *  être contenu entre 224.0.0.0 et 239.255.255.255
     *  @param port est le port associé à l'adresse donnée
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java ClientUDP <host> <port>\nif using make: make run host=<host> port=<port>");
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
