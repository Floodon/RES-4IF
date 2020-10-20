package http.server;

/**
 * Classe repr�sentant le HEADER d'une r�ponse HTTP
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
	 * M�thode renvoyant un string correspondant au HEADER de la r�ponse HTTP
	 * Renvoie le string contenant les informations du HEADER de la r�ponse HTTP
	 * 
	 * @return Un string repr�sentant le HEADER de la r�ponse HTTP
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
