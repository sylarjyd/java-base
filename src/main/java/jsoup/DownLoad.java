package jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownLoad implements Runnable{
		
		private LinkedBlockingQueue<String> queue2 = null;
		private String dir = null;
//		http://www.1ppt.com/article/54053.html
		private static String weburl = "http://www.1ppt.com";
		private final static String User_Agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
		private static Map<String,String> map = null;
		static {
		    map = new HashMap<>();
			map.put("cw_tc", "7b39758815558986302876799eaa391c9b03fc8d2d9cd571ddb505536d7ff2");
			map.put("UM_distinctid", "16a8aaf51a4214-085488f78e0854-36664c08-1fa400-16a8aaf51a5513");
			map.put("bdshare_firstime", "1557104910036");
			map.put("BDTUJIAID", "d686900509635f3d851cbf2a581569e3");
			map.put("CNZZDATA5092133", "cnzz_eid%3D1961729927-1557099657-null%26ntime%3D1557110457");
		}
		
		public DownLoad(LinkedBlockingQueue<String> queue2,String dir) {
			this.queue2 = queue2;
			this.dir = dir;
		}
		
		@Override
		public void run() {
			do {
				try {
					String task = queue2.take();				
					
					String url = weburl+task;
					Document doc = null;
					try {
						 doc = Jsoup.connect(url).cookies(map).userAgent(User_Agent).get();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Elements elements = doc.select("ul").attr("class", "downurllist");
					Element element = elements.get(3);
//					System.out.println(element);
					String downurl = element.select("a[href*=http://ppt.1ppt.com/uploads/soft/]").attr("href");
					System.out.println(downurl);
					String fileName1 = downurl.substring(downurl.lastIndexOf("/")+1);
					Elements elementsByClass = doc.getElementsByClass("ppt_info");
//					System.out.println(elementsByClass);
					String html = elementsByClass.select("h1").html();			
//					System.out.println(html);			
					String fileName2 = fileName1+"_"+html+".zip";
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;
					try {
						URL mainurl = new URL(downurl);
						bis = new BufferedInputStream(mainurl.openStream());
						bos = new BufferedOutputStream(new FileOutputStream(dir+"\\"+fileName2));
						byte[] b = new byte[1024 * 1024];
						int len = 0;
						while((len=bis.read(b))!=-1) {
							bos.write(b, 0, len);
							bos.flush();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
					finally {
						if(bos!=null) {
							try {
								bos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(bis!=null) {
							try {
								bis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
			
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}while(queue2.size()>0);					
		}
		
		public static void main(String[] args) {
			Document doc = null;
			try {				
				doc = Jsoup.connect("http://www.1ppt.com/article/52717.html").cookies(map).userAgent(User_Agent).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File("D:\\sylar\\tmp\\ppt\\ppt");
			if(!file.exists()) {
				file.mkdirs();
			}
			Elements elements = doc.select("ul").attr("class", "downurllist");
			Element element = elements.get(3);
			System.out.println(element);
			String downurl = element.select("a[href*=http://ppt.1ppt.com/uploads/soft/]").attr("href");
			System.out.println(downurl);
			String fileName1 = downurl.substring(downurl.lastIndexOf("/")+1);
			Elements elementsByClass = doc.getElementsByClass("ppt_info");
			System.out.println(elementsByClass);
			String html = elementsByClass.select("h1").html();			
			System.out.println(html);			
			String fileName2 = fileName1+"_"+html+".zip";
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				URL url = new URL(downurl);
				bis = new BufferedInputStream(url.openStream());
				bos = new BufferedOutputStream(new FileOutputStream(file+"\\"+fileName2));
				byte[] b = new byte[1024 * 1024];
				int len = 0;
				while((len=bis.read(b))!=-1) {
					bos.write(b, 0, len);
					bos.flush();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			finally {
				if(bos!=null) {
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(bis!=null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	
		}
		
	}