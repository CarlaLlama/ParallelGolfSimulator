package golfgame;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * BallStash class for initializing the stash of golf balls for each golf game.
 * Contains an array of golfBalls, that is the size of the stash specified.
 */
public class BallStash {
    
    private int sizeStash;
    private int sizeBucket;
    private golfBall[] stash;

    /**
     * BallStash constructor
     * Initializes an array of new golfballs, henceforth each golfball's ID will relate to its position in the stash
     * @param stash - size of stash, becomes size of new array of golfballs
     * @param bucket - size of bucket
     */
    public BallStash(int stash, int bucket){
        sizeStash = stash;
        sizeBucket = bucket;
        this.stash= new golfBall[stash];
        for (int i = 0; i < sizeStash; i++) {
            this.stash[i] = new golfBall();
        }
    }

    /**
     * Calculate whether the stash contains enough balls to fill a golfer's bucket
     * @return true if has enough balls
     */
    public synchronized boolean enoughBalls(){
        int count = 0;
        for (int i = 0; i < sizeStash; i++) {
            if(stash[i]!=null){                 //If a position is not null, it must contain a golfball
                count++;
                if(count==sizeBucket){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Fill a bucket of balls from the stash, remove these balls from the stash
     * @param myID - id of the golfer whose bucket is being filled (for printing out)
     * @param out - an empty array of golfBalls to put them in - the bucket
     * @return the bucket of balls
     */
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
        //System.out.println(Arrays.toString(stash));
        return out;
        
    }

    /**
     * Add array of collected balls to the stash
     * @param g - array of collected balls
     */
    public void addBallsToStash(golfBall[] g){
        for (int i = 0; i < sizeStash; i++) {
            if(g[i]!=null){
                stash[i] = g[i];
            }
        }
    }

    /**
     * Calculate the number of balls currently in the stash
     * @return - number of balls in stash
     */
    public int getBallsInStash(){
        int numBalls=0;
        for (golfBall stash1 : stash) {
            if (stash1 != null) {
                numBalls++;
            }
        }
        return numBalls;
    }
}
