package golfgame;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

    private AtomicBoolean done;  // flag to indicate when threads should stop
    private AtomicBoolean bollieOnField;
    private BallStash sharedStash; //link to shared stash
    private Range sharedField; //link to shared field
    private Random waitTime;

    //link to shared field
    Bollie(BallStash stash,Range field,AtomicBoolean doneFlag, AtomicBoolean cartOnField) {
        sharedStash = stash; //shared 
        sharedField = field; //shared
        waitTime = new Random();
        done = doneFlag;
        bollieOnField = cartOnField;
    }

    @Override
    public void run() {
        //while True
        golfBall [] ballsCollected = new golfBall[sharedStash.getSizeStash()];
        while (done.get()!=true) {
            try {
                sleep(waitTime.nextInt(5000)+2000);
                if(done.get()!=true){
                    bollieOnField.set(true);
                    System.out.println("*********** Bollie collecting balls   ************");	
                    ballsCollected = sharedField.collectAllBallsFromField();
                    //collect balls, no golfers allowed to swing while this is happening
                    int howManyBalls = 0;
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
