package stream;
import java.io.*;
import java.net.*;

/**
 * Exemple d'un client supportant un chat utilisant des connexions de type TCP
 * 
 *  Le ClientReceiver est un thread lancé par le client qui gère l'attente de
 *  la réception des messages en provenance du serveur
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */


public class ClientListener
	extends Thread {
	private Socket clientSocket;

	/**
	 * Constructeur de ClientListener
	 * 
	 * @param s socket de connexion au serveur
	 */
	ClientListener(Socket s) {
		this.clientSocket = s;
	}

 	/**
  	* Le thread se met en attente de reception de message en provenance du serveur
  	**/
	public void run() {
    	  try {


    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
            
    		while (true) {
				String line = socIn.readLine();
				System.out.println(line);
				
    		}
    	} catch (Exception e) {
			e.printStackTrace();
        }
       }
  }
