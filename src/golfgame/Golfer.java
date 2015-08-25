package golfgame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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



    Golfer(BallStash stash,Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag, int bucketSize, int golferNum) {
            sharedStash = stash; //shared 
            sharedField = field; //shared
            cartOnField = cartFlag; //shared
            done = doneFlag;
            golferBucket = new golfBall[bucketSize];
            swingTime = new Random();
            myID=golferNum;
            this.bucketSize=bucketSize;
    }

    @Override
    public void run() {	
        while (done.get()!=true) {	 
            System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+bucketSize+" balls.");
            if(sharedStash.enoughBalls()){
                golferBucket = sharedStash.getBucketBalls(myID, golferBucket);
                System.out.println(Arrays.toString(golferBucket));
            for (int b=0;b<bucketSize;b++)
            { //for every ball in bucket
                if(done.get()== false){
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
                    while(cartOnField.get()==true){//spin this or be smart and use blockingqueue
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
    }	
}
