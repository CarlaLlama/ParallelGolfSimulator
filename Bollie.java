import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Bollie class for initializing the bollie thread run method.
 * Initializes bollie, stops golfer threads, collects balls and releases golfer threads when done
 */
public class Bollie extends Thread{

    private AtomicBoolean done;                 // flag to indicate when threads should stop
    private AtomicBoolean bollieOnField;        // flag to indicate when bollie is on the range and the balls should stop
    private BallStash sharedStash;              // link to shared stash
    private Range sharedField;                  // link to shared field
    private Random waitTime;                    // random time to wake bollie up
    private Semaphore semaphore;
    private int noGolfers;

    /**
     * Bollie constructor
     * @param stash - the ballstash to share
     * @param field - the range to share
     * @param doneFlag - initially set to false
     * @param cartOnField - initially set to false
     * @param s - semaphore that golfers have acquired while bollie is on field
     */
    Bollie(BallStash stash,Range field,AtomicBoolean doneFlag, AtomicBoolean cartOnField, int noGolfers, Semaphore s) {
        sharedStash = stash;
        sharedField = field;
        waitTime = new Random();
        done = doneFlag;
        bollieOnField = cartOnField;
        this.noGolfers = noGolfers;
        semaphore = s;
    }

    /**
     * Run the bollie thread
     */
    @Override
    public void run() {
        golfBall [] ballsCollected;                                             //Array to hold the balls bollie collects
        while (done.get()!=true) {                                              //Check that Range has not closed
            try {
                sleep(waitTime.nextInt(5000)+2000);                             //Sleep for 2-7 seconds
                if(done.get()!=true){                                           //Recheck condition for closing, just in case!
                    bollieOnField.set(true);                                    //Set Atomic boolean to true
                    System.out.println("*********** Bollie collecting balls   ************");	
                    ballsCollected = sharedField.collectAllBallsFromField();    //Collect balls, no golfers allowed to swing while this is happening
                    
                    semaphore.drainPermits();                                   //drain permits so that golfers must wait
                    System.out.println("*********** Bollie collected "+ballsCollected.length+" balls ************");	

                    sleep(1000);                                                //Simulate collecting and adding 
                    sharedStash.addBallsToStash(ballsCollected);                //Add collected balls to stash
                    semaphore.release(noGolfers);                               //Release semaphore and all waiting threads, so that golfers waiting can continue to their next swing
                    bollieOnField.set(false);                                   //Set Atomic boolean to false, condition no longer blocked
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
        sharedStash.golfersGo();
    }	
}
