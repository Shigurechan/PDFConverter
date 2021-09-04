import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class Main 
{	
	static final int threadNum = 8;	//スレッド数
	public static void main(String args[]) 
	{	
		Scanner scanner = new Scanner(System.in);		
	
		System.out.println("windows: Ctrl + Z 開始");
		System.out.println("linux:   Ctrl + D 開始\n");
		
		List<File> file = new ArrayList<>();				//ディレクトパス
	
		while(true)
		{			
			System.out.print("\nDirectory > ");			
						
			if(scanner.hasNextLine() == false) { break; }				
			String fileName = scanner.nextLine();
		
			file.add(new File(fileName));	
		}
		

		int i = 0;
		
		for(FileControl.ProcessStatus s : ConvertDirectory.status)
		{
			System.out.println("[ " + i + " ]: " + s);
			i++;
		}


		if(file.size() > 0)
		{
			for(File f : file)
			{
				ExecutorService pool = Executors.newFixedThreadPool(threadNum);				
				ConvertDirectory con = new ConvertDirectory(f,pool);

				con.Start();
				try
				{
					pool.shutdown();	//スレッドプールを閉じる
					
					while(true)
					{
						boolean b = false;
						int i = 0;
						for(FileControl.ProcessStatus s : ConvertDirectory.status)
						{
							if(s == FileControl.ProcessStatus.Completion)
							{
								b = true;
							}
							else
							{
								b = false;
							}

							//System.out.println("[ " + i + " ]: " + s);

							i++;
						}

						if(b == true)
						{
							break;
						}
						

						//System.out.println("\033[2J");
						//System.out.println("\033[5A");

					}

					pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);	//待機
					
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
		
				con.End();
			}
		}
		
		System.out.println("\n\n\n\n\n全部終了");		
			
		scanner.close(); 		//scanner close
	}
}

