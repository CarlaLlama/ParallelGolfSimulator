package golfgame;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BallStash {
    
    private int sizeStash;
    private int sizeBucket;
    private golfBall[] stash;

    public BallStash(int stash, int bucket){
        sizeStash = stash;
        sizeBucket = bucket;
        this.stash= new golfBall[stash];
        for (int i = 0; i < sizeStash; i++) {
            this.stash[i] = new golfBall();
        }
    }

    //will have golfball or null in each position

    public synchronized boolean enoughBalls(){
        int count = 0;
        for (int i = 0; i < sizeStash; i++) {
            if(stash[i]!=null){
                count++;
                if(count==sizeBucket){
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized golfBall[] getBucketBalls(int myID, golfBall[] out){
        for (int i = 0; i < sizeBucket; i++) {
            for (int j = i; j < sizeStash; j++) {
                if(stash[j]!=null && stash[j].isTaken()==false){
                    out[i] = stash[j]; 
                    out[i].setTaken();
                    stash[j] = null;
                    break;
                }                    
            }
        }
        System.out.println("<<< Golfer #"+ myID + " filled bucket with  "+sizeBucket+" balls" + " ("+getBallsInStash()+" balls remaining in stash).");
        System.out.println(Arrays.toString(stash));
        return out;
        
    }

    public void addBallsToStash(golfBall[] g){
        for (int i = 0; i < sizeStash; i++) {
            if(g[i]!=null){
                stash[i] = g[i];
            }
        }
    }

    public int getBallsInStash(){
        int numBalls=0;
        for (golfBall stash1 : stash) {
            if (stash1 != null) {
                numBalls++;
            }
        }
        return numBalls;
    }
    //getters and setters for static variables - you need to edit these
    public void setSizeBucket (int noBalls) {
            sizeBucket=noBalls;
    }
    public int getSizeBucket () {
            return sizeBucket;
    }
    public void setSizeStash (int noBalls) {
            sizeStash=noBalls;
    }
    public int getSizeStash () {
            return sizeStash;
    }
}
