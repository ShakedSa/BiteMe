package ServerUtils;

import java.time.LocalDateTime;

import JDBC.mysqlConnection;

/**
 * @author Eden
 * This method is a thread that runs and does required operations every 24hours.
 */
public class DailyThread extends Thread{
	

    // class fields
    private boolean keepRunning;
    private final long sleepTime;

    // constructor
   public DailyThread() {
        keepRunning = true;
        this.sleepTime = 24 * 60 * 60 * 1000; //24hours interval
    } // end constructor

    /**
     * force the thread to stop
     */
    void stopRunning() {
        keepRunning = false;

        interrupt();
    } // end method stopRunning


    /**
     *   Runnable interface methods
     */
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
        		if(month==1) // if new year, previous month is 12,year-1;
        		{
        			month=12;
        			year--;
            		reportsHandler.createAllReports(month,year);
            		mysqlConnection.createMonthlySuppliersReceipt(month,year);
        		}
        		else {
        			reportsHandler.createAllReports(month-1,year);
        			mysqlConnection.createMonthlySuppliersReceipt(month,year);
        		}
        		
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
