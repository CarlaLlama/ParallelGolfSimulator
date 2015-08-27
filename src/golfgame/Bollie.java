package golfgame;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

    private AtomicBoolean done;                 // flag to indicate when threads should stop
    private AtomicBoolean bollieOnField;        // flag to indicate when bollie is on the range and the balls should stop
    private BallStash sharedStash;              // link to shared stash
    private Range sharedField;                  // link to shared field
    private Random waitTime;
    private Semaphore semaphore;

    /**
     * Bollie constructor
     * @param stash - the ballstash to share
     * @param field - the range to share
     * @param doneFlag - initially set to false
     * @param cartOnField - initially set to false
     */
    Bollie(BallStash stash,Range field,AtomicBoolean doneFlag, AtomicBoolean cartOnField, Semaphore s) {
        sharedStash = stash;
        sharedField = field;
        waitTime = new Random();
        done = doneFlag;
        bollieOnField = cartOnField;
        semaphore = s;
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
                    //semaphore.acquire();
                    System.out.println("*********** Bollie collecting balls   ************");	
                    ballsCollected = sharedField.collectAllBallsFromField();   //collect balls, no golfers allowed to swing while this is happening
                    int howManyBalls = 0;                                      //count how many balls collected
                    for (int i = 0; i < ballsCollected.length; i++) {
                        if(ballsCollected[i]!=null){
                            howManyBalls++;
                        }
                    }
                    System.out.println("*********** Bollie collected "+howManyBalls+" balls ************");	
                    synchronized(this){
                        notifyAll();
                    }
                    sharedStash.addBallsToStash(ballsCollected);
                    //System.out.println("balls in stash:"+sharedStash.getBallsInStash());      
                    sleep(1000);
                    bollieOnField.set(false);
                    for (int i = 0; i < semaphore.getQueueLength(); i++) {
                        semaphore.release();
                    }
                    System.out.println("*********** Bollie adding "+howManyBalls+" balls to stash, "+sharedStash.getBallsInStash()+" balls in stash ************");
                }
                semaphore.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
    }	
}
