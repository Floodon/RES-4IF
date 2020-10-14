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
	
	MultiClient(Socket s, String pseudo) {
		this.clientSocket = s;
		this.pseudo = pseudo;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {


    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
			liste_clients.add(socOut);

			//Affiche à tous le monde la connexion d'un nouvel utilisateur
			for(int i=0;i<liste_clients.size();i++) {
				liste_clients.get(i).println(pseudo+" s'est connecté !");
				
			  }

    		while (true) {
				  String line = socIn.readLine();
				  if(line == null) {
					  for(int i=0;i<liste_clients.size();i++) {
						liste_clients.get(i).println(pseudo+" s'est déconnecté !");
					  }
					  break;
				  }
				for(int i=0;i<liste_clients.size();i++) {
					liste_clients.get(i).println(pseudo+":"+line);
					
			  	}
    		}
    	} catch (Exception e) {
        }
       }
  }

  
