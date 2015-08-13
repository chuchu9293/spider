package com.likeyichu.spider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsdnSpider implements Runnable{
	static final Logger logger = LoggerFactory.getLogger(CsdnSpider.class);
	Object lock=new Object();



	public Map<String, String> getCsdnBlogIndex() {
		Map<String, String> map = new HashMap<>();
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36 LBBROWSER";
		Document doc = null;
		Elements elements = null;
		Element element = null;
		try {
			doc = Jsoup.connect("http://blog.csdn.net/chuchus/")
					.userAgent(userAgent).get();
		} catch (IOException e) {
			logger.error("getCsdnBlogIndex ERROR",e);
		}
		elements = doc.select("#blog_rank");
		element = elements.get(0);
		String visitCountsStr = element.child(0).child(0).text();
		String rank = element.child(3).child(0).text();
		visitCountsStr = visitCountsStr.substring(0,
				visitCountsStr.length() - 1);
		rank = rank.substring(1, rank.length() - 1);
		map.put("访问次数", visitCountsStr);
		map.put("博客排名", rank);
		int csdnVisitCounter=Integer.valueOf(visitCountsStr);
		if(StaticData.csdnVisitCounter==0)
			StaticData.csdnVisitCounter=csdnVisitCounter;
		synchronized(lock) {
			if(csdnVisitCounter>StaticData.csdnVisitCounter){
				logger.info("爬取有效，访问增加了:"+(csdnVisitCounter-StaticData.csdnVisitCounter)+"现为："+
						csdnVisitCounter);
				//此句必须放在这里。因为csdn服务端缓存，可能这次查是n+1次，下次查仍是n次访问。
				StaticData.csdnVisitCounter=csdnVisitCounter;
			}
			logger.debug("爬取效果暂无体现，仍为："+csdnVisitCounter);
		}
		
		return map;
	}
	@Override
	public void run() {
		try{
		getCsdnBlogIndex();
		}catch(Exception e){
			logger.error("run() ERROR",e);
		}
	}
	public static void main(String[] args) {
	}

}
