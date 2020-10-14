package http.server;

public class HttpResponse {
    public String protocolVersion;
    public String code;
    public String codeSignification;
    public String body;
    public HttpResponse() {}
    
    public string stringify(){
        return protocolVersion + code + codeSignification + "\n" + body;
    }
}
