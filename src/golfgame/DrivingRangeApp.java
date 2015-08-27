package golfgame;


import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main class for Golf Game.
 * Initializes shared variables, opens range and creates threads.
 */
public class DrivingRangeApp {

	public static void main(String[] args) throws InterruptedException {
            
                //Command inputs for number of golfers, size of the stash and size of golfer's buckets
		int noGolfers = Integer.parseInt(args[0]);
		int sizeStash = Integer.parseInt(args[1]);
		int sizeBucket = Integer.parseInt(args[2]);
		
                //Get UI from user
                Scanner s = new Scanner(System.in);
                System.out.println("Enter [T] to run terminal text game, or [V] to run the visual version!");
                String choice = s.nextLine();
                switch(choice.toLowerCase().charAt(0)){
                    case 't':
                        break;
                    case 'v': 
                        if(noGolfers>=3){
                            System.out.println("Please run this with 3 or golfers, otherwise it is difficult to see output.");
                            System.out.println("Initiating in text mode");
                            break;
                        }else{
                            VisualGame g = new VisualGame();
                        }
                }
                
                //initialize shared variables   
                AtomicBoolean done  = new AtomicBoolean(false);                 //Atomic Boolean to be used as flag for if range is closed or not
                BallStash stash = new BallStash(sizeStash, sizeBucket);         //New ball stash of size specified
                AtomicBoolean cartFlag = new AtomicBoolean(false);              //Atomic Boolean to be used as flag for if bollie is on the field or not
                Range field = new Range(cartFlag, sizeStash);                   //New golfing range
                Semaphore semaphore = new Semaphore(noGolfers);                         //New semaphore to initialize permits for waiting golfer threads
                
                System.out.println("=======   River Club Driving Range Open  ========");
		System.out.println("======= Golfers: "+noGolfers+" balls: "+sizeStash+ " bucketSize: "+sizeBucket+"  ======");
		
		//create golfer threads and set them running
                Golfer[] g = new Golfer[noGolfers];
                for (int i = 0; i < noGolfers; i++) {
                    g[i] = new Golfer(stash,field,cartFlag,done, sizeBucket, i+1, semaphore);
                    g[i].start();
                }
                
                //create bollie thread and set it running
                Bollie bols = new Bollie(stash,field,done, cartFlag, noGolfers, semaphore);
                bols.start();
                
		//Run range for set amount of time
                Random r = new Random();
		Thread.sleep(r.nextInt(10000)+10000);                           //Close driving range after 10 to 20 seconds
		done.set(true);                                                 //Alert threads to the closing of the range 
		System.out.println("=======  River Club Driving Range Closing ========");

	}
        
        public static void runText(){
            
        }
        
        public static void runVisual(){
            
        }

}
