
import java.io.IOException;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    File path;      //ディレクトリパス
    int threadNum;  //スレッド数
    List<Converter> dirList = new ArrayList<>();		//変換		
    List<List<Image>> image  = new ArrayList<>();       //画像
	
    ExecutorService pool;   //スレッドプール

    //デバッグ用　処理時間
    long startTime; //開始タイム
    long endTime;   //終了タイム



    //コンストラクタ
    public ConvertDirectory(File p,int t,ExecutorService es)
    {
        path = p;
        threadNum = t;
        pool = es;
    }


    
    public void Start()
    {
        
		FileControl.GetDirectory(path,image,threadNum);	//パス取得

		for(int i = 0; i < threadNum; i++)
		{
			dirList.add(new Converter(image.get(i)));
		}

		startTime = System.currentTimeMillis();	//デバッグ用　開始時間

		//変換
		for(Converter con : dirList)
		{
            pool.submit(con);
        }
        
        
    }


    //Join
    public void End()
    {
        
        
		try
		{
			List<PDDocument> document = new ArrayList<>();
			for(Converter d : dirList)
			{	
				document.add(d.getDocument());
			}


			for(int i = 1; i < document.size(); i++)
			{
				for(int j = 0; j < document.get(i).getNumberOfPages(); j++)
				{
					document.get(0).addPage(document.get(i).getPage(j));
				}
			}

            
    		document.get(0).save("test.pdf");   //保存



            //document.close
            for(Converter d : dirList)
            {	
                d.getDocument().close();
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
