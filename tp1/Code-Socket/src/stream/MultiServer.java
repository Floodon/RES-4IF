package stream;
import java.net.*;
import java.io.*;

/**
 * Exemple d'un serveur supportant un chat utilisant des connexions de type TCP
 * 
 *  Le MultiServeur est la classe à lancer pour lancer le serveur
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */

public class MultiServer {
  
 	/**
	* main method
	* Créer le fichier d'historique s'il n'existe pas, puis se met en attente de connexion.
	* Lorsqu'une connexion est demandée, le serveur créé un thread MultiClient par client qui gère l'attente
	* de messages en provenance du client.
	* 
	* Cette classe reste en attente de nouvelles connexions
	*
	* @param port
  	* 
  	**/
       public static void main(String args[]){ 
		ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java MultiServer <port>\nif using make: make run_server port=<port>");
          System.exit(1);
  	}
	try {
		File historique = new File("./bin/historique.log");

		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port   
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			BufferedReader socIn = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));    
			String pseudo = socIn.readLine();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			MultiClient ct = new MultiClient(clientSocket,pseudo,historique);
			ct.start();
		}
        } catch (Exception e) {
			e.printStackTrace();
        }
      }
  }

  
