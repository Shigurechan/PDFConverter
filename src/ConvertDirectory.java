
import java.io.IOException;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;



/*
* ###############################
* ディレクトリを変換
* ###############################
*/

public class ConvertDirectory
{
    static List<FileControl.ProcessStatus> status = new ArrayList<>();	//進行状況
	static int a;

    File path;      //ディレクトリパス
	List<Converter> dirList = new ArrayList<>();		//変換		
    List<List<Image>> image  = new ArrayList<>();       //画像
	
    ExecutorService pool;   //スレッドプール

    //デバッグ用　処理時間
    long startTime; //開始タイム
    long endTime;   //終了タイム



    //コンストラクタ
    public ConvertDirectory(File p,ExecutorService es)
    {
        path = p;
        
        pool = es;
    }

	public void Status()
	{
		
	}



    
    public void Start()
    {
		FileControl.GetDirectory(path,image,Main.threadNum);	//パス取得

		for(int i = 0; i < Main.threadNum; i++)
		{
			dirList.add(new Converter(image.get(i),i));
		
		}
		startTime = System.currentTimeMillis();	//デバッグ用　開始時間
		
		//変換
		for(Converter con : dirList)
		{

			status.add(FileControl.ProcessStatus.Start);
			
            pool.submit(con);
			 
        }
        

	}


    
    public void End()
    {
        
        
		try
		{
			List<PDDocument> document = new ArrayList<>();
			for(Converter d : dirList)
			{	
				document.add(d.getDocument());
			}

			
			//進行状況
			for(int i = 0; i< Main.threadNum; i++)
			{
				status.set(i,FileControl.ProcessStatus.FileConcatenation);
			}


			for(int i = 1; i < document.size(); i++)
			{
				for(int j = 0; j < document.get(i).getNumberOfPages(); j++)
				{
					document.get(0).addPage(document.get(i).getPage(j));
				}
			}

            
			//進行状況
			for(int i = 0; i< Main.threadNum; i++)
			{
				status.set(i,FileControl.ProcessStatus.SaveFile);
			}

    		document.get(0).save( path.getAbsolutePath() + ".pdf");   //保存

            //document.close
            for(Converter d : dirList)
            {	
                d.getDocument().close();
            }
            

			//進行状況
			for(int i = 0; i< Main.threadNum; i++)
			{
				status.set(i,FileControl.ProcessStatus.Completion);
			}



		}
		catch(IOException e)
		{
			e.printStackTrace();
        }
        
		endTime = System.currentTimeMillis();	//デバッグ用　終了時間
        
		System.out.println("処理時間: " + (endTime - startTime));
    }
}