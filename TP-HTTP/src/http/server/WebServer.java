///A Simple Web Server (WebServer.java)

package http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Exemple d'un serveur HTTP supportant toutes les m�thodes HTTP
 * 
 * @author Boscher Enzo, Bonhomme Alexandre
 * @version 1.0
 */
public class WebServer {

/**
	 * Lance le serveur sur le port 3000 Entre dans une boucle infinie dans
	 * l'attente d'une connection Lors d'une connection lit les donn�e envoy�es En
	 * focntion du type de requ�te fais appel � la m�thode adapt�e
	 * 
	 * @see GetRequest
	 * @see HeadRequest
	 * @see PutRequest
	 * @see PostRequest
	 * @see DeleteRequest
	 * @see NotImplemented
	 */
	
	protected void start() {
		ServerSocket s;

		System.out.println("Webserver starting up on port 3000");
		try {
			// create the main server socket
			s = new ServerSocket(3000);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}

		System.out.println("Waiting for connection \n");
		for (;;) {
			try {
				// wait for a connection
				Socket remote = s.accept();
				// remote is now the connected socket
				System.out.println("Connection, sending data. \n");
				BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
				BufferedOutputStream out = new BufferedOutputStream(remote.getOutputStream());

				String str = ".";
				String request = "";
				while (str != null && !str.equals("")) {
					str = in.readLine();
					request += str + "\n";
				}
				if (!request.equals("null\n")) {
					// Parse the request
					HttpRequest http = parseRequest(request);
					if (http.contentLength != null) {
						int bodySize = Integer.parseInt(http.contentLength);
						char[] body = new char[bodySize];
						in.read(body, 0, bodySize);
						http.body = new String(body);
					}
					// System.out.println(request);
					if (http.requestType.equals("GET")) {
						GetRequest(http, out);
					}
					if (http.requestType.equals("HEAD")) {
						HeadRequest(http, out);
					}
					if (http.requestType.equals("PUT")) {
						PutRequest(http, out);
					}
					if (http.requestType.equals("POST")) {
						PostRequest(http, out);
					}
					if (http.requestType.equals("DELETE")) {
						DeleteRequest(http, out);
					}
					if (http.requestType.equals("PATCH")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("COPY")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("OPTIONS")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("LINK")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("UNLINK")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("PURGE")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("LOCK")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("UNLOCK")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("PROPFIND")) {
						NotImplemented(http, out);
					}
					if (http.requestType.equals("VIEW")) {
						NotImplemented(http, out);
					}
					if (out != null) {
						out.flush();
					}
					remote.close();
				}
			} catch (Exception e) {
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
		}
	}

/**
	 * M�thode parsant le string requ�te en une instance de la classe HttpRequest
	 * Cr�� et renvoie une instance de la classe HttpRequest
	 * 
	 * @param request la requete HTTP re�ue
	 * @return une instance de la classe HttpRequest correspondant � la requ�te HTTP
	 *         re�ue
	 */

	protected HttpRequest parseRequest(String request) {
		String[] splited = request.split("[ \n]"); // Enleve les espaces et les retours � la ligne
		HttpRequest http = new HttpRequest();
		http.requestType = splited[0];
		http.ressourceRequested = splited[1];
		http.protocolVersion = splited[2];
		for (int i = 0; i < splited.length; i++) {
			if (splited[i].equals("Content-Length:") && i < splited.length - 1) {
				http.contentLength = splited[i + 1];
			}
		}
		return http;
	}

	/**
	 * M�thode renvoyant pour une extension de fichier le type de fichier associ�
	 * Renvoie le type de fichier associ� si l'extension est reconnue Renvoie le
	 * type "application/octet-stream" si l'extension n'est pas reconnue
	 * 
	 * @param extension l'extension du fichier
	 * @return un String d�crivant le type de ressource adapt� au format HTTP
	 */

	protected String contentType(String extension) {
		String type = "";
		if (extension != null) {
			if (extension.equals("htm") || extension.equals("html"))
				type = "text/html";
			else if (extension.equals("mp3"))
				type = "audio/mpeg";
			else if (extension.equals("mp4"))
				type = "vid�o/mp4";
			else if (extension.equals("avi"))
				type = "video/x-msvideo";
			else if (extension.equals("css"))
				type = "text/css";
			else if (extension.equals("csv"))
				type = "text/csv";
			else if (extension.equals("gif"))
				type = "image/gif";
			else if (extension.equals("aac"))
				type = "audio/aac";
			else if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("JPG"))
				type = "image/jpeg";
			else if (extension.equals("json"))
				type = "application/json";
			else if (extension.equals("pdf"))
				type = "application/pdf";
			else if (extension.equals("png"))
				type = "image/png";
			else if (extension.equals("xhtml"))
				type = "application/xhtml+xml";
			else if (extension.equals("xml"))
				type = "application/xml";
			else if (extension.equals("png"))
				type = "image/png";
			else
				type = "application/octet-stream";
		}

		return type;
	}

