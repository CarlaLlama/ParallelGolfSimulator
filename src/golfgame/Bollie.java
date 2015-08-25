package golfgame;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

    private AtomicBoolean done;                 // flag to indicate when threads should stop
    private AtomicBoolean bollieOnField;        // flag to indicate when bollie is on the range and the balls should stop
    private BallStash sharedStash;              // link to shared stash
    private Range sharedField;                  // link to shared field
    private Random waitTime;

    /**
     * Bollie constructor
     * @param stash - the ballstash to share
     * @param field - the range to share
     * @param doneFlag - initially set to false
     * @param cartOnField - initially set to false
     */
    Bollie(BallStash stash,Range field,AtomicBoolean doneFlag, AtomicBoolean cartOnField) {
        sharedStash = stash;
        sharedField = field;
        waitTime = new Random();
        done = doneFlag;
        bollieOnField = cartOnField;
    }

    /**
     * Run the bollie thread
     */
    @Override
    public void run() {
        golfBall [] ballsCollected;             //Array to hold the balls bollie collects
        while (done.get()!=true) {
            try {
                sleep(waitTime.nextInt(5000)+2000);             //Sleep for 2-7 seconds
                if(done.get()!=true){                           //Recheck condition for closing, to prevent more balls from being hit
                    bollieOnField.set(true);
                    System.out.println("*********** Bollie collecting balls   ************");	
                    ballsCollected = sharedField.collectAllBallsFromField();   //collect balls, no golfers allowed to swing while this is happening
                    int howManyBalls = 0;                                      //count how many balls collected
                    for (int i = 0; i < ballsCollected.length; i++) {
                        if(ballsCollected[i]!=null){
                            howManyBalls++;
                        }
                    }
                    System.out.println("*********** Bollie collected "+howManyBalls+" balls, adding them to stash ************");	
                    synchronized(this){
                        notifyAll();
                    }
                    bollieOnField.set(false);
                    sleep(1000);
                    sharedStash.addBallsToStash(ballsCollected);
                    System.out.println("balls in stash:"+sharedStash.getBallsInStash());                        
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
    }	
}
