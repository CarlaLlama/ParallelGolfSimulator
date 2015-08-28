package golfgame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Initialization class for the driving range.
 * Initializes shared range with a cartFlag for bollie, and a collector array for the balls that have been hit onto the field.
 */
public class Range {
	private static int sizeStash;
	private AtomicBoolean cartOnField;
        private golfBall[] ballsOnField;

    public Range(AtomicBoolean cartOnField, int sizeStash) {
        this.sizeStash=sizeStash;
        this.cartOnField = cartOnField;
        this.ballsOnField = new golfBall[sizeStash];
    }
	
    /**
     * Make an array containing all the balls (and no nulls) that exist in the field array
     * @return array of balls.
     */
    public synchronized golfBall[] collectAllBallsFromField(){
            int thisMany = howManyBallsOnField();
            golfBall[] collected = new golfBall[thisMany];
            for (int i = 0; i < thisMany; i++) {
                for (int j = i; j < sizeStash; j++) {
                    if(ballsOnField[j]!=null){
                        collected[i] = ballsOnField[j];
                        ballsOnField[j] = null;
                        break;
                    }
                }
            }
            return collected;
    }
    
    /**
     * Count the number of golf balls in the array
     * @return number
     */
    public synchronized int howManyBallsOnField(){
        int cnt=0;
        for (int i = 0; i < sizeStash; i++) {
            if(ballsOnField[i]!=null){
                cnt++;
            }
        }
        return cnt;
    }
    
    /**
     * Add ball to the field array in its respective position.
     * @param ball to hit onto field.
     */
    public synchronized void hitBallOntoField(golfBall ball){
            ballsOnField[ball.getID()] = ball;
    }
    
    //VISUAL METHODS
    //Print line representing the field, with all the balls
    public synchronized String lineToPrint(){
        String out = "";
        for(int i = 0; i < sizeStash; i ++){
            if(ballsOnField[i]==null){
                out+=" ";
            }else{
                out+=ballsOnField[i].getID();
            }
        }
        return out;
    }
}
