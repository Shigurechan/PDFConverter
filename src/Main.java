import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main 
{

	
	
	public static void main(String args[]) 
	{	
		Scanner scanner = new Scanner(System.in);	
		List<Converter> dirList = new ArrayList<>();
		List<ConverterPage> pageList = new ArrayList<>();
		
		System.out.println("Ctrl + Z でエンコード開始");
		
	
		
		int i = 0;	//表示用
		while(true)
		{	
	
			if(scanner.hasNextLine() == false)
			{
				break;
			}
			
			
			System.out.print("\n\nDirectory: ");
			String fileName = scanner.nextLine();
			System.out.println(fileName);
			
			File file = new File(fileName);
			
			//System.out.println(file.getName().substring(file.getName().lastIndexOf(".")));
			
			System.out.println(" " + i + " >: " + fileName);
			
			if(file.exists() == true)
			{
				
				if(file.isFile() == true)
				{
					String extension = file.getName().substring(file.getName().lastIndexOf("."));
					
					if(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg"))
					{
						System.out.println("あああ");
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
				System.out.println("ディレクトリまたはファイルが存在しません。");
			}	
			
			i++;
			
			
			
		}
		
		
		if(dirList.size() > 0)
		{
			for(Converter con : dirList)
			{
				con.start();
			}
				
		}
		
		if(pageList.size() > 0)
		{
			for(ConverterPage con : pageList)
			{
				con.start();
			}	
		}
		
		try
		{
			for(Converter con : dirList)
			{
				con.join();
			}	
			
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

