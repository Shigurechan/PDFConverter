
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/* ##################################################################
 * 単一の画像ファイルからPDFを生成 
 * ##################################################################*/

public class ConverterPage extends Thread
{
	File pageName;
	
	public ConverterPage(String file)
	{
		pageName = new File(file);
	}
	
	@Override
	public void run()
	{
		try
		{				
			BufferedImage b  = ImageIO.read(pageName);								
			Image img = new Image(b.getWidth(),b.getHeight(),pageName.getPath());	
			PDDocument document = new PDDocument();											
			PDRectangle rec = new PDRectangle();
			rec.setUpperRightX(0);
			rec.setUpperRightY(0);
			rec.setLowerLeftX(img.width);
			rec.setLowerLeftY(img.height);
			PDPage page = new PDPage(rec);			
			document.addPage(page);
			
			PDImageXObject xImage = PDImageXObject.createFromFile(pageName.getPath(),document);
			PDPageContentStream stream = new PDPageContentStream(document,page);
			stream.drawImage(xImage, 0,0);
			stream.close();
			
			document.save(pageName.getParent() + "\\" + pageName.getName() + ".pdf");	
//			System.out.println("--->: " + fileName.getParent() + "\\" + fileName.getName() + ".pdf");
			
				
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
