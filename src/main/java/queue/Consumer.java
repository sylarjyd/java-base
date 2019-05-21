package queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
	private BlockingQueue<String> blockingQueue = null;
	static Random random = new Random();
	
	public Consumer(BlockingQueue<String> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String name = Thread.currentThread().getName();
		try {
			String task = blockingQueue.take();
			System.out.println(name+" consumer consume a task :"+task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
