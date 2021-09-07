
import java.util.Arrays;

/*
 * ######################################################################
 * プログレスバー
 * ###################################################################### 
 */ 

public class ProgressBar
{
    //コンストラクタ
    public ProgressBar()
    {

    }

    //指定した進行状況まで表示 指定した進行状況： true
    public boolean WaitView(Process.Status st)
    {
        if(false == IsStatus(st))
        {
            //System.out.print("\u001b[2J");	    //画面クリア
            System.out.print("\u001b[0G");		    //一番左に移動
            System.out.print("\u001b[2K");		    //一行削除
            //System.out.print("\u001b[1;0H");		//一番上の左に移動
            if(NowStatusLate() != Process.Status.Start)          
            {
                System.out.print("[ " + NowStatusLate()+" ]  ");
                System.out.print(" " + ConvertDirectory.dirName);
                System.out.print(" " + PrograssPercentage()+" %");


            }

            return false;
        }
        else
        {
            return true;
        }

    }






    //進行状況を表示
    public void ThreadMainView(String str)
    {

        //System.out.print("\u001b[2J");	    //画面クリア
        System.out.print("\u001b[0G");		    //一番左に移動
        System.out.print("\u001b[2K");		    //一行削除
        //System.out.print("\u001b[1;0H");		//一番上の左に移動

        //System.out.print("[ " + str +" ]  ");
        System.out.print("[ " + ConvertDirectory.threadMain.getStatus() +" ] ");
        if(ConvertDirectory.threadMain.getGauge() >= 0)
        {
            System.out.print(ConvertDirectory.threadMain.getGauge()+" %");
        }
        System.out.print(" " + ConvertDirectory.dirName);

    }




    //すべてのプロセスが同じ進行状況か確認
    public boolean IsStatus(Process.Status st)
    {
        boolean b[] = new boolean[ConvertDirectory.status.size()];

        int i = 0;
        for(Process p : ConvertDirectory.status)
        {
            if(p.getStatus() == st)
            {
                b[i] = true;
            }else
            {
                b[i] = false;
                
            }

            i++;
        }

        for(boolean a : b)
        {
            if(a == false)
            {
                return false;
            }
        }

        return true;
    }





    //すべてのプロセスの状況 一番先のやつを返す
    public Process.Status NowStatusLate()
    {
        int a[] = new int[ConvertDirectory.status.size()];

        int i = 0;
        for(Process s : ConvertDirectory.status)
        {            
            a[i] = s.getStatus().ordinal();
          //  System.out.println(s.getStatus().ordinal());
          //  System.out.println(a[i]);

            i++;
        }

        Arrays.sort(a);
        //System.out.println("ううう " + Process.Status.ValueInt(a[0]));
        return Process.Status.ValueInt(a[0]);
    }
    





    //すべてのプロセスの状況 一番先のやつを返す
    public Process.Status NowStatus()
    {
        

        //Process.Status st[] = new Process.Status[Process.Status.values().length];
        int a = 0;
        Process.Status strr = Process.Status.Start;
        for(Process s : ConvertDirectory.status)
        {            
            if(s.getStatus().ordinal() > a )
            {
                a = s.getStatus().ordinal();
                strr = s.getStatus();                
            }
        }

        return strr;
    }
        

    //全スレッドの処理の進行状況
    public int PrograssPercentage()
    {
        int percent = 0;
        for(Process p : ConvertDirectory.status)
        {
            percent += p.getGauge(); 
        }

        percent = percent / Main.threadNum;

        return percent;

    }



}