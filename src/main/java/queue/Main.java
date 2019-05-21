package queue;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {

	public static void main(String[] args) {
		
		ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(2);
		
		for (int i = 0; i < 3; i++) {
			new Thread(new Producer(blockingQueue),"producer"+i).start();
		}
		
		for (int i = 0; i < 3; i++) {
			new Thread(new Consumer(blockingQueue),"consumer"+i).start();
		}
		
	}
	
	
}
