package ServerUtils;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import Entities.ServerResponse;
import JDBC.mysqlConnection;

public class reportsHandler {


	/**
	 * Creates monthly revenue report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyRevenueReportPdf(String Branch, String Month, String Year) {
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	int numOfOrders, totalEarnings, highestEarning=0;
	int netIncome=0;
	String profitableRestaurant="";
	document.addTitle("Monthly Report");
	
		try {
			PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempRevenueReport.pdf"));
			document.open();
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 35, BaseColor.BLACK);
			//add title and report details:
			document.add(pdfConfigs.createTitle("Monthly Revenue Report\n", font));
			Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Report Date :" + Year+"-"+Month+"-01"+"\n"
					+ "Creation Date: "+currentDate.toString() +"\n\n",font);			reportDetails.setAlignment(1);
			document.add(reportDetails);
			// handling sql data:
			// table: name,total orders, total income.
				PdfPTable table = new PdfPTable(3);
				pdfConfigs.addTableHeader(table,"Restaurant Name","Total orders","Total income");
				for(String res: Restaurants) { // for each restaurant in branch:
						numOfOrders=mysqlConnection.getNumOfOrders(res,Month,Year);
						totalEarnings=mysqlConnection.getEarnings(res,Month,Year);
						netIncome+=totalEarnings;
						pdfConfigs.addRows(table,res,numOfOrders,totalEarnings);
						if(totalEarnings>=highestEarning) {
							highestEarning=totalEarnings;
							profitableRestaurant=res;
						}
				}
				document.add(table);
				font.setSize(25);
				document.add(new Paragraph("\nMost profitable restaurant: " + profitableRestaurant 
						+ " that earned "+ highestEarning +" NIS"+"\n Total NET Income: "+netIncome,font));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//after creating the PDF with relevant data, store it inside the SQL as BLOB:
		ArrayList<String> info = new ArrayList<String>();
		//reportType,Month,Year,branch
		info.add("MonthlyRevenueReport");
		info.add(Month);
		info.add(Year);
		info.add(Branch);
		//loading the temp report:
		InputStream is=null;
		try {
			is = new FileInputStream(new File(Branch + "TempRevenueReport.pdf"));
			mysqlConnection.updateFile(is, info);
			//close file connection
			if(is!=null)
				is.close();
			//delete temp file from server.
			//File f = new File(Branch + "TempRevenueReport.pdf");
			//f.delete();
		} catch (Exception e) {e.printStackTrace();}

	}
	/**
	 * Creates monthly orders report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyOrdersReportPdf(String Branch, String Month,String Year) {
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	ArrayList<String> OrderedDishes;
	int numOfOrders,i=1,ordersSum=0,mostOrderedAmount=0,RestaurantOrdersSum=0;
	String mostOrderedDish="none";
	document.addTitle("Monthly Report");
	/*
	 * for each restaurant in branch:
	 * 		create table including: dish name,components used,amount of orders
	 */
		try {
			PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempOrdersReport.pdf"));
			document.open();
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 35, BaseColor.BLACK);
			//set title:
			document.add(pdfConfigs.createTitle("Monthly Order Report\n", font));
			//set branch and date info:
			Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Report Date :" + Year+"-"+Month+"-01"+"\n"
					+ "Creation Date: "+currentDate.toString() +"\n\n",font);
			reportDetails.setAlignment(1);
			document.add(reportDetails);
			//table for restaurant x:
			for(String res: Restaurants) {
				RestaurantOrdersSum=0;
				i=1;
				//get dishes list:
				OrderedDishes= mysqlConnection.getDishesList(res,Month,Year);
				if(OrderedDishes.size()==0) {
					document.add(new Paragraph(" "+res + " Had no orders this month. \n\n"));
					continue; // dont create empty tables
				}
				//set table rest name
				document.add(new Paragraph(" \n"+res + " Restaurant orders: \n\n"));
				//set table
				PdfPTable table = new PdfPTable(3);
				pdfConfigs.addTableHeader(table,"#","Dish Name","Total orders");

				//get total orders of the dish and create table row per dish in a restaurant
				for(String dish:OrderedDishes) {
					numOfOrders = mysqlConnection.getNumOfOrderedDishes(res,Month,Year,dish);
					pdfConfigs.addRows(table,i,dish,numOfOrders);
					ordersSum+=numOfOrders;
					RestaurantOrdersSum+=numOfOrders;
					i++;
					if(numOfOrders>mostOrderedAmount){
						mostOrderedAmount=numOfOrders;
						mostOrderedDish=dish;
					}
				}
				document.add(table);
				document.add(new Paragraph("Total Number of orders on the restaurant: "+ RestaurantOrdersSum));
			}//add some general information before closing the pdf file.
			document.add(new Paragraph("Most Ordered Dish: "+mostOrderedDish + " with "+mostOrderedAmount+
					" orders\nTotal Dish ordered on branch: "+ ordersSum));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//after creating the PDF with relevant data, store it inside the SQL as BLOB:
		ArrayList<String> info = new ArrayList<String>();
		//arraylist inside order: reportType,Month,Year,branch
		info.add("MonthlyOrdersReport");
		info.add(Month);
		info.add(Year);
		info.add(Branch);
		//loading the temp report:
		InputStream is=null;
		try {
			is = new FileInputStream(new File(Branch + "TempOrdersReport.pdf"));
			mysqlConnection.updateFile(is, info);
			//close file connection
			if(is!=null)
				is.close();
			//delete temp file from server.
		//	File f = new File(Branch + "TempOrdersReport.pdf");
		//	f.delete();
		} catch (Exception e) {e.printStackTrace();}
		

	}
	

	/**
	 * Creates monthly performance report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyPerformanceReportPdf(String Branch, String Month, String Year) {
		ArrayList<Double> ratingAverages = new ArrayList<Double>();
		Document document = new Document();
		LocalDate currentDate=LocalDate.now();
		ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
		int numOfOrders, delayedOrders, totalDelayedOrders=0,totalOrders=0,prepTime=0;
		float delayedPercentage=0,totalDelayedPercentage=0;
		document.addTitle("Monthly Report");
		
			try {
				PdfWriter writer =	PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempPerformanceReport.pdf"));
				document.open();
				Font font = FontFactory.getFont(FontFactory.HELVETICA, 30, BaseColor.BLACK);
				document.add(pdfConfigs.createTitle("Monthly Performance Report\n", font));
				Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Report Date :" + Year+"-"+Month+"-01"+"\n"
						+ "Creation Date: "+currentDate.toString() +"\n\n",font);				reportDetails.setAlignment(1);//center=1
				document.add(reportDetails);
				// handling sql data:
				// table: restaurant name,total orders,# delayed orders, %delayed orders 
					PdfPTable table = new PdfPTable(5);
					pdfConfigs.addTableHeader(table,"Restaurant Name","Total orders","Delayed orders","% of orders delayed","Avg Preparing time(Minutes)");
					for(String res: Restaurants) { // for each restaurant in branch:
							prepTime=mysqlConnection.getAvgPrepTime(res,Month,Year);
							numOfOrders=mysqlConnection.getNumOfOrders(res,Month,Year);
							delayedOrders=mysqlConnection.getDelayedOrders(res,Month,Year);
							//int ratingAvg = mysqlConnection.getAvgRating(res,Month,Year);
						//	ratingAverages.add((double) ratingAvg);
							if(numOfOrders!=0)
								delayedPercentage=(float)100*delayedOrders/numOfOrders;
							else
								delayedPercentage=0;
							pdfConfigs.addRows(table,res,numOfOrders,delayedOrders,delayedPercentage,prepTime);
							totalDelayedOrders+=delayedOrders;
							totalOrders+=numOfOrders;
					}
					document.add(table);
					font.setSize(25);
					if(totalDelayedOrders!=0)
						totalDelayedPercentage=(float)100*totalDelayedOrders/totalOrders;
					else
						totalDelayedPercentage=0;
					document.add(new Paragraph("\n total Orders: "+totalOrders + "\nTotal Delayed :" + totalDelayedPercentage+"%\n\n",font));
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//after creating the PDF with relevant data, store it inside the SQL as BLOB:
			ArrayList<String> info = new ArrayList<String>();
			//reportType,Month,Year,branch
			info.add("MonthlyPerformanceReport");
			info.add(Month);
			info.add(Year);
			info.add(Branch);
			//loading the temp report:
			InputStream is=null;
			try {
				is = new FileInputStream(new File(Branch + "TempPerformanceReport.pdf"));
				mysqlConnection.updateFile(is, info);
				//close file connection
				if(is!=null)
					is.close();
				//delete temp file from server.
			//	File f = new File(Branch + "TempPerformanceReport.pdf");
			//	f.delete();
			} catch (Exception e) {e.printStackTrace();}
			

	}
	
	/**
	 * creates all 3 types of monthly reports for the given month and year.
	 * @param month
	 * @param year
	 */
	public static void createAllReports(int month, int year) {
		String[] Branches = {"North","Central","South"};
		if(month==13) // make sure to create new year reports if needed (since we enter month+1 here on server.java)
		{
			month=1;
			year++;
		}
		createQuarterlyReports(month, year, Branches);
		String monthS=Integer.toString(month),yearS=Integer.toString(year);
		for(String s:Branches) {
			createMonthlyOrdersReportPdf(s, monthS, yearS);
			createMonthlyPerformanceReportPdf(s, monthS, yearS);
			createMonthlyRevenueReportPdf(s, monthS, yearS);
		}
	}
	/**
	 * @return the date of the last report on sql
	 * returns today if null
	 */
	public static int[] getLastReportDate() {
		int year,month;

		Date d = mysqlConnection.checkLastReportDate();
		if(d!=null)
		{
			//deNormalize values:
			month=d.getMonth()+1;
			year=d.getYear()+1900;
		}
		else {//if no reports were ever made, return last month (to initialize empty reports on sql)
			month=LocalDate.now().getMonthValue()-2;
			year=LocalDate.now().getYear();
			if(month==0) {
				month=12;
				year--;
			}
		}
		if(month==1) {
			month=12;
			year--;
		}
		int arr[] = {year,month};
		return arr;
	}
	
	
	/**
	 * Creates quarterly revenue reports for the specific year and quarter for the entire branches.
	 * @param month
	 * @param year
	 * @param Branches
	 */
	public static void createQuarterlyReports(int month, int year, String[] Branches) {
		int currentMonth= LocalDate.now().getMonthValue();
		if(month%3==0 || (currentMonth-1)%3==0 ) {//quarter ended, create new quarter revenue report.
			for(String branch:Branches)
    			reportsHandler.quarterlyRevenueReportPdf(branch, Integer.toString(month/3), Integer.toString(year));
		}
	}


	/**
	 * Creates quarterly revenue report for a given branch and quarter, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */ 
	
	//NEED TO ADD HISTOGRAM FOR RATINGS:
	/*
	 * 					//add ratings histogram : ratingAverages
					PdfContentByte contentByte = writer.getDirectContent();
					PdfTemplate template = contentByte.createTemplate(350, 350);
					Graphics2D graphics2d = template.createGraphics(350, 350,new DefaultFontMapper());
					Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 350,350);
				//	JFreeChart chart =pdfConfigs.generateHist(ratingAverages);
					
					if(!ratingAverages.isEmpty())
					{
						double[] values = ratingAverages.stream().mapToDouble(Double::doubleValue).toArray();
				        HistogramDataset dataset = new HistogramDataset();
				        dataset.addSeries("key", values, 5);

				        JFreeChart chart = ChartFactory.createHistogram("Average Ratings in restaurants",
				                   "Rating Average this month", "amount of restaurants", dataset, PlotOrientation.VERTICAL, false, true, false);
				        chart.draw(graphics2d, rectangle2d);
						
					}
				graphics2d.dispose();
				contentByte.addTemplate(template, 0, 0);
	 */
	public static ServerResponse quarterlyRevenueReportPdf(String Branch, String quarter, String Year) {
		
		// add check if report exists and return serverResponse with value.
		if(mysqlConnection.checkReportExists(Branch, quarter, Year)) {
			return new ServerResponse("exists");
		}
		
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	int numOfOrders, totalEarnings,highestEarning=0;
	int quarterInt = Integer.parseInt(quarter);
	String profitableRestaurant="";
	int netIncome=0, endMonth=quarterInt*3, startMonth=endMonth-2;
	ArrayList<Double> values = new ArrayList<Double>();
	document.addTitle("Quarterly Revenue Report");
	
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempQRevenueReport.pdf"));
			document.open();
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 30, BaseColor.BLACK);
			//add title and report details:
			document.add(pdfConfigs.createTitle("Quarterly Revenue Report\n", font));
			font.setSize(20);
			Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Report date:" + Year+" Quarter: "+quarter+"\n"
					+ "Creation Date: "+currentDate.toString() +"\n\n",font);
			reportDetails.setAlignment(1);
			document.add(reportDetails);
			// handling sql data:
			// table: name,total orders, total income.
				PdfPTable table = new PdfPTable(3);
				pdfConfigs.addTableHeader(table,"Restaurant Name","Total orders","Total income");
				for(String res: Restaurants) { // for each restaurant in branch:
					totalEarnings=0; // reset
					numOfOrders=0;
					for(int i=startMonth; i<=endMonth;i++) {
						numOfOrders+=mysqlConnection.getNumOfOrders(res,Integer.toString(i),Year);
						totalEarnings+=mysqlConnection.getEarnings(res,Integer.toString(i),Year);
					}
						values.add((double) numOfOrders);
						netIncome+=totalEarnings;
						pdfConfigs.addRows(table,res,numOfOrders,totalEarnings);
						if(totalEarnings>=highestEarning)
						{
							highestEarning=totalEarnings;
							profitableRestaurant=res;
						}
				}
				document.add(table);
				font.setSize(25);
				document.add(new Paragraph("\nMost profitable restaurant:" + profitableRestaurant +
						" that earned "+ highestEarning +" NIS"+ "\nTotal branch income: "+netIncome+ "\n",font));
				PdfContentByte contentByte = writer.getDirectContent();
				PdfTemplate template = contentByte.createTemplate(350, 350);
				Graphics2D graphics2d = template.createGraphics(350, 350,new DefaultFontMapper());
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, 350,350);
				JFreeChart chart =pdfConfigs.generateHist(values);
				if(chart!=null)
					chart.draw(graphics2d, rectangle2d);
				graphics2d.dispose();
				contentByte.addTemplate(template, 0, 0);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//after creating the PDF with relevant data, store it inside the SQL as BLOB:
		ArrayList<String> info = new ArrayList<String>();
		//reportType,Month,Year,branch
		info.add("QuarterlyRevenueReport");
		info.add(quarter);
		info.add(Year);
		info.add(Branch);
		//loading the temp report:
		InputStream is=null;
		try {
			is = new FileInputStream(new File(Branch + "TempQRevenueReport.pdf"));
			mysqlConnection.updateFile(is, info);
			//close file connection
			if(is!=null)
				is.close(); 
			//delete temp file from server.
			File f = new File(Branch + "TempQRevenueReport.pdf");
			f.delete();
		} catch (Exception e) {e.printStackTrace();}
		
		return new ServerResponse("created");
	}

}
