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
			
		int i = 0;	//表示用
		while(true)
		{
			
			System.out.print("\n\nDirectory　or File > ");
			
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
		
		try
		{
			//待機
			for(Converter con : dirList)
			{
				con.join();
			}	
			
			//待機
			for(ConverterPage con : pageList)
			{
				con.join();
			}	
			
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		scanner.close(); //scanner close
	
		
		System.out.println("終了");
	}
}

