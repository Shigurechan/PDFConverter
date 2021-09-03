import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/* ##################################################################
 * PDF 生成 ディレクトリ
 * ##################################################################*/

public class Converter implements Runnable
//public class Converter implements Runnable
{
	File filePath;	
	PDDocument document;
	List<Image> convList = new ArrayList<>();	

	public PDDocument getDocument()
	{
		return document;
	}

	private void GeneratePDF(List<Image> fileList)
	{
		try
		{
			
			List<PDPage> page = new ArrayList<>();	
			
			
			FileControl.LoadImage(convList);	//画像をロード


			
			for(Image image  : fileList)
	    	{							
				PDRectangle rec = new PDRectangle();
				rec.setUpperRightX(0);
				rec.setUpperRightY(0);
				rec.setLowerLeftX(image.width);
				rec.setLowerLeftY(image.height);
				
				System.out.println(image.path + "Page Generate --- size ---> ("+ image.width + " , " + image.height + ")");
					
				page.add(new PDPage(rec));
				
				document.addPage(page.get(page.size() -1));					
	    	}
			
		
			for(int i = 0; i < fileList.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(fileList.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);
					
				System.out.println( "PDF Generate: "+ fileList.get(i).path);
				stream.close();	
	    	}
			
			
			//document.save(filePath.getPath() + "\\" + filePath.getName() + ".pdf");	
			//System.out.println("--->: " + filePath.getPath() + "\\" + filePath.getName() + ".pdf");

			
			//document.close();
						
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public Converter(List<Image> a) 
	{				
		document = new PDDocument();	
		//filePath = path;

		convList = a;
	}
	
	@Override
	public void run()
	{
		GeneratePDF(convList);	//生成

	}
	
}
