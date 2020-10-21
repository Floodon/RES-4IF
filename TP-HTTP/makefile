JC = javac 
JCR = java 
JCOPT = -d
JCROPT = -classpath

default: compil

compil: 
	$(JC) $(JCOPT) bin/http/server/ src/http/server/*.java 

run: 
	$(JCR) $(JCROPT) bin http.server.WebServer

clean: 
	$(RM) bin/http/server.class *~ 
