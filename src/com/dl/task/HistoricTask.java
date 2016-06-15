package com.dl.task;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.dl.dao.HistoryDao;
import com.dl.pojo.Mstinfo_his;


public class HistoricTask extends TimerTask {
	private Logger logger = Logger.getLogger(HistoricTask.class); 
	@Override
	public void run() {
		
		HistoryDao historyDao = HistoryDao.getInstance();
		List<Mstinfo_his> list = historyDao.getMstinfoForHistory();
		if(historyDao.dataToHistory(list)&&historyDao.dataToReport())
		{
			historyDao.updateMstinfoForReport();
//			historyDao.deleteMstinfoForHistory();
			logger.info("数据迁移timer执行完成");
			System.out.println("执行完成");
			
		}else{
			System.out.println("执行失败");
			logger.error("数据迁移timer执行失败");
		}
//		System.out.println("111");
	}

}
