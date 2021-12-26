package ServerUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import JDBC.mysqlConnection;
import jdk.vm.ci.meta.Local;

public class DailyThread extends Thread{
	

    // class fields
    private boolean keepRunning;
    private final long sleepTime;

    // constructor
   public DailyThread() {
        keepRunning = true;
        this.sleepTime = 24 * 60 * 60 * 1000; //24hours interval
    } // end constructor

    // class methods
    // force the thread to stop
    void stopRunning() {
        keepRunning = false;

        interrupt();
    } // end method stopRunning

    // Runnable interface methods
    // run 
    @Override
    public void run() {
    	int month=0,year=0;
        while (keepRunning) {
        	LocalDateTime date = LocalDateTime.now();
        	month=0;
        	year=0;
        	//reset daily balance on w4c cards:
        	mysqlConnection.resetDailyBalance();
        	if(date.getDayOfMonth()==1){//new month !
        		//reset monthly w4c balance:
            	mysqlConnection.resetMonthlyBalance();
        		month= date.getMonthValue();
        		year=date.getYear();
        		if(month==1) // if new year, make report for 12,year-1;
        		{
        			month=12;
        			year--;
            		reportsHandler.createAllReports(month,year);
        		}
        		else
        			reportsHandler.createAllReports(month-1,year);
        		
        	}
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                if (keepRunning) {
                    e.printStackTrace();
                }
            }
        }
    } // end method run

}
