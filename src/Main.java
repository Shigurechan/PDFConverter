import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class Main 
{	
	
	public static void main(String args[]) 
	{	
		final int threadNum = 8;	//スレッド数


		Scanner scanner = new Scanner(System.in);		
		while(true)
		{
			

			System.out.println("windows: Ctrl + Z 開始");
			System.out.println("linux:   Ctrl + D 開始\n");
			
			List<File> file = new ArrayList<>();				//ディレクトパス
			List<ConvertDirectory> dirList = new ArrayList<>();	//変換ディレクトリ

			while(true)
			{			
				System.out.print("\nDirectory　or File > ");			
							
				if(scanner.hasNextLine() == false) { break; }				
				String fileName = scanner.nextLine();
			
				file.add(new File(fileName));	
			}
			
			ExecutorService pool = Executors.newFixedThreadPool(threadNum);

			for(File f : file)
			{
				dirList.add(new ConvertDirectory(f,threadNum,pool));
			}
			
					
			//処理を開始
			for(ConvertDirectory c : dirList)
			{
				c.Start();        
			}


			//スレッドを閉じる
			try
			{
				pool.shutdown();	//スレッドプールを閉じる
				pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);            	
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			//終了処理
			for(ConvertDirectory c : dirList)
			{
				c.End();        
			}

			System.out.println("\n\n\n\n\n全部終了");		

		}	

		scanner.close(); 		//scanner close
	}
}

