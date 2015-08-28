JFLAGS = -g
JC = javac
JARFILE = junit-4.8.1.jar
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	golfBall.java \
	BallStash.java \
	Range.java \
	Golfer.java \
	Bollie.java \
	VisualGolfer.java \
	VisualBollie.java \
	VisualGame.java \
	DrivingRangeApp.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
