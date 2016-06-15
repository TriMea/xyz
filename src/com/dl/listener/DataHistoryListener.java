package com.dl.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dl.timerimp.HistoryTimer;

public class DataHistoryListener implements ServletContextListener {
	
	private HistoryTimer historyTimer = null;
   //监听结束
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		 if(historyTimer != null)
	        {
	           historyTimer.stop();
	        }
	}
  //监听开始
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("监听开始");
		historyTimer = new HistoryTimer(1);
		historyTimer.start();
       
	}

}
