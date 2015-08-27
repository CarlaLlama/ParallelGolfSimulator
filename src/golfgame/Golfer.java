package golfgame;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initialization class for each golf ball.
 * Initializes each  with a taken flag (whether in a user bucket (true) or on the field or in the stash (false))
 */
public class Golfer extends Thread {
    private AtomicBoolean done;                                //Flag for if range is closing
    private AtomicBoolean cartOnField;                         //Flag for if bollie is on the range
    private int myID;                                          //ID of the current golfer thread
    private golfBall[] golferBucket;                           //Bucket of golfballs for each golfer
    private BallStash sharedStash;                             //Link to shared stash
    private Range sharedField;                                 //Link to shared field
    private Random swingTime;                                  //Random time for each golfer to swing
    private int bucketSize;                                    //Size of golfer bucket
    private Semaphore semaphore;                               //Initialise permitting

    

    Golfer(BallStash stash,Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag, int bucketSize, int golferNum, Semaphore sem) {
            sharedStash = stash; //shared 
            sharedField = field; //shared
            cartOnField = cartFlag; //shared
            done = doneFlag;
            golferBucket = new golfBall[bucketSize];
            swingTime = new Random();
            myID=golferNum;
            this.bucketSize=bucketSize;
            semaphore=sem;
    }

    @Override
    public void run() {	
        while (done.get()!=true) {                                              //while the driving range is not closed
            if(cartOnField.get()!=true){                                        //
                System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+bucketSize+" balls.");
                golferBucket = sharedStash.getBucketBalls(myID, golferBucket);  //fill or refill bucket of balls
                for (int b=0;b<bucketSize;b++){                                 //for every ball in bucket
                    try {
                        sleep(swingTime.nextInt(2000));                         //golfer takes 0 - 2 seconds to swing
                        sharedField.hitBallOntoField(golferBucket[b]);          //put that ball onto the field
                        System.out.println("Golfer #"+ myID + " hit ball #"+golferBucket[b].getID()+" onto field");	
                    } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                  if(cartOnField.get()==true){ try {
                      semaphore.acquire();                                      //Golfer tries to acquire a permit, must wait for bollie
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      semaphore.release();                                      //Release immediately after it is acquired
                    }
                }   
            }
        }
        System.out.println(">>> Golfer #"+ myID + " returned with empty bucket.");
    }	
}
