package golfgame;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range {
	private static int sizeStash;
	private AtomicBoolean cartOnField;
        private golfBall[] ballsOnField;
        private int cnt;
	//Add constructors

    public Range(AtomicBoolean cartOnField, int sizeStash) {
        this.sizeStash=sizeStash;
        this.cartOnField = cartOnField;
        this.ballsOnField = new golfBall[sizeStash];
    }
        
	
    public golfBall[] collectAllBallsFromField(){
        golfBall[] collected = new golfBall[sizeStash];
        for (int i = 0; i < sizeStash; i++) {
            if(ballsOnField[i]!=null){
                collected[i] = ballsOnField[i];
                ballsOnField[i] = null;
            }
        }
        return collected;
    }
    

    public synchronized void hitBallOntoField(golfBall ball){
        ball.setNotTaken();
        ballsOnField[ball.getID()] = ball;
    }
}
