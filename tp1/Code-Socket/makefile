JC = javac 
JCR = java 
JCOPT = -d
JCROPT = -classpath

default: compil

compil: 
	$(JC) $(JCOPT) ./classes src/*/*.java 

run_server: 
	$(JCR) $(JCROPT) classes stream.MultiServer $(port)

run_client:
	$(JCR) $(JCROPT) classes stream.EchoClient $(host) $(port)

clean: 
	$(RM) classes/*/*.class *~ 