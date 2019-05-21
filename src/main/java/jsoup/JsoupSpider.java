package jsoup;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 爬取图片 
 * @author jyd
 *
 */
public class JsoupSpider implements Runnable{
	
	private static Integer count = 0;
	private Integer start = 0;
	private Integer end = 0;
	
	
	
	public JsoupSpider() {
		super();
	}

	public JsoupSpider(Integer start, Integer end) {
		super();
		this.start = start;
		this.end = end;
	}

	// http://www.nipic.com/photo/jingguan/shanshui/index.html?page= 后面的数字逐个加1
	// 直到2010
	public static String getAllURL(String url) {
		StringBuffer sb = null;
		try {
			sb = new StringBuffer();
			for (int i = 1; i <= 2010; i++) {
				sb.append(url + i).append("\r\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
 
	// 在大图的网址获取详细的图片地址
	public static String getPicURL(String picHrefUrl) {
		String picURL = null;
		if (picHrefUrl != null) {
			try {
				Document doc = Jsoup.connect(picHrefUrl).get();
				Element element = doc.getElementById("J_worksImg");
				picURL = element.attr("src");
			} catch (Exception e) {
				new RuntimeException(" ");
			}
		}
		return picURL;
	}
 
	// 下载图片到本地磁盘
	public static void downloadPicToLocal(String picSourceURL, String picDestPath) {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			URL url = new URL(picSourceURL);
			bis = new BufferedInputStream(url.openStream());
			bos = new BufferedOutputStream(new FileOutputStream(picDestPath));
			byte[] b = new byte[1024 * 1024];
			int len = 0;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
				bos.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			new RuntimeException(" ");
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
 
	// 获取文件后缀名
	public static String getNameExtension(String picURL) {
		String extension = null;
 
		try {
			int lastIndexOf = 0;
			if (picURL != null && !picURL.equals("")) {
				if (picURL.endsWith(".jpg"))
					lastIndexOf = picURL.lastIndexOf(".jpg");
				if (picURL.endsWith(".png"))
					lastIndexOf = picURL.lastIndexOf(".png");
				extension = picURL.substring(lastIndexOf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extension;
	}
 
	// 通过Jsoup获取网页的单个图片地址_通过传入网页地址和生成图片目录
	// 目标网址为http://www.nipic.com/photo/jingguan/shanshui/index.html?page=1
	public static boolean getOnePic(String webURL, String picDestURL,int start,int end) {
		boolean flag = true;
		try {
			// webURL="http://www.nipic.com/photo/jingguan/shanshui/index.html?page="
			// 后面的数字逐个加1 直到2010
			// 创建Document对象，拿到元素对象再操作属性。
			File file = new File(picDestURL);
			if (!file.exists()) {
				file.mkdir();
			}
			Map<String,String> map = new HashMap<>();
			map.put("Hm_lvt_d60c24a3d320c44bcd724270bc61f703", "1555982316");
			map.put("BDTUJIAID", "8ee665ee030a2e62aa88219122a2577b");
			map.put("Hm_lpvt_d60c24a3d320c44bcd724270bc61f703", "1555983889");
			String User_Agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
			for (int i = start; i <= end; i++) {
				// 分2010次拿到每页的网址
				try {
					String mainURL = webURL + i;
					
					Document doc = Jsoup.connect(mainURL).userAgent(User_Agent).
							data(map).get();
//					System.out.println(doc);
					// 拿到每个页面的每个class元素relative block works-detail hover-none new-works-img-box
					Elements elements = doc.getElementsByClass("new-works-img-box");
					System.out.println(elements);
					for (Element element : elements) {
						try {
							// 获取每个页面的大图的网址
							String picHrefUrl = element.attr("href");
							// 获取每个大图的详细地址
							String picURL = getPicURL(picHrefUrl);
							// 从图片的详细地址开始下载单个图片到本地目标路径
							System.out.println(picURL);
							synchronized (count) {
								++count;
								System.out.println("已抓取" + count + "张图片。");
							}
							File file2 = new File(picDestURL + "\\" + i);
							if(!file2.exists()) {
								file2.mkdirs();
							}
							downloadPicToLocal(picURL, file2+"\\"+count+getNameExtension(picURL));							
						} catch (Exception e) {
							e.printStackTrace();
						}
 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
 
			}
			// 计数器归零
			count = 0;
		} catch (Exception e) {
			flag = false;
			new RuntimeException("在服务器找不到对应图片，正在寻找下一个图片中。。。");
		}
		return flag;
	}
 
	public static void main(String[] args) {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);
		for (int i = 1; i < 6; i++) {
			newFixedThreadPool.submit(new Thread(new JsoupSpider((i-1)*100+1,i*100)));
		}		
//		System.out.println(result);
 
	}

	@Override
	public void run() {
		boolean result = getOnePic("http://www.nipic.com/photo/jingguan/shanshui/index.html?page=",
				"D:\\sylar\\PictureSpider",start,end);
		
	}
}