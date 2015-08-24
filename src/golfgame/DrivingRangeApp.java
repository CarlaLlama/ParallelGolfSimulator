package golfgame;


import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp {


	public static void main(String[] args) throws InterruptedException {
		AtomicBoolean done  =new AtomicBoolean(false);

                Scanner s = new Scanner(System.in);
                //change this to cmd input
                System.out.println("Please enter the number of golfer threads:");                
		int noGolfers = Integer.parseInt(s.nextLine());
                System.out.println("Please enter the initial size of the supply stash:");
		int sizeStash= Integer.parseInt(s.nextLine());
                System.out.println("Please enter the number of balls per bucket:");
		int sizeBucket=Integer.parseInt(s.nextLine());
                
		
		//initialize shared variables
                
                BallStash stash = new BallStash(sizeStash, sizeBucket);
                AtomicBoolean cartFlag = new AtomicBoolean(false);
                
                Range field = new Range(cartFlag, sizeStash);
                
                System.out.println("=======   River Club Driving Range Open  ========");
		System.out.println("======= Golfers: "+noGolfers+" balls: "+sizeStash+ " bucketSize: "+sizeBucket+"  ======");
		
		//create threads and set them running
                Golfer[] g = new Golfer[noGolfers];
                for (int i = 0; i < noGolfers; i++) {
                    g[i] = new Golfer(stash,field,cartFlag,done, sizeBucket, i+1);
                    g[i].start();
                }
                
                Bollie bols = new Bollie(stash,field,done, cartFlag);
                bols.start();
		//for testing, just run for a bit
		Thread.sleep(10000);
		done.set(true);
		System.out.println("=======  River Club Driving Range Closing ========");

		
	}

}
