package stream;
import java.io.*;
import java.net.*;

public class ClientListener
	extends Thread {
	private Socket clientSocket;
	
	ClientListener(Socket s) {
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
            
    		while (true) {
				String line = socIn.readLine();
				System.out.println(line);
				
    		}
    	} catch (Exception e) {; 
        }
       }
  }
