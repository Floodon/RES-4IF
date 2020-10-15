/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;


import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MultiServer {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
       public static void main(String args[]){ 
		ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		//Créé fichier de persistence du chat s'il n'existe pas
		//BufferedWriter est synchronized !
		BufferedWriter file_writer = new BufferedWriter(new FileWriter("./bin/historique.log"));
      	Scanner file_reader = new Scanner(new File("./bin/historique.log"));

		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port   
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			BufferedReader socIn = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));    
			String pseudo = socIn.readLine();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			MultiClient ct = new MultiClient(clientSocket,pseudo,file_writer,file_reader);
			ct.start();
		}
        } catch (Exception e) {
			//fos.flush();
			//fos.close();
        }
      }
  }

  
