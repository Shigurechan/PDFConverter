
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
    static List<Process> status = new ArrayList<>();							//進行状況
	static Process threadMain = new Process(Process.Status.Start,-1);			//メインスレッドで処理
	static String dirName;														//ディレクトリ名
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
		dirName = path.getName();	//ディレクトリ名
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

		End();	//終了処理


	}


    
    public void End()
    {
        
		try
		{
			ProgressBar b = new ProgressBar();
			//進行状況

			threadMain.setProcess(Process.Status.FileConcatenation,(int)0,"");
			List<PDDocument> document = new ArrayList<>();
			for(Converter d : dirList)
			{	
				document.add(d.getDocument());
			}

			// ファイル連結
			float per  = 0;
			int page = 0;	
			float gauge = 0;
			for(int i = 1; i < document.size(); i++)
			{
				page += document.get(i).getNumberOfPages();
			}
			per = 100.0f / (float)page;


			//ファイル連結処理に移行
		

			//進行状況			
			for(int i = 1; i < document.size(); i++)
			{
				for(int j = 0; j < document.get(i).getNumberOfPages(); j++)
				{					
					
					document.get(0).addPage(document.get(i).getPage(j));
					
					//進行状況
					threadMain.setProcess(Process.Status.FileConcatenation,(int)gauge,"");
					b.ThreadMainView("");

					gauge += per;
					if(gauge > 99.0){ gauge = 100; }
				}
			}
			
			//System.out.println();


					
			//進行状況
			threadMain.setProcess(Process.Status.SaveFile,-1,"");
			b.ThreadMainView("");
	
    		document.get(0).save( path.getAbsolutePath() + ".pdf");   //保存
			

            //document.close
            for(Converter d : dirList)
            {	
                d.getDocument().close();
            }
            
			//進行状況
			threadMain.setProcess(Process.Status.Completion,-1,"");

			System.out.print("\u001b[0G");		    //一番左に移動
			System.out.print("\u001b[2K");		    //一行削除 
			System.out.print("[ " + ConvertDirectory.threadMain.getStatus() +" ] " + "Path: " + path.getAbsolutePath() + ".pdf");
			
			//static 変数をリセット
			status.clear();
			threadMain.setProcess(Process.Status.Start, 0, "");

		}
		catch(IOException e)
		{
			e.printStackTrace();
        }
        
		endTime = System.currentTimeMillis();	//デバッグ用　終了時間
        


		//System.out.println("\n処理時間: " + (endTime - startTime));
    }
}
