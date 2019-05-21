package concurrent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
 
/**
 *
 * 大屎蛋教程网-dashidan.com
 * HashMap与ConcurrentHashMap的区别于应用场景
 * Created by 大屎蛋 on 2018/5/18.
 */
public class Demo1 {
	
	static volatile CountDownLatch countDownLatch = new CountDownLatch(100);
 
    public static void main(String[] args) throws InterruptedException {
        /** 全局HashMap*/
        HashMap<Integer, Integer> hashMap = new HashMap();
        hashMap.put(0, 0);        
        
        /** 多线程编辑100次*/
        for (int i = 0; i < 100; i++) {
            new Thread(new EditThread(hashMap)).start();
        }
        countDownLatch.await();
        /** 读取线程*/
        new Thread(new ReadThread(hashMap)).start();
        /** 输出最终结果*/
        System.out.println("Demo1 main value " + hashMap.get(0));
    }
    
    static class EditThread implements Runnable {
    	 
        Map<Integer, Integer> hashMap;
     
        public EditThread(Map<Integer, Integer> hashMap) {
            this.hashMap = hashMap;
        }
     
        @Override
        public void run() {
           synchronized (countDownLatch) {
        	   hashMap.put(0, hashMap.get(0) + 1);
               countDownLatch.countDown();
               long count = countDownLatch.getCount();
               System.out.println(count);
        	   
           	}
        }
    }
     
    static class ReadThread implements Runnable {
     
        Map<Integer, Integer> hashMap;
     
        public ReadThread(Map<Integer, Integer> hashMap) {
            this.hashMap = hashMap;
        }
     
        @Override
        public void run() {
            System.out.println("value " + hashMap.get(0));
        }
    }
}
 

