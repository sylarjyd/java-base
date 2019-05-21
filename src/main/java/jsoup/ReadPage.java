package jsoup;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadPage implements Runnable{
		
		private LinkedBlockingQueue<Integer> queue1 = null;
		private LinkedBlockingQueue<String> queue2 = null;
//		http://www.1ppt.com/xiazai/ppt_xiazai_1.html
		private final static String weburl = "http://www.1ppt.com/xiazai/ppt_xiazai_";
		private final static String User_Agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
		public ReadPage(LinkedBlockingQueue<Integer> queue1,LinkedBlockingQueue<String> queue2) {
			this.queue1 = queue1;
			this.queue2 = queue2;
		}
		
		@Override
		public void run() {
			while(queue1.size()>0) {
				try {
					Integer pageNum = queue1.take();
					String url = weburl+pageNum+".html";				
					Document doc = null;
					try {
						 doc = Jsoup.connect(url).userAgent(User_Agent).get();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Elements elements = doc.select("ul").attr("class", "tplist");
					Element element = elements.get(2);
					Elements es = element.select("a[href*=/article/]").html("下载");
					int size = es.size();
					HashSet<String> hashSet = new HashSet<>();
					for (int i = 0; i < size; i++) {
						Element e = es.get(i);
						String url2 = e.attr("href");
						hashSet.add(url2);				
					}
					System.out.println("页数:"+pageNum);
//					hashSet.forEach(e->{										
//						System.out.println(e);
//					});
					queue2.addAll(hashSet);
					System.out.println("已经放入队列:"+queue2.size());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
			}			
		}
		
		public static void main(String[] args) {
			Document doc = null;
			try {
				doc = Jsoup.connect("http://www.1ppt.com/xiazai/ppt_xiazai_1.html").userAgent(User_Agent).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements elements = doc.select("ul").attr("class", "tplist");
//			System.out.println(elements);
			Element element = elements.get(2);
			Elements es = element.select("a[href*=/article/]").html("下载");
			int size = es.size();
			HashSet<String> hashSet = new HashSet<>();
			for (int i = 0; i < size; i++) {
				Element e = es.get(i);
				String url2 = e.attr("href");
				hashSet.add(url2);				
			}
			
			hashSet.forEach(e->{
				System.out.println(e);
			});
			
			
		}
		
	}