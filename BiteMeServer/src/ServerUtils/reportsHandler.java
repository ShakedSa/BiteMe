package ServerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import JDBC.mysqlConnection;

public class reportsHandler {


	/**
	 * Creates monthly revenue report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyRevenueReportPdf(String Branch, String Month) {
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	int numOfOrders;
	int totalEarnings;
	int netIncome=0;
	document.addTitle("Monthly Report");
	
		try {
			PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempRevenueReport.pdf"));
			document.open();
			Font font = FontFactory.getFont(FontFactory.COURIER, 35, BaseColor.BLACK);
			Chunk c = new Chunk("Monthly Revenue Report\n");
			c.setFont(font);
			c.setUnderline(2, -4);
			Paragraph title = new Paragraph();
			title.add(c);
			title.setAlignment(1);
			document.add(title);
			Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Date :" + currentDate.toString() +"\n\n\n",font);
			reportDetails.setAlignment(1);
			document.add(reportDetails);
			// handling sql data:
			// table: name,total orders, total income.
				PdfPTable table = new PdfPTable(3);
				pdfConfigs.addTableHeader(table,"Restaurant Name","Total orders","Total income");
				for(String res: Restaurants) { // for each restaurant in branch:
						numOfOrders=mysqlConnection.getNumOfOrders(res,LocalDate.now().getMonth());
						totalEarnings=mysqlConnection.getEarnings(res,LocalDate.now().getMonth());
						netIncome+=totalEarnings;
						pdfConfigs.addRows(table,res,numOfOrders,totalEarnings);
				}
				document.add(table);
				font.setSize(25);
				document.add(new Paragraph("\n\n\n total NET Income: "+netIncome,font));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//after creating the PDF with relevant data, store it inside the SQL as BLOB:
		ArrayList<String> info = new ArrayList<String>();
		//reportType,Month,Year,branch
		info.add("MonthlyRevenueReport");
		info.add(Integer.toString(currentDate.getMonthValue()));
		info.add(Integer.toString(currentDate.getYear()));
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
			File f = new File(Branch + "TempRevenueReport.pdf");
			f.delete();
		} catch (Exception e) {e.printStackTrace();}
		

	}
	/**
	 * Creates monthly orders report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyOrdersReportPdf(String Branch, String Month) {
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	ArrayList<String> OrderedDishes;
	int numOfOrders,i=1,ordersSum=0,mostOrderedAmount=0;
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
			Chunk c = new Chunk(" Monthly Order Report\n");
			c.setFont(font);
			c.setUnderline(2, -4);
			Paragraph title = new Paragraph();
			title.add(c);
			title.setAlignment(1);
			document.add(title);
			//set branch and date info:
			Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Date :" + currentDate.toString() +"\n\n\n",font);
			reportDetails.setAlignment(1);
			document.add(reportDetails);
			//table for restaurant x:
			for(String res: Restaurants) {
				i=1;
				//get dishes list:
				OrderedDishes= mysqlConnection.getDishesList(res,LocalDate.now().getMonth());
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
					numOfOrders = mysqlConnection.getNumOfOrderedDishes(res,LocalDate.now().getMonth(),dish);
					pdfConfigs.addRows(table,i,dish,numOfOrders);
					ordersSum+=numOfOrders;
					i++;
					if(numOfOrders>mostOrderedAmount){
						mostOrderedAmount=numOfOrders;
						mostOrderedDish=dish;
					}
				}
				document.add(table);
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
		info.add(Integer.toString(currentDate.getMonthValue()));
		info.add(Integer.toString(currentDate.getYear()));
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
			File f = new File(Branch + "TempOrdersReport.pdf");
			f.delete();
		} catch (Exception e) {e.printStackTrace();}
		

	}
	

	/**
	 * Creates monthly performance report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyPerformanceReportPdf(String Branch, String Month) {

		Document document = new Document();
		LocalDate currentDate=LocalDate.now();
		ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
		int numOfOrders, delayedOrders, netIncome=0,totalDelayedOrders=0,totalOrders=0;
		float delayedPercentage=0,totalDelayedPercentage=0;
		document.addTitle("Monthly Report");
		
			try {
				PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempPerformanceReport.pdf"));
				document.open();
				Font font = FontFactory.getFont(FontFactory.COURIER, 30, BaseColor.BLACK);
				Chunk c = new Chunk("Monthly Performance Report\n");
				c.setFont(font);
				c.setUnderline(2, -4);
				Paragraph title = new Paragraph();
				title.add(c);
				title.setAlignment(1);
				document.add(title);
				Paragraph reportDetails= new Paragraph("Branch: "+ Branch + " \n Date :" + currentDate.toString() +"\n\n\n",font);
				reportDetails.setAlignment(1);//center=1
				document.add(reportDetails);
				// handling sql data:
				// table: restaurant name,total orders,# delayed orders, %delayed orders 
					PdfPTable table = new PdfPTable(4);
					pdfConfigs.addTableHeader(table,"Restaurant Name","Total orders","Delayed orders","% of orders delayed");
					for(String res: Restaurants) { // for each restaurant in branch:
							numOfOrders=mysqlConnection.getNumOfOrders(res,LocalDate.now().getMonth());
							delayedOrders=mysqlConnection.getDelayedOrders(res,LocalDate.now().getMonth());
							if(numOfOrders!=0)
								delayedPercentage=(float)100*delayedOrders/numOfOrders;
							else
								delayedPercentage=0;
							pdfConfigs.addRows(table,res,numOfOrders,delayedOrders,delayedPercentage);
							totalDelayedOrders+=delayedOrders;
							totalOrders+=numOfOrders;
					}
					document.add(table);
					font.setSize(25);
					if(totalDelayedOrders!=0)
						totalDelayedPercentage=(float)100*totalDelayedOrders/totalOrders;
					else
						totalDelayedPercentage=0;
					document.add(new Paragraph("\n total Orders: "+totalOrders + "\nTotal Delayed :" + totalDelayedPercentage+"%",font));
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//after creating the PDF with relevant data, store it inside the SQL as BLOB:
			ArrayList<String> info = new ArrayList<String>();
			//reportType,Month,Year,branch
			info.add("MonthlyPerformanceReport");
			info.add(Integer.toString(currentDate.getMonthValue()));
			info.add(Integer.toString(currentDate.getYear()));
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
				File f = new File(Branch + "TempRevenuePerformance.pdf");
				f.delete();
			} catch (Exception e) {e.printStackTrace();}
			

	}
}
