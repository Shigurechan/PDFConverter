
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
 * ######################################################################
 * ディレクトリ変換
 * ###################################################################### 
 */ 
 
public class ConvertDirectory
{
    static List<Process> status = new ArrayList<>();	//進行状況
	static Process threadMain = new Process(Process.Status.Start,-1);			//メインスレッドで処理
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
	
    public void Start()
    {
		FileControl.GetDirectory(path,image,Main.threadNum);	//パス取得

		for(int i = 0; i < Main.threadNum; i++)
		{
			dirList.add(new Converter(image.get(i),i));
		
		}

		startTime = System.currentTimeMillis();	//デバッグ用　開始時間
		
		//変換
		int k = 0;
		for(Converter con : dirList)
		{

			status.add( new Process(Process.Status.Start,k));	//進行状況			
            pool.submit(con);
			k++;
        }       

		//pool.shutdown();

			
		ProgressBar bar = new ProgressBar();
		while(true)
		{
			if(bar.WaitView(Process.Status.GeneratePDF_Completion) == true)
			{
				break;
			}
		}

		End();


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

			
			float per  = 0;
			int page = 0;	
			float gauge = 0;
			for(int i = 1; i < document.size(); i++)
			{
				page += document.get(i).getNumberOfPages();
			}
			per = 100.0f / (float)page;


			//ファイル連結処理に以降
			for(int i = 0; i< Main.threadNum; i++)
			{
				status.get(i).setProcess(Process.Status.FileConcatenation,0,"");
			}



			for(int i = 1; i < document.size(); i++)
			{
				for(int j = 0; j < document.get(i).getNumberOfPages(); j++)
				{					
					status.get(i).setProcess(Process.Status.FileConcatenation,(int)gauge,Integer.toString(j));	//ファイル連結中
					document.get(0).addPage(document.get(i).getPage(j));

					//System.out.print("[ " + Process.Status.FileConcatenation + " ]" + " > " + (gauge) + "%" );
					
					gauge += per;
					if(gauge > 99.0)
					{
						gauge = 100;
					}

					//System.out.print("\u001b[2K");		//一行削除
					//System.out.print("\u001b[0G");		//一番左に移動
			
				}
			}

//			System.out.print("\u001b[2J");			//画面クリア
			//System.out.println("[ " + Process.Status.SaveFile + " ]");

    		document.get(0).save( path.getAbsolutePath() + ".pdf");   //保存

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
