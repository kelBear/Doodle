JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
	
CLASSES = \
		BotPanel.java \
		ColourButton.java \
		ColourPanel.java \
		Doodle.java \
		DrawPanel.java \
		DrawStroke.java \
		LineButton.java \
		LinePanel.java \
		Model.java \
		TimedCoordinate.java \
		View.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
