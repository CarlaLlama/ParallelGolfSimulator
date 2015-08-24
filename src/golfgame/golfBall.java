package golfgame;

import java.util.concurrent.atomic.AtomicBoolean;


public class golfBall {
	private AtomicBoolean taken = new AtomicBoolean();
	private static int noBalls;
	private int myID;
	
	golfBall() {
		myID=noBalls;
		incID();
	}
	
	public int getID() {
		return myID;		
	}
        
        public boolean isTaken(){
            return taken.get();
        }
        
        public void setTaken(){
            taken.set(true);
        }
        
        public void setNotTaken(){
            taken.set(false);
        }
	
	private static void  incID() {
		noBalls++;
	}
        public String toString(){
            return ""+myID;
        }
	
}
