

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class Main 
{
	//�摜�N���X
	static class Image
	{
		//�R���X�g���N�^
		public Image(int w,int h,String n)
		{
			width = w;
			height = h;
			path = n;
		}
		
		
		public int width;	//��
		public int height;	//�c
		public String path;	//�p�X
	}


	
	
	//�t�@�C���̉摜�p�X���擾
	static public void GetDirectory(File dir,List<Image> list)
	{
		for(int i = 0; i < dir.listFiles().length; i++)
		{
			
			try 
			{
				BufferedImage b  = ImageIO.read(dir.listFiles()[i]);
				Image img = new Image(b.getWidth(),b.getHeight(),dir.listFiles()[i].getPath());
						
				list.add(img);
				
				b = null;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			System.out.println(list.get(list.size() - 1).path);
			//System.out.println("###################################");
			//System.out.println("Width: " + list.get(list.size() -1).width);
			//System.out.println("Height: " + list.get(list.size() -1).height);
			//System.out.println("Path: " + list.get(list.size() -1).path);
			//System.out.println("###################################\n");
		}	
		
		System.out.println();
		System.out.println();
	}
	
	//PDF�t�@�C���𐶐�
	static void GeneratePDF(List<Image> list,List<String> pathList,File fileName)
	{
		try
		{
			PDDocument document = new PDDocument();	//�h�L�������g
			List<PDPage> page = new ArrayList<>();	//�y�[�W
			
			for(Image image  : list)
	    	{
				
					
				PDRectangle rec = new PDRectangle();
				rec.setUpperRightX(0);
				rec.setUpperRightY(0);
				rec.setLowerLeftX(image.width);
				rec.setLowerLeftY(image.height);
				
				page.add(new PDPage(rec));
				document.addPage(page.get(page.size() -1));				
	    	}
			
			
			 
			for(int i = 0; i < list.size(); i++)
	    	{
				
				PDImageXObject xImage = PDImageXObject.createFromFile(list.get(i).path,document);
				PDPageContentStream stream = new PDPageContentStream(document,page.get(i));
				stream.drawImage(xImage, 0,0);
					
				System.out.println( "[����]: "+ new File(list.get(i).path).getName());
				stream.close();	
	    	}
			
			
						
			document.save(fileName.getParent() + "\\" + fileName.getName() + ".pdf");	
			
			System.out.println("==================== ���� ====================");
			System.out.println(">: " + fileName.getParent() + "\\" + fileName.getName() + ".pdf");
			document.close();
			
			
		
			
			
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) 
	{
		Scanner scanner = new Scanner(System.in);
		List<Image> imageList  = new ArrayList<>();
	  
		System.out.print("Directory: ");
		String fileName = scanner.next();
		File f = new File(fileName);
	  
		GetDirectory(f,imageList);	//�摜�p�X�ǂݍ���
	  
	  
		List<String> fileList = new ArrayList<>();
		GeneratePDF(imageList,fileList,f);	//PDF�t�@�C���𐶐�
	  
	  
	  
	  
		scanner.close();
	  
	}
}

