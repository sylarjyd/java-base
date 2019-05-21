package jsoup;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 爬取ppt
 * @author jyd
 *
 */
public class JsoupSpider2 {

	
	public static void main(String[] args) {
		
		LinkedBlockingQueue<Integer> queue1 = new LinkedBlockingQueue<>();
		for (int i = 1; i < 106; i++) {
			try {
				queue1.put(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
		LinkedBlockingQueue<String> queue2 = new LinkedBlockingQueue<>();
				
		ExecutorService newFixedThreadPool1 = Executors.newFixedThreadPool(5);
		ExecutorService newFixedThreadPool2 = Executors.newFixedThreadPool(3);

		String dir = "D:\\sylar\\tmp\\ppt\\ppt";
		
		File file = new File(dir);
		if(!file.exists()) {
			file.mkdirs();
		}
//		ReadPage readPage = new ReadPage(queue1, queue2);
		for (int i = 1; i < 6; i++) {
			ReadPage readPage = new ReadPage(queue1, queue2);
			newFixedThreadPool1.submit(new Thread(readPage));
		}	
//		DownLoad downLoad = new DownLoad(queue2,dir);
		for (int i = 1; i < 4; i++) {
			DownLoad downLoad = new DownLoad(queue2,dir);
			newFixedThreadPool2.submit(new Thread(downLoad));
		}
	}
	
	

}