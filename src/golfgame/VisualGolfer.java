package golfgame;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisualGolfer extends Thread {
    private AtomicBoolean done;                                //Flag for if range is closing
    private AtomicBoolean cartOnField;                         //Flag for if bollie is on the range
    private int myID;                                          //ID of the current golfer thread
    private golfBall[] golferBucket;                           //Bucket of golfballs for each golfer
    private BallStash sharedStash;                             //Link to shared stash
    private Range sharedField;                                 //Link to shared field
    private int bucketSize;                                    //Size of golfer bucket
    private Semaphore semaphore;                               //Initialise permitting
    private VisualGame v;

    VisualGolfer(BallStash stash,Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag, int bucketSize, int golferNum, Semaphore sem, VisualGame v) {
            sharedStash = stash; //shared 
            sharedField = field; //shared
            cartOnField = cartFlag; //shared
            done = doneFlag;
            golferBucket = new golfBall[bucketSize];
            myID=golferNum;
            this.bucketSize=bucketSize;
            semaphore=sem;
            this.v=v;
    }

    @Override
    public void run() {	
        Random swingTime = new Random();
        while (done.get()!=true) {                                              //while the driving range is not closed
            if(cartOnField.get()!=true){                                        //while Bollie is not on the the field
                System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+bucketSize+" balls.");
                golferBucket = sharedStash.getBucketBalls(myID, golferBucket, done);  //fill or refill bucket of balls
                for (int b=0;b<bucketSize;b++){                                 //for every ball in bucket
                    try {
                        sleep(swingTime.nextInt(2000));                         //golfer takes 0 - 2 seconds to swing
                        sharedField.hitBallOntoField(golferBucket[b]);          //put that ball onto the field
                        if(golferBucket[b]!=null){
                            System.out.println("Golfer #"+ myID + " hit ball #"+golferBucket[b].getID()+" onto field");
                        }
                        	
                    } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                    
                    
                    try {
                        sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VisualGolfer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  if(cartOnField.get()==true){ try {
                      semaphore.acquire();                                      //Golfer tries to acquire a permit, must wait for bollie
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      semaphore.release();                                      //Release immediately after it is acquired
                    }
                  v.printGolferAndBucket(myID, golferBucket);
                  v.printField(sharedStash.size());
                }   
                
                
            }
        }
        System.out.println(">>> Golfer #"+ myID + " returned with empty bucket.");
    }	
}
