package ServerUtils;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class pdfConfigs {
	
	
	/**
	 * @param table
	 * @param col1
	 * @param col2
	 * @param col3
	 * creates a row in the given table
	 */
	public static void addRows(PdfPTable table, String col1, int col2, int col3) {
	    table.addCell(col1);
	    table.addCell(Integer.toString(col2));
	    table.addCell(Integer.toString(col3));

	}
	
	/**
	 * @param table
	 * creates a row in the given table
	 */
	public static void addRows(PdfPTable table, int col1, String col2, int col3) {
	    table.addCell(Integer.toString(col1));
	    table.addCell(col2);
	    table.addCell(Integer.toString(col3));

	}
	/**
	 * @param table
	 * creates a row in the given table
	 */
	public static void addRows(PdfPTable table, String res, int numOfOrders, int delayedOrders,
			int refunds) {
		// TODO Auto-generated method stub
	    table.addCell(res);
	    table.addCell(Integer.toString(numOfOrders));
	    table.addCell(Integer.toString(delayedOrders));
	    table.addCell(Float.toString(refunds));
	}
	
	/**
	 * @param table
	 * creates a row in the given table
	 */
	public static void addRows(PdfPTable table, String res, int numOfOrders, int delayedOrders,
			float delayedPercentage) {
		// TODO Auto-generated method stub
	    table.addCell(res);
	    table.addCell(Integer.toString(numOfOrders));
	    table.addCell(Integer.toString(delayedOrders));
	    table.addCell(Float.toString(delayedPercentage)+"%");
	}
	//Avg Preparing time
	/**
	 * @param table
	 * creates a row in the given table
	 */
	public static void addRows(PdfPTable table, String res, int numOfOrders, int delayedOrders,
			float delayedPercentage, int preparingTime) {
		// TODO Auto-generated method stub
	    table.addCell(res);
	    table.addCell(Integer.toString(numOfOrders));
	    table.addCell(Integer.toString(delayedOrders));
	    table.addCell(Float.toString(delayedPercentage)+"%");
	    table.addCell(Integer.toString(preparingTime));
	}
	
	
	/**
	 * Creates a table header with the given column names
	 * @param table
	 * @param col1
	 * @param col2
	 * @param col3
	 */
	public static void addTableHeader(PdfPTable table, String col1, String col2, String col3) {
	    Stream.of(col1,col2,col3)
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	
	/**
	 * Creates a table header with the given column names
	 * @param table
	 * @param col1
	 * @param col2
	 * @param col3
	 * @param col4
	 */
	public static void addTableHeader(PdfPTable table, String col1, String col2, String col3, String col4) {
	    Stream.of(col1,col2,col3,col4)
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}

	
	/**
	 * Creates a table header with the given column names
	 * @param table
	 * @param col1
	 * @param col2
	 * @param col3
	 * @param col4
	 * @param col5
	 */
	public static void addTableHeader(PdfPTable table, String col1, String col2, String col3, String col4, String col5) {
	    Stream.of(col1,col2,col3,col4,col5)
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	/**
	 * @param titleTxt
	 * @param font
	 * @return a Paragraph with the given title and font.
	 */
	public static Paragraph createTitle(String titleTxt, Font font)
	{
		Chunk c = new Chunk(titleTxt);
		c.setFont(font);
		c.setUnderline(2, -4);
		Paragraph title = new Paragraph();
		title.add(c);
		title.setAlignment(1);
		return title;
	}
	
	
	/**
	 * @param vals
	 * @return histogram with the given values.
	 */
	public static JFreeChart generateHist(ArrayList<Double> vals) {
		if(vals.isEmpty())
			return null;
		double[] values = vals.stream().mapToDouble(Double::doubleValue).toArray();
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 5);

        JFreeChart histogram = ChartFactory.createHistogram("Quarterly orders in restaurants",
                   "Orders from a restaurant in a quarter", "amount of restaurants", dataset, PlotOrientation.VERTICAL, false, true, false);


		return histogram;
	}
}
