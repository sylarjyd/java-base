package queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{

	private BlockingQueue<String> blockingQueue = null;
	static Random random = new Random();
	
	public Producer(BlockingQueue<String> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		try {
			Thread.currentThread().sleep(random.nextInt(10));
			blockingQueue.put(name);	
			System.out.println(name+" producer produce a task");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
