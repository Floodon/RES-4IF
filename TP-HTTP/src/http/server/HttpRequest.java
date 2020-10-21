package http.server;

/**
 * Classe representant une requete HTTP
 * 
 */

public class HttpRequest {
    public String requestType;
    public String ressourceRequested;
    public String protocolVersion;
    public String requestHeaderField;
    public String messageBody;
    public String contentType;
    public String contentLength;
    public String body;
    public HttpRequest() {}
}