package com.dl.timerimp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.dl.servlet.OrderReceiveServlet;
import com.dl.task.HistoricTask;

public class HistoryTimer {

	private final Timer timer = new Timer();
	private final int day;
	private Logger logger = Logger.getLogger(HistoryTimer.class); 
	public HistoryTimer(int days) {
	day = days;
	}

	public void start() {
	
	    timer.schedule(new HistoricTask(),getFirstDate(),day*24*60*60*1000);
//		timer.schedule(new HistoricTask(),0,day*24*60*60*1000);
	    System.out.println("启动");
	    logger.info("数据迁移timer启动");
	}

	private Date getFirstDate()
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 1);
		ca.add(Calendar.DAY_OF_MONTH, +1);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTime();
	}
	public void stop() {
    logger.info("数据迁移timer停止");	
	timer.cancel();
	}
}
