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

//public class Converter implements Callable<Process>
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
			FileControl.LoadImage(convList,threadNumber);	//画像をロード
			//ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.LoadFileImage_Completion,(int)100,"");	//進行状況
//			System.out.println();

			float per = 100.0f / fileList.size();
			float gauge = 0;		
			for(Image image  : fileList)
	    	{							
				PDRectangle rec = new PDRectangle();
				rec.setUpperRightX(0);
				rec.setUpperRightY(0);
				rec.setLowerLeftX(image.width);
				rec.setLowerLeftY(image.height);
					
				page.add(new PDPage(rec));
			
				ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.GeneratePage,(int)gauge,"");	//進行状況				
				document.addPage(page.get(page.size() -1));					
				gauge += per;
	    	}
						
			//ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.GeneratePage_Completion,(int)gauge,"");	//進行状況



			//PDFページに焼付け
			per = 100.0f / fileList.size();
			gauge = 0;
			for(int i = 0; i < fileList.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(fileList.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);

				ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.GeneratePDF,(int)gauge,new File(fileList.get(i).path).getName());	//進行状況

				stream.close();	
				gauge += per;
	    	}
			
			ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.GeneratePDF_Completion,100,"");	//進行状況

						
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

		ConvertDirectory.status.get(threadNumber).setProcess(Process.Status.GeneratePDF_Completion,100,"");	//進行状況
	}
	
}
