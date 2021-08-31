
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;





public class Converter extends Thread
{
	File fileName;	//ディレクトリパス
	
	
	//ファイルの画像パスを取得
	private void GetDirectory(File dir,List<Image> list)
	{
		
		System.out.println("==================== ファイル読み込み　==================== ");
		
		int i = 0;
		for(; i < dir.listFiles().length; i++)
		{				
			boolean w = false;
			try 
			{
				BufferedImage b  = ImageIO.read(dir.listFiles()[i]);
				Image img = new Image(b.getWidth(),b.getHeight(),dir.listFiles()[i].getPath());
						
				list.add(img);
				
				b = null;
			} 
			catch(NullPointerException e)
			{
				
				w = true;
				System.out.println("###### Warning: 未対応のファイル形式です: " + dir.listFiles()[i].getPath() + " ######");
				
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
			
			if ( w == false)
			{
				System.out.println(list.get(list.size() - 1).path);
			}
			//System.out.println("###################################");
			//System.out.println("Width: " + list.get(list.size() -1).width);
			//System.out.println("Height: " + list.get(list.size() -1).height);
			//System.out.println("Path: " + list.get(list.size() -1).path);
			//System.out.println("###################################\n");
		}	
		
		System.out.println("ページ数: " + i);
		
		
		
		System.out.println();
		//System.out.println();
	}
	
	
	//PDFファイルを生成
	private void GeneratePDF(List<Image> list,List<String> pathList,File fileName)
	{
		try
		{
			PDDocument document = new PDDocument();	//ドキュメント
			List<PDPage> page = new ArrayList<>();	//ページ
			
			System.out.println("==================== ページ生成　==================== ");	//画面表示
			
			//ページ生成
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
			
			System.out.println("\n==================== 書き込み ====================");
			
			//画像焼き付け 
			for(int i = 0; i < list.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(list.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);
					
				System.out.println( "[成功]: "+ new File(list.get(i).path).getName());
				stream.close();	
	    	}
			
			System.out.println("==================== PDF生成中 ====================\n");
			document.save(fileName.getParent() + "\\" + fileName.getName() + ".pdf");	
			System.out.println("--->: " + fileName.getParent() + "\\" + fileName.getName() + ".pdf");
			System.out.println("\n==================== 完了 ====================");
			
			document.close();
						
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//コンストラクタ
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
		GetDirectory(fileName,imageList);					//画像パス読み込み 	
		GeneratePDF(imageList,fileList,fileName);			//PDFファイルを生成				
	}
	
	
	
}