	/**
	 * M�thode g�rant la m�thode HTTP GET. 
	 * Verifie si le fichier est un executable (app?), si c'est le cas, v�rifie que la requ�te est bien formul�e.
	 * Si c'est le cas execute l'executable avec les param�tres fournis et renvoie le r�sultat dans la r�ponse HTTP avec le code HTTP200
	 * Sinon renvoie un code d'erreur HTTP 400 
	 * Si le fichier n'est pas un executable, essaie d'acc�der au fichier demand� Si le
	 * fichier existe la r�ponse aura pour code HTTP 200 Si le fichier n'existe pas
	 * la r�ponse aura pour code HTTP 404 Si le fichier existe la r�ponse est
	 * constitu�e d'un HEADER et a pour BODY le fichier demand� Si le fichier
	 * n'existe pas la r�ponse est constitu�e d'un HEADER et a pour BODY le fichier
	 * error.html En cas d'erreur essaie de renvoyer le code d'erreur HTTP 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void GetRequest(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		String path = "../doc";
		File file = new File(path + http.ressourceRequested);
		if (http.ressourceRequested.substring(0, 5).equals("/app?")) {
			try {
				System.out.println();
				if(!http.ressourceRequested.contains("&") 
				|| http.ressourceRequested.split("&").length<2 
				|| !http.ressourceRequested.split("&")[0].substring(http.ressourceRequested.split("&")[0].lastIndexOf("=")+1).matches("-?\\d+(\\.\\d+)?") 
				|| !http.ressourceRequested.split("&")[1].substring(http.ressourceRequested.split("&")[1].lastIndexOf("=")+1).matches("-?\\d+(\\.\\d+)?")
				){
					response.code = "400";
					response.codeSignification = "Bad Request";
					response.protocolVersion = http.protocolVersion;
					response.contentType = "text/html";
					out.write(response.stringify().getBytes());
					out.write("<h1>Error 400: Bad Request</h1>".getBytes());
				}else {
					String[] splited = http.ressourceRequested.split("&");
					String[] param = new String[3];
					param[0] = "../lib/Calculatrice.exe";
					param[1] = splited[0].substring(splited[0].lastIndexOf("=") + 1);
					param[2] = splited[1].substring(splited[1].lastIndexOf("=") + 1);
						
					Runtime runtime = Runtime.getRuntime();
					Process process = runtime.exec(param);
					InputStreamReader reader = new InputStreamReader(process.getInputStream());
					BufferedReader br = new BufferedReader(reader);
					String line = "";
					String result = "";
					while ((line = br.readLine()) != null) {
						result += line;
					}

					response.code = "200";
					response.codeSignification = "OK";
					response.protocolVersion = http.protocolVersion;
					response.contentType = "text/html";
					out.write(response.stringify().getBytes());
					out.write(result.getBytes());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (file.exists()) {
				// Ecriture du HEADER
				response.code = "200";
				response.codeSignification = "OK";
			} else {
				file = new File(path + "/error.html");
				// Ecriture du HEADER
				response.code = "404";
				response.codeSignification = "Ressource Not Found";

				System.out.println("The file doesn't exist");
			}
			if (file.getName().lastIndexOf('.') > 0) {
				response.contentType = contentType(file.getName().substring(file.getName().lastIndexOf('.') + 1));
			}
			response.protocolVersion = http.protocolVersion;
			response.contentLength = String.valueOf(file.length());

			try {
				System.out.println(response.stringify());
				out.write(response.stringify().getBytes());
				FileInputStream fileIn = new FileInputStream(file);
				int bufferSize = 256;
				byte[] buffer = new byte[bufferSize];
				int nbRead;
				while ((nbRead = fileIn.read(buffer)) != -1) {
					out.write(buffer, 0, nbRead);
				}
				fileIn.close();
			} catch (IOException e) {
				response.code = "500";
				response.codeSignification = "Internal Error";
				try {
					out.write(response.stringify().getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

	/**
	 * M�thode g�rant la m�thode HTTP HEAD. Essaie d'acc�der au fichier demand� Si
	 * le fichier existe la r�ponse aura pour code HTTP 200 Si le fichier n'existe
	 * pas la r�ponse aura pour code HTTP 404 Renvoie une r�ponse contenant le
	 * HEADER qu'aurait re�u le m�me appel avec la m�thode GET La r�ponse n'a pas de
	 * BODY En cas d'erreur essaie de renvoyer le code d'erreur HTTP 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void HeadRequest(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		String path = "../doc";
		File file = new File(path + http.ressourceRequested);
		if (file.exists()) {
			// Ecriture du HEADER
			response.code = "200";
			response.codeSignification = "OK";
		} else {
			file = new File(path + "/error.html");
			// Ecriture du HEADER
			response.code = "404";
			response.codeSignification = "Ressource Not Found";

			System.out.println("The file doesn't exist");
		}
		if (file.getName().lastIndexOf('.') > 0) {
			response.contentType = contentType(file.getName().substring(file.getName().lastIndexOf('.') + 1));
		}
		response.protocolVersion = http.protocolVersion;
		response.contentLength = String.valueOf(file.length());

		try {
			System.out.println(response.stringify());
			out.write(response.stringify().getBytes());
		} catch (IOException e) {
			response.code = "500";
			response.codeSignification = "Internal Error";
			try {
				out.write(response.stringify().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * M�thode g�rant la m�thode HTTP PUT. Essaie d'ajouter le fichier envoy� Si le
	 * fichier existait d�ja et a �t� �cras�, renvoie le code HTTP 204 Si le fichier
	 * n'existait pas et a �t� cr��, renvoie le code HTTP 201 En cas d'erreur essaie
	 * de renvoyer le code d'erreur HTTP 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void PutRequest(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		String path = "../doc";
		File file = new File(path + http.ressourceRequested);
		if (file.exists()) {
			response.code = "204";
			response.codeSignification = "No Content";
		} else {
			response.code = "201";
			response.codeSignification = "Created";
		}
		response.protocolVersion = http.protocolVersion;
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			fileOut.write(http.body.getBytes());
			fileOut.close();
			out.write(response.stringify().getBytes());
		} catch (IOException e) {
			response.code = "500";
			response.codeSignification = "Internal Error";
			try {
				out.write(response.stringify().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println(response.stringify());
	}

	/**
	 * M�thode g�rant la m�thode HTTP POST. Essaie de trouver le fichier auquel
	 * ajouter des informations Si le fichier est trouv� et que le contenu a bien
	 * �t� ajout� renvoie le code HTTP 200 Si le fichier n'existait pas et a �t�
	 * cr��, renvoie le code HTTP 201 En cas d'erreur essaie de renvoyer le code
	 * d'erreur HTTP 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void PostRequest(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		String path = "../doc";
		File file = new File(path + http.ressourceRequested);
		if (file.exists()) {
			response.code = "200";
			response.codeSignification = "OK";
		} else {
			response.code = "201";
			response.codeSignification = "Created";
		}
		response.protocolVersion = http.protocolVersion;
		try {
			FileOutputStream fileOut = new FileOutputStream(file, file.exists());
			fileOut.write(http.body.getBytes());
			fileOut.close();
			out.write(response.stringify().getBytes());
		} catch (IOException e) {
			response.code = "500";
			response.codeSignification = "Internal Error";
			try {
				out.write(response.stringify().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println(response.stringify());
	}

	/**
	 * M�thode g�rant la m�thode HTTP DELETE. Essaie de trouver le fichier �
	 * supprimer dans le r�pertoire doc et de le supprimer En cas de succ�s renvoie
	 * le code HTTP 200 En cas d'�chec si le fichier n'a pas pu �tre supprim�
	 * renvoie le code d'erreur HTTP 403 En cas d'�chec si le fichier n'a �t� trouv�
	 * renvoie le code d'erreur HTTP 404 En cas d'erreur essaie de renvoyer le code
	 * d'erreur HTTP 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void DeleteRequest(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		String path = "../doc";
		File file = new File(path + http.ressourceRequested);
		if (file.delete()) {
			response.code = "200";
			response.codeSignification = "OK";
		} else if (file.exists()) {
			response.code = "403";
			response.codeSignification = "Forbidden";
		} else {
			response.code = "404";
			response.codeSignification = "Not Found";
		}
		response.protocolVersion = http.protocolVersion;
		try {
			out.write(response.stringify().getBytes());
		} catch (IOException e) {
			response.code = "500";
			response.codeSignification = "Internal Error";
			try {
				out.write(response.stringify().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println(response.stringify());
	}

	/**
	 * M�thode g�rant les m�thodes HTTP non impl�ment�es. Renvoie le code d'erreur
	 * http 501 En cas d'erreur essaie de renvoyer le code d'erreur 500
	 * 
	 * @param http La requ�te http re�ue
	 * @param out  Le flux d'�criture binaire vers le client
	 */

	protected void NotImplemented(HttpRequest http, BufferedOutputStream out) {
		HttpResponse response = new HttpResponse();
		response.code = "501";
		response.codeSignification = "Not Implemented";
		response.protocolVersion = http.protocolVersion;
		try {
			out.write(response.stringify().getBytes());
		} catch (IOException e) {
			response.code = "500";
			response.codeSignification = "Internal Error";
			try {
				out.write(response.stringify().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println(response.stringify());
	}

	/**
	 * Lance l'application.
	 * 
	 * @param args les param�tres sont inutilis�s.
	 */

	public static void main(String args[]) {
		WebServer ws = new WebServer();
		ws.start();
	}
}
