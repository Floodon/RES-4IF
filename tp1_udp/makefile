JC = javac 
JCR = java 
JCOPT = -d
JCROPT = -classpath

default: compil

compil: 
	$(JC) $(JCOPT) classes src/*.java 

run: 
	$(JCR) $(JCROPT) classes ClientUDP $(host) $(port)

clean: 
	$(RM) classes/*.class *~ 