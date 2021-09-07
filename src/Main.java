import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.lang.model.util.ElementScanner14;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/*
 * ######################################################################
 * Mainクラス
 * ###################################################################### 
 */ 


public class Main 
{	
	static final int threadNum = 8;	//スレッド数





	public static void main(String args[]) 
	{	
		Scanner scanner = new Scanner(System.in);		
		ExecutorService pool = Executors.newFixedThreadPool(threadNum);				
		List<File> file = new ArrayList<>();				//ディレクトパス
	

		System.out.println("windows: Ctrl + Z 開始");
		System.out.println("linux:   Ctrl + D 開始\n");

		//ファイル受付
		while(true)
		{			
			System.out.print("\nDirectory > ");			
						
			if(scanner.hasNextLine() == false) { break; }				
			String fileName = scanner.nextLine();

			//ファイルタイプ識別
			FileControl.FileType type = FileControl.GetFileType(new File(fileName));
			if( type == FileControl.FileType.Directory)
			{
				file.add(new File(fileName));	
			}
			else if (type == FileControl.FileType.File)
			{
				//TODO 単一の画像ファイルの時の場合
			}
			else
			{
				System.out.println("未対応ファイルです: " + fileName);
			}
		}
	
		System.out.println("\u001b[?25l");	//カーソル非表示

		int num = 0;
		//ディレクトリ処理
		if(file.size() > 0)
		{
			//System.out.print("\u001b[2J");	//画面クリア

			for(File f : file)
			{
				ConvertDirectory con = new ConvertDirectory(f,pool);
				con.Start();	//処理開始
				num++;
			}
		}
		
		pool.shutdown();
	
		System.out.println();
		//System.out.println("\n\n\n\n\n全部終了");			
		System.out.print("\u001b[?25h");		//カーソル表示
		scanner.close(); 						//scanner close
		System.exit(0);							//終了
	}
}

