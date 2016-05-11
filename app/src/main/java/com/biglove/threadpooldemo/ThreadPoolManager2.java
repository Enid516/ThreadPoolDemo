package com.biglove.threadpooldemo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by enid on 2016/2/27.
 */
public class ThreadPoolManager2 {
    private static ThreadPoolManager2 mInstance;
    private ExecutorService mThreadPool;
    private List<Runnable> mList;
    private ThreadProgressInterface mProgressInterface;

    private ThreadPoolManager2(){
        mThreadPool = Executors.newFixedThreadPool(10);
    }

    public static ThreadPoolManager2 Instance(){
        if (mInstance == null){
            mInstance = new ThreadPoolManager2();
        }
        return mInstance;
    }

    public void addThreadToPool(List<Runnable> list){
        this.mList = list;
    }

    public void start(){
        if (mList == null){
            mProgressInterface.onThreadEnd();
            return;
        }
        mProgressInterface.onThreadStart();

        for (Runnable runnable: mList) {
            if (!mThreadPool.isShutdown())
                mThreadPool.execute(runnable);
        }

        mThreadPool.shutdown();
        System.out.println("终止线程池");
        while(true){
            if (mThreadPool.isTerminated()) {
                    mProgressInterface.onThreadEnd();
                break;
            }
        }
    }


    public void setProgressInterface(ThreadProgressInterface progressInterface){
        this.mProgressInterface = progressInterface;
    }

    public interface ThreadProgressInterface{
        public void onThreadStart();
        public void onThreadEnd();
    }

}
