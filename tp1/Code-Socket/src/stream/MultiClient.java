/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.util.*;
import java.io.*;
import java.net.*;

public class MultiClient
	extends Thread {
	public static List<PrintStream> liste_clients = Collections.synchronizedList(new ArrayList<PrintStream>());
	private Socket clientSocket;
	private String pseudo;
	private BufferedWriter file_writer;
	private Scanner file_reader;
	
	MultiClient(Socket s, String pseudo, BufferedWriter file_writer, Scanner file_reader) {
		this.clientSocket = s;
		this.pseudo = pseudo;
		this.file_writer = file_writer;
		this.file_reader = file_reader;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {

			String to_send;
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
			liste_clients.add(socOut);

			//Restitue l'historique du chat
			while (file_reader.hasNextLine()) {
				String data = file_reader.nextLine();
				System.out.println(data);
			  }

			//Affiche à tous le monde la connexion d'un nouvel utilisateur
			to_send = pseudo+" s'est connecté !";
				// Sauvegarde le message
				sauvegarder(to_send);
			for(int i=0;i<liste_clients.size();i++) {
				liste_clients.get(i).println(to_send);
				
			  }

    		while (true) {
				  String line = socIn.readLine();
					//Affiche à tous le monde la déconnexion d'un utilisateur
				  if(line == null) {
						to_send = pseudo+" s'est déconnecté !";
						// Sauvegarde le message
						sauvegarder(to_send);
					  for(int i=0;i<liste_clients.size();i++) {
						liste_clients.get(i).println(to_send);
					  }
					  break;
				  }
				to_send = pseudo+": "+line;
				// Sauvegarde le message
				sauvegarder(to_send);

				for(int i=0;i<liste_clients.size();i++) {
					liste_clients.get(i).println(to_send);
			  	}
    		}
    	} catch (Exception e) {
        }
	   }
	   
	   public void sauvegarder(String to_save) {
		try {
			to_save += "\n";
			file_writer.write(to_save);
		} catch (Exception e) {
			System.out.println("couldn't save your message");
		}
	   }
  }

  
