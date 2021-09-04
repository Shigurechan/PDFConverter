import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.util.concurrent.Callable;

import java.util.ArrayList;
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

//public class Converter implements Callable<FileControl.ProcessStatus>
public class Converter implements Runnable
{
	
	int threadNumber;					//スレッド番号


	PDDocument document;
	List<Image> convList = new ArrayList<>();	

	//document
	public PDDocument getDocument()
	{
		return document;
	}

	//PDFを生成
	private void GeneratePDF(List<Image> fileList)
	{
		try
		{
			
			List<PDPage> page = new ArrayList<>();	
			
	//		status = FileControl.ProcessStatus.LoadFileImage;

			FileControl.LoadImage(convList);	//画像をロード

	//		status = FileControl.ProcessStatus.GeneratePage;

			ConvertDirectory.status.set(threadNumber,FileControl.ProcessStatus.GeneratePage);	//進行状況

			for(Image image  : fileList)
	    	{							
				PDRectangle rec = new PDRectangle();
				rec.setUpperRightX(0);
				rec.setUpperRightY(0);
				rec.setLowerLeftX(image.width);
				rec.setLowerLeftY(image.height);
				
				//System.out.println(image.path + "Page Generate --- size ---> ("+ image.width + " , " + image.height + ")");
					
				page.add(new PDPage(rec));
				
				document.addPage(page.get(page.size() -1));					
	    	}
			
			//status = FileControl.ProcessStatus.GeneratePage;
			
			ConvertDirectory.status.set(threadNumber,FileControl.ProcessStatus.GeneratePDF);	//進行状況
			for(int i = 0; i < fileList.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(fileList.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);
					
				//System.out.println( "PDF Generate: "+ fileList.get(i).path);
				stream.close();	
	    	}
			
			ConvertDirectory.status.set(threadNumber,FileControl.ProcessStatus.Completion);	//進行状況

			//status = FileControl.ProcessStatus.Completion;
			
			//document.save(filePath.getPath() + "\\" + filePath.getName() + ".pdf");	
			//System.out.println("--->: " + filePath.getPath() + "\\" + filePath.getName() + ".pdf");

			
			//document.close();
						
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//コンストラクタ
	public Converter(List<Image> i,int n) 
	{
		threadNumber = n;
		document = new PDDocument();	
		convList = i;
	}
	
	//実行
	@Override
	public void run()
	{
		GeneratePDF(convList);	//生成
	}
	
}
