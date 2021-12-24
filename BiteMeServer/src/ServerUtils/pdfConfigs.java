package ServerUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sun.tools.javac.util.ArrayUtils;

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
			float delayedPercentage) {
		// TODO Auto-generated method stub
	    table.addCell(res);
	    table.addCell(Integer.toString(numOfOrders));
	    table.addCell(Integer.toString(delayedOrders));
	    table.addCell(Float.toString(delayedPercentage)+"%");
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
	
	
	
	
	public static JFreeChart generateBarChart() {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.setValue(791, "Population", "2017");
		dataSet.setValue(978, "Population", "2018");
		dataSet.setValue(1262, "Population", "2019");
		dataSet.setValue(1650, "Population", "2020");
		dataSet.setValue(2519, "Population", "2021");
		dataSet.setValue(6070, "Population", "today");

		JFreeChart chart = ChartFactory.createBarChart(
				"Aviel Gayness growth over the years", "Year", "Dicks sucked in millions",
				dataSet, PlotOrientation.VERTICAL, false, true, false);

		return chart;
	}
	
	public static JFreeChart generateHist(ArrayList<Double> vals) {
		double[] values = vals.stream().mapToDouble(Double::doubleValue).toArray();
//        double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
//                12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
//                49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
//                93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
//                77, 44, 58, 91, 10, 67, 57, 19, 88, 84                                
//              };
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 5);

        JFreeChart histogram = ChartFactory.createHistogram("Quarterly orders in restaurants",
                   "Orders from a restaurant in a quarter", "amount of restaurants", dataset, PlotOrientation.VERTICAL, false, true, false);


		return histogram;
	}
}
