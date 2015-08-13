package com.likeyichu.spider;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker {
	static final Logger logger = LoggerFactory.getLogger(Worker.class);
	int secnod;
	public Worker(int second){
		this.secnod=second;
	}
	public void start(){
		ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(10);
		ses.scheduleWithFixedDelay(new CsdnSpider(), 0, secnod,TimeUnit.SECONDS);
	}
	
	
	public static void main(String[] args) {
		int period=60;// one minute
		if(args!=null)
			period=Integer.valueOf(args[0]);
		logger.info("period(秒) is :"+period);
		Worker worker=new Worker(period);
		worker.start();
	}
}
