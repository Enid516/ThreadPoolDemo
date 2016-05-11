package com.biglove.threadpooldemo;

/**
 * Created by enid on 2016/2/27.
 */
public class TestTask implements Runnable{
    private int mTaskNum;
    public TestTask(int num){
        this.mTaskNum = num;
    }

    @Override
    public void run() {
        doSomething();
    }

    private void doSomething(){
        System.out.println("正在执行task:" + mTaskNum);
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行task"+mTaskNum+"结束");
    }
}
