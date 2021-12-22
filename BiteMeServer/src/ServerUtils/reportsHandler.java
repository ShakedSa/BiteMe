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
		info.add("Monthly Revenue Report");
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
	 * Creates monthly revenue report for a given branch and month, and stores it in SQL
	 * @param Branch
	 * @param Month
	 */
	public static void createMonthlyOrdersReportPdf(String Branch, String Month) {
	Document document = new Document();
	LocalDate currentDate=LocalDate.now();
	ArrayList<String> Restaurants= mysqlConnection.getRestaurantList(Branch);
	int numOfOrders;
	int totalEarnings;
	int netIncome=0;
	document.addTitle("Monthly Report");
	
		try {
			PdfWriter.getInstance(document, new FileOutputStream(Branch + "TempOrdersReport.pdf"));
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
		info.add("Monthly Revenue Report");
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
}
