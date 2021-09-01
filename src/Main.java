import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main 
{	
	public static void main(String args[]) 
	{	
		Scanner scanner = new Scanner(System.in);	
		List<Converter> dirList = new ArrayList<>();		//ディレクトリ
		List<ConverterPage> pageList = new ArrayList<>();	//ページ
			
		System.out.println("windows: Cntrl + z 開始");
		System.out.println("linux: Cntrl + D 開始\n");
		
		
		int i = 0;	//表示用
		while(true)
		{
			
			System.out.print("\nDirectory　or File > ");
			
			if(scanner.hasNextLine() == false)
			{
				break;
			}
			
			

			String fileName = scanner.nextLine();
			System.out.println(fileName);
			
			File file = new File(fileName);
			
			//System.out.println(file.getName().substring(file.getName().lastIndexOf(".")));
			
			System.out.println(" " + i + " >: " + fileName);
			
			//ファイルかディレクトリかを選別
			if(file.exists() == true)
			{				
				if(file.isFile() == true)
				{
					String extension = file.getName().substring(file.getName().lastIndexOf("."));
					
					if(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg"))
					{					
						pageList.add(new ConverterPage(fileName));
					}	
				}
				else
				{	
					dirList.add(new Converter(fileName));
				}
			}
			else
			{
				System.out.println("対応形式ではりません: " + file.getName());
			}	
			
			i++;	
		}
		
		
		//ディレクトリ
		if(dirList.size() > 0)
		{
			for(Converter con : dirList)
			{
				con.start();
			}
			
		}
		
		//ファイル
		if(pageList.size() > 0)
		{
			for(ConverterPage con : pageList)
			{
				con.start();
			}	
		}
		
		long startTime = System.currentTimeMillis();	//開始時間
		
		while(true)
		{
			boolean page = true;
			boolean dir = true;
			//待機
			for(Converter con : dirList)
			{
				if(con.isAlive() == false)
				{
					dir = false;
				}
				else
				{
					dir = true;
				}
			}	
			
			//待機
			for(ConverterPage con : pageList)
			{
				if(con.isAlive() == false)
				{
					page = false;
				}
				else
				{
					page = true;
				}
			}	
			
			if((page == false) && (dir == false))
			{
				break;
			}
			
			
		}
		
		scanner.close(); //scanner close
		
		long endTime = System.currentTimeMillis();	//終了時間
		System.out.println("終了: " + (endTime - startTime));
	}
}

