
package golfgame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Text Based UI for Golf Game
 * @author carla
 */
public class VisualGame {
    private AtomicBoolean done;                                //Flag for if range is closing
    private AtomicBoolean cartOnField;                         //Flag for if bollie is on the range
    private int myID;                                          //ID of the current golfer thread
    private golfBall[] golferBucket;                           //Bucket of golfballs for each golfer
    private BallStash sharedStash;                             //Link to shared stash
    private Range sharedField;                                 //Link to shared field
    private int bucketSize;                                    //Size of golfer bucket
    private int columnNumber;
    private int leftOver;
    private int rowNumber;

    VisualGame(BallStash stash,Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag, int bucketSize, int golferNum) {
            sharedStash = stash; //shared 
            sharedField = field; //shared
            cartOnField = cartFlag; //shared
            done = doneFlag;
            golferBucket = new golfBall[bucketSize];
            myID=golferNum;
            this.bucketSize=bucketSize;
            columnNumber = (int) Math.floor(Math.sqrt(bucketSize));
            leftOver = bucketSize - columnNumber*columnNumber;
            rowNumber = columnNumber+1;
    }

    public void menu(){
        System.out.println("********** Welcome to the visual golf game! **********");
        
    }
    
    public void printAll(int numGolfers, int sizeBucket, int SizeStash){
            printGolfers(numGolfers); 
            printBucket(sizeBucket, numGolfers);
            printField(SizeStash);
    }
    
    public void printGolfers(int numGolfers){
        //Display pictures of golfers
        for (int i = 0; i < numGolfers; i++) {
            System.out.print(" _____     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("'    \\\\     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("    O//     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("    \\_\\     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("    | |     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("   /  |     \t");
        }
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("  /   |     \t");
        }  
        System.out.println("");
        for (int i = 0; i < numGolfers; i++) {
            System.out.print("Golfer #"+(i+1)+"\t");
        }
        System.out.println("");
    }
    
    public void printBucket(int numBalls, int numGolfers ){
        for (int j = 0; j < rowNumber; j++) {
            for (int i = 0; i < numGolfers; i++) {      
                System.out.print("|" + repeat('-', columnNumber)+"|"+repeat(' ', numGolfers)+"\t");
            }
            System.out.println("");
        }
        for (int i = 0; i < numGolfers; i++) {
             System.out.print("+"+ repeat('-',columnNumber)+"+"+repeat(' ', numGolfers)+"\t");
            
        }
        System.out.println("");
           
    }
    
    //one line is part of the golfer, the next is part of the bucket
    public synchronized void printGolferAndBucket(int golferID, golfBall[] ballsInBucket){
        
        System.out.println("");
        System.out.print(" _____     \t");
        System.out.println("");
        System.out.print("'    \\\\     \t");
        System.out.println("");
        System.out.print("    O//     \t");
        System.out.println("");
        System.out.print("    \\_\\     \t");
        System.out.println("");
        System.out.print("    | |     \t");
        System.out.println("");
        System.out.print("   /  |     \t");
        System.out.println("");
        System.out.print("Golfer #"+golferID+"\t");
        System.out.println("");
        //Bucket:first row, sometimes never full
        String out = "";
        for (int i = 0; i < leftOver; i++) {
            out+=" ";
        }
        for (int i = 0; i < columnNumber-leftOver; i++) {
            if(ballsInBucket[i]!=null){
                out+=" "+ballsInBucket[i].getID();
            }else{
                out+=" ";
            }
        }
        System.out.println("|" +out+"|");
        int inc =0;
        for (int j = 0; j<rowNumber; j+=2) {
            out="";
            for (int i = 1; i < columnNumber+leftOver; i++) {
                if(ballsInBucket[i+j]!=null){
                    out+=" "+ballsInBucket[i+j].getID();
                }else{
                    out+=" ";
                }
                
            }
            System.out.println("|" +out+"|");
        }
        System.out.print("+"+ repeat('-',columnNumber)+"+");
    }
    
    public void printField(int size){
        System.out.println("              "+sharedField.lineToPrint());
        System.out.println("Golfing range:"+repeat('*',size));
    }
    
    public String repeat(char c, int times){
        StringBuffer b = new StringBuffer();
        for(int i=0;i < times;i++){
            b.append(c);
        }
    return b.toString();
}
    
    public void printBollie(){
        System.out.println("BOLLIE ON THE RANGE...\n");
        System.out.println("          ____|~\\_");
        System.out.println("~~~~~~~~~[    _|_|-]");
        System.out.println("          (_)   (_)");
    }
    
}
