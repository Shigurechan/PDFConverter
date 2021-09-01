
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
 * ディレクトリのPDFを生成 
 * ##################################################################*/



public class Converter extends Thread
{
	File fileName;	
	
	
	private void GetDirectory(File dir,List<Image> list)
	{
		
		
		
		List<String> strList = new ArrayList<>();
		
		
		
		for(int i = 0; i < dir.listFiles().length; i++)
		{
			strList.add(dir.listFiles()[i].getPath());
		}
		
		Collections.sort(strList);	
				
		int i = 0;
		for(; i < dir.listFiles().length; i++)
		{				
			boolean w = false;
			try 
			{
				BufferedImage b  = ImageIO.read(new File(strList.get(i)));
				Image img = new Image(b.getWidth(),b.getHeight(),strList.get(i));
						
				list.add(img);
				
				b = null;
			} 
			catch(NullPointerException e)
			{
				
				w = true;
				
				
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
			
			if ( w == false)
			{
				//System.out.println(list.get(list.size() - 1).path);
			}
			
		}	
		
		
		
		
		
		System.out.println();
		//System.out.println();
	}
	
	
	
	private void GeneratePDF(List<Image> list,List<String> pathList,File fileName)
	{
		try
		{
			PDDocument document = new PDDocument();	
			List<PDPage> page = new ArrayList<>();	
			
			
			
			
			for(Image image  : list)
	    	{							
				PDRectangle rec = new PDRectangle();
				rec.setUpperRightX(0);
				rec.setUpperRightY(0);
				rec.setLowerLeftX(image.width);
				rec.setLowerLeftY(image.height);
				
				System.out.println(image.path + "  --- size ---> ("+ image.width + " , " + image.height + ")");
					
				page.add(new PDPage(rec));
				document.addPage(page.get(page.size() -1));					
	    	}
			
		
			for(int i = 0; i < list.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(list.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);
					
				System.out.println( "PDF generation: "+ list.get(i).path);
				stream.close();	
	    	}
			
			
			document.save(fileName.getPath() + "\\" + fileName.getName() + ".pdf");	
			System.out.println("--->: " + fileName.getPath() + "\\" + fileName.getName() + ".pdf");

			
			document.close();
						
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public Converter(String filePath) 
	{				
		fileName = new File(filePath);
	}
	
	@Override
	public void run()
	{
		List<Image> imageList  = new ArrayList<>();
		List<String> fileList = new ArrayList<>();
			
		System.out.println();
		GetDirectory(fileName,imageList);					 	
		GeneratePDF(imageList,fileList,fileName);							
	}
	
}
