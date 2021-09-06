
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* ########################################## 
 * # ファイル操作クラス
 * ##########################################*/


public class FileControl
{




	//ファイルタイプ
	public enum FileType
	{
		Directory,	//ディレクトリ
		File,		//単一の画像ファイル
		Invalid		//対応していないファイル
	}


	//ディレクトリを取得
	public static void GetDirectory(File dirName,List<List<Image>> outList,int splitNum)
	{
		List<String> strList = new ArrayList<>();

		ProgressBar bar = new ProgressBar();

		//System.out.println("ああああ　"+ dirName.listFiles().length);
		//パスをロード
		float per = 100.0f / dirName.listFiles().length;
		float p = 0;
		for(int i = 0; i < dirName.listFiles().length; i++)
		{
			ConvertDirectory.threadMain.setProcess(Process.Status.LoadFilePath,(int)p,"");	//進行状況

			strList.add(dirName.listFiles()[i].getPath());
//			System.out.println("Load FilePath: " + dirName.listFiles()[i].getPath());	//デバッグ
			p += per;
		}
		
		Collections.sort(strList);	//順番にソート	

		//パスを設定
		int num = strList.size() / splitNum;		//分割の枚数
		int notMuch = strList.size() % splitNum;	//余り

		

		if(	(float)(strList.size() % splitNum) > 0.0f )
		{
			for(int i = 0; i< splitNum; i++)
			{
				outList.add(new ArrayList<>());

				if( i == (splitNum - 1) )
				{
					for(int j = 0; j < num + notMuch; j++)
					{
						outList.get(outList.size() - 1).add(new Image(0,0,strList.get(num * i + j)));


						ConvertDirectory.threadMain.setProcess(Process.Status.InputFilePath,(int)p,"");	//進行状況
//						System.out.println("Input FilePath: " + strList.get(num * i + j));	//デバッグ
						bar.View(,strList.get(num * i + j));
					}									
				}
				else
				{
					for(int j = 0; j < num; j++)
					{
						outList.get(outList.size() - 1).add(new Image(0,0,strList.get(num * i + j)));
//						System.out.println("Input FilePath: " + strList.get(num * i + j));	//デバッグ

						bar.View(strList.get(num * i + j));
						ConvertDirectory.threadMain.setProcess(Process.Status.InputFilePath,(int)p,"");	//進行状況
					}				
				}
			}	
		}
		else
		{
			for(int i = 0; i< splitNum; i++)
			{
				outList.add(new ArrayList<>());

				for(int j = 0; j < num; j++)
				{
					outList.get(outList.size() - 1).add(new Image(0,0,strList.get(num * i + j)));
//					System.out.println("Input FilePath: " + strList.get(num * i + j));	//デバッグ

					ConvertDirectory.threadMain.setProcess(Process.Status.InputFilePath,(int)p,"");	//進行状況
					bar.View(strList.get(num * i + j));
				}								
			}
		}

		

	}

	//画像をロード
	public static void LoadImage(List<Image> outList)
	{
		for(int i = 0; i < outList.size(); i++)
		{					
			try 
			{
				BufferedImage b  = ImageIO.read(new File(outList.get(i).path));							
//				System.out.println("LoadFileImage: " + outList.get(i).path +  " --- size ---> ("+ b.getWidth() + " , " + b.getHeight() + ")");	//デバッグ
				outList.get(i).width = b.getWidth();
				outList.get(i).height = b.getHeight();
					
				b = null;
			} 
			catch(NullPointerException e)
			{
							
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
				
		}							
	}



	//ファイルタイプを取得　
	public static FileType GetFileType(File file)
	{
		//ファイルかディレクトリかを選別
		if(file.exists() == true)
		{				
			if(file.isFile() == true)
			{
				String extension = file.getName().substring(file.getName().lastIndexOf("."));
				
				if(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg"))
				{					
					return FileType.File;
				}	
			}
			else
			{	
				return FileType.Directory;
			}
		}
		else
		{
			//System.out.println("対応形式ではりません: " + file.getName());
			return FileType.Invalid;
		}	
		
		return FileType.Invalid;
	}
}	