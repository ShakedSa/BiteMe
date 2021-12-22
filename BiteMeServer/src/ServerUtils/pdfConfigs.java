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
	
	public static void addCustomRows(PdfPTable table) 
			  throws URISyntaxException, BadElementException, IOException {
			  // Path path = Paths.get(ClassLoader.getSystemResource("C:\\Users\\Eden\\git\\BiteMe\\BiteMeServer\\test.jpg").toURI());
			    Image img = Image.getInstance("test.jpg");
			    		//(getClass().getResource("../images/" +"dominos" + "-logo.jpg").toString());
			    img.scalePercent(10);

			    PdfPCell imageCell = new PdfPCell(img);
			    table.addCell(imageCell);

			    PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
			    horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    table.addCell(horizontalAlignCell);

			    PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
			    verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			    table.addCell(verticalAlignCell);
			}
	
}
