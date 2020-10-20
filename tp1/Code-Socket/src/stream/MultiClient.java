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
	
	MultiClient(Socket s, String pseudo, File histo) {
		this.clientSocket = s;
		this.pseudo = pseudo;
		try{
			this.file_writer = new BufferedWriter(new FileWriter(histo, true));
			this.file_reader = new Scanner(histo);
		} catch(Exception e) { 
			e.printStackTrace();
		}
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
				socOut.println(data);
			  }

			//Affiche à tous le monde la connexion d'un nouvel utilisateur
			to_send = pseudo+" s'est connecté !";
				// Sauvegarde le message
				sauvegarder(to_send);
			for(int i=0;i<liste_clients.size();i++) {
				liste_clients.get(i).println(to_send);
				
			  }

    		while (true) {
				// réception du message
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
				// Envoi du message à tous les clients
				to_send = pseudo+": "+line;
				// Sauvegarde le message
				sauvegarder(to_send);

				for(int i=0;i<liste_clients.size();i++) {
					liste_clients.get(i).println(to_send);
			  	}
			}
			file_writer.close();
			file_reader.close();
    	} catch (Exception e) {
			e.printStackTrace();
        }
	   }
	   
	   public void sauvegarder(String to_save) {
		try {
			to_save += "\n";
			file_writer.append(to_save);
			file_writer.flush();
		} catch (Exception e) {
			System.out.println("couldn't save your message");
			e.printStackTrace();
		}
	   }
  }

  
