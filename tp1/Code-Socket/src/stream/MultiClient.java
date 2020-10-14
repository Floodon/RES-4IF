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
	
	MultiClient(Socket s) {
		this.clientSocket = s;
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
			System.out.println(liste_clients.size());

    		while (true) {
				  String line = socIn.readLine();
				for(int i=0;i<liste_clients.size();i++) {
					liste_clients.get(i).println(line);
					
			  	}
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  }

  
