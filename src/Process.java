import java.util.AbstractCollection;
import java.util.EnumSet;

/*
 * ######################################################################
 * 処理の進行状況を管理
 * ###################################################################### 
 */ 



public class Process
{
    

    //コンストラクタ
    public Process(Status s,int th)
    {
        status = s;
        thread = th;
    }
    
    //現在の処理状況
    public void setProcess(Status s,int g,String n)
    {
        status = s;
        gauge = g;
        name = n;
    }

    //処理状況を返す
    public Status getStatus()
    {
        return status;
    }


    //％を返す
    public int getGauge()
    {
        return gauge;
    }

    //スレッド番号
    public int getThreadNumber()
    {
        return thread;
    }




    //処理の進行状況
	public static enum Status
	{
		Start,				//初期開始
		LoadFilePath,		            //パスを読み込み
		LoadFilePath_Completion,		//パスを読み込み 完了

		InputFilePath,		            //パスを設定
		InputFilePath_Completion,		//パスを設定 完了

		LoadFileImage,		            //画像を読み込み
		LoadFileImage_Completion,		//画像を読み込み 完了

		GeneratePage,		            //ページを生成
        GeneratePage_Completion,        //ページを生成 完了

		GeneratePDF,		            //PDFに書き込み 
        GeneratePDF_Completion,		    //PDFに書き込み 完了

		FileConcatenation,	            //ファイル連結
        FileConcatenation_Completion,	//ファイル連結 完了
        
		SaveFile,			            //ファイルを保存
		SaveFile_Completion,			//ファイルを保存 完了

		Completion,			//全て完了

        Invalid; //無効なプロセス


        public static Status ValueInt(final int value) 
        {

            for(Status m : EnumSet.allOf(Status.class)) 
            {
                if(m.ordinal() == value) 
                {
                    return m;
                }
            }

            return Invalid;
        }

	}

    String name;            //ファイル名
    int gauge;              //ゲージ
    Status status;          //状態
    int thread;             //スレッド番号

}
