package golfgame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Initialization class for each golf ball.
 */
public class golfBall {
	private static int noBalls;
	private int myID;
	
	golfBall() {
		myID=noBalls;
		incID();
	}
	
	public int getID() {
		return myID;		
	}
	
	private static void  incID() {
		noBalls++;
	}
        @Override
        public String toString(){
            return ""+myID;
        }
	
}
