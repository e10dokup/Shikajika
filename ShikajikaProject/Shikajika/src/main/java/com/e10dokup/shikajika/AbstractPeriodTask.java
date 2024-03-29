package com.e10dokup.shikajika;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractPeriodTask {

    private long period;
    private boolean isDaemon;
    private boolean isCancelled = true;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    /**
     * periodミリ秒の周期で動かす
     * @param period 周期ミリ秒
     * @param isDaemon 定期処理を行うスレッドをデーモンスレッドで作成するかどうか（false=ユーザースレッド）
     */
    public AbstractPeriodTask(long period, boolean isDaemon) {
        handler = new Handler();
        this.period = period;
        this.isDaemon = isDaemon;
    }

    /**
     * periodミリ秒の周期で動かす。タイマースレッドはユーザースレッドで作成される
     * @param period 周期ミリ秒
     */
    public AbstractPeriodTask(long period){
        this(period,false);
    }

    /**
     * 周期タスクの実行を開始する
     */
    public void execute(){

        if(!isCancelled){
            //isCancelledがfalse（=実行中）ならばこのメソッドは実行しない
            return;
        }

        //timerをキャンセルした場合、timer,timerTaskは破棄されるので都度作り直す
        timerTask = new TimerTask(){
            @Override
            public void run() {
                preInvokersMethod();
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        invokersMethod();
                    }
                });
                postInvokersMethod();
            }
        };
        timer = new Timer(isDaemon);
        timer.scheduleAtFixedRate(timerTask, period, period);

    }

    /**
     * 周期タスクの実行をキャンセルする
     */
    public void cancel(){
        if(timer==null || timerTask==null){
            return;
        }
        timer.cancel();
        timer = null;
        isCancelled = true;
    }

    /**
     * 本インスタンスを作成したスレッド(例えばUIスレッド）で処理させるメソッド
     */
    abstract protected void invokersMethod();

    /**
     * タイマースレッドで処理させるメソッドでinvokersMethodの直前に呼ばれる。
     */
    protected void preInvokersMethod(){
    }

    /**
     * タイマースレッドで処理させるメソッドでinvokersMethodの直後に呼ばれる。
     */
    protected void postInvokersMethod(){
    }

}