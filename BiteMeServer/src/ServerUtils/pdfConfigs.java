package ServerUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class pdfConfigs {
	public static void addRows(PdfPTable table, String col1, int col2, int col3) {
	    table.addCell(col1);
	    table.addCell(Integer.toString(col2));
	    table.addCell(Integer.toString(col3));

	}
	public static void addRows(PdfPTable table, int col1, String col2, int col3) {
	    table.addCell(Integer.toString(col1));
	    table.addCell(col2);
	    table.addCell(Integer.toString(col3));

	}
	
	public static void addRows(PdfPTable table, String res, int numOfOrders, int delayedOrders,
			float delayedPercentage) {
		// TODO Auto-generated method stub
	    table.addCell(res);
	    table.addCell(Integer.toString(numOfOrders));
	    table.addCell(Integer.toString(delayedOrders));
	    table.addCell(Float.toString(delayedPercentage)+"%");
	}
	
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

	
}
