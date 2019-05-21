package concurrent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * Created by bj on 2018/5/18.
 */
public class Demo2 {
 
    public static void main(String[] args) {
        /** 全局ConcurrentHashMap*/
        ConcurrentHashMap<Integer, Integer> hashMap = new ConcurrentHashMap();
        hashMap.put(0, 0);
 
        /** 多线程编辑100次*/
        for (int i = 0; i < 100; i++) {
            new Thread(new EditThread(hashMap)).start();
        }
 
        /** 读取线程*/
        new Thread(new ReadThread(hashMap)).start();
        /** 输出最终结果*/
        System.out.println("Demo2 main value " + hashMap.get(0));
    }
    
    
    static class EditThread implements Runnable {
   	 
        Map<Integer, Integer> hashMap;
     
        public EditThread(Map<Integer, Integer> hashMap) {
            this.hashMap = hashMap;
        }
     
        @Override
        public void run() {
            synchronized (hashMap) {
            	hashMap.put(0, hashMap.get(0) + 1);
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


