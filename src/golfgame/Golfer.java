package golfgame;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Golfer extends Thread {

    //remember to ensure thread saftey

    private AtomicBoolean done; 
    private AtomicBoolean cartOnField;

    private int noGolfers; //shared amoungst threads

    private int myID;

    private golfBall  [] golferBucket;
    private BallStash sharedStash; //link to shared stash
    private Range sharedField; //link to shared field
    private Random swingTime;
    private int bucketSize;
    private Semaphore semaphore;

    

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
        while (done.get()!=true) {	 
            System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+bucketSize+" balls.");
            if(sharedStash.enoughBalls()){
                golferBucket = sharedStash.getBucketBalls(myID, golferBucket);
                System.out.println(Arrays.toString(golferBucket));
                for (int b=0;b<bucketSize;b++){ //for every ball in bucket
                    try {
                        sleep(swingTime.nextInt(2000));
                        //if(cartOnField.get()!=true){
                            sharedField.hitBallOntoField(golferBucket[b]);
                        System.out.println("Golfer #"+ myID + " hit ball #"+golferBucket[b].getID()+" onto field");	

                        //}
                    } catch (InterruptedException e) {
                                e.printStackTrace();
                        } //      swing
                    //!!wait for cart if necessary if cart there
                  while(cartOnField.get()==true){
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }else{
                try {//wait for stash to be refilled
                    synchronized(this){
                       this.wait(); 
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Golfer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }     
        }
        System.out.println(">>> Golfer #"+ myID + " returned with empty bucket.");
    }	
}
