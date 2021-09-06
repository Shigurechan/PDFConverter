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

    //処理の進行状況
	public static enum Status
	{
		Start,				//初期開始
		LoadFilePath,		//パスを読み込み
		InputFilePaty,		//パスを設定
		LoadFileImage,		//画像を読み込み
		GeneratePage,		//ページを生成
		GeneratePDF,		//PDFに書き込み
        GeneratePDF_Completion,		    //PDFに書き込み 完了
		FileConcatenation,	//ファイル連結
		SaveFile,			//ファイルを保存
		Completion,			//全て完了

        Invalid //無効なプロセス
	}

    String name;            //ファイル名
    int gauge;              //ゲージ
    Status status;          //状態
    int thread;             //スレッド番号








}
