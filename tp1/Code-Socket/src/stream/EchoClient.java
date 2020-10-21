package stream;
import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Exemple d'un client supportant un chat utilisant des connexions de type TCP
 * 
 *  EchoCLient est le client de base à lancer pour se connecter au chat
 *  celui-ci demande un host ainsi qu'un numéro de port
 *  
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */


public class EchoClient {

 
  /**
  *  main method
  *  Client permettant de se connecter au chat, celui-ci prend en argument un host et un
  *  port. Le client demande à l'utilisateur son pseudo, lance le thread ClientListener
  *  et se met en attente d'entrées sur la sortie standard, les envoyant immédiatement au 
  *  serveur.
  * 
  *  @param host Adresse IPv4 du serveur: donner localhost en cas de connexion locale
  *  @param port Port du serveur
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>\nif using make: make run_client host=<host> port=<port>");
          System.exit(1);
        }

        try {
          System.out.print("Pseudo: ");
          Scanner reader = new Scanner(System.in);
          String pseudo = reader.nextLine();

      	    // creation socket ==> connexion
      	    echoSocket = new Socket(args[0], Integer.valueOf(args[1]).intValue()); 
	    socOut= new PrintStream(echoSocket.getOutputStream());
      stdIn = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Succesfully connected as "+pseudo);
      socOut.println(pseudo);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            e.printStackTrace();
            System.exit(1);
        }
                             
        String line;
        ClientListener cl = new ClientListener(echoSocket);
        cl.start();


        while (true) {
        	line=stdIn.readLine();
          if (line.equals(".")) break;
          socOut.println(line);
        }
      socOut.close();
      stdIn.close();
      echoSocket.close();
    }
}


