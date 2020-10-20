package http.server;

/**
 * Classe représentant le HEADER d'une réponse HTTP
 * 
 */

public class HttpResponse {
    public String protocolVersion;
    public String code;
    public String codeSignification;
    public String body;
    public String contentLength;
    public String contentType;
    public String server;
    public String date;
    public HttpResponse() {
    	server = "LocalHost";
    }
	/**
	 * Méthode renvoyant un string correspondant au HEADER de la réponse HTTP
	 * Renvoie le string contenant les informations du HEADER de la réponse HTTP
	 * 
	 * @return Un string représentant le HEADER de la réponse HTTP
	 */
    public String stringify(){
    	String res ="";
    	res += protocolVersion + " " + code + " " + codeSignification + "\n";
    	res += "Server : " + server +"\n";
    	if(contentLength != null) {
        	res += "Content-Length :" + contentLength +"\n";
    	}
    	if(contentType != null) {
    		res += "Content-Type : " + contentType + "\n";
    	}
    	res += "\n"; //FIN DU HEADER
    	return res;
    }
}
