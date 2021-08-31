
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ConverterPage extends Thread
{
	File pageName;	//ページ
	
	public ConverterPage(String file)
	{
		pageName = new File(file);
	}
	
	@Override
	public void run()
	{
		try
		{				
			BufferedImage b  = ImageIO.read(pageName);								//画像ロード
			Image img = new Image(b.getWidth(),b.getHeight(),pageName.getPath());	//ページサイズ
			PDDocument document = new PDDocument();									//ドキュメント		
			PDRectangle rec = new PDRectangle();
			rec.setUpperRightX(0);
			rec.setUpperRightY(0);
			rec.setLowerLeftX(img.width);
			rec.setLowerLeftY(img.height);
			PDPage page = new PDPage(rec);
			
//			System.out.println(img.path + "  --- size ---> ("+ img.width + " , " + img.height + ")");
			
			
			PDImageXObject xImage = PDImageXObject.createFromFile(pageName.getPath(),document);
			PDPageContentStream stream = new PDPageContentStream(document,page);
			stream.drawImage(xImage, 0,0);
				
//			System.out.println( "[成功]: "+ new File(pageName.getPath()).getName());
			
			stream.close();	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
