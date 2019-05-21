package concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo {

	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		
		Future<String> submit = newFixedThreadPool.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				System.out.println("handle is working");
				Thread.currentThread().sleep(5000L);
				return "return handle is success";
			}
		});
		
		String string = submit.get();
		System.out.println(string);
		
		newFixedThreadPool.shutdown();
		
	}
}
