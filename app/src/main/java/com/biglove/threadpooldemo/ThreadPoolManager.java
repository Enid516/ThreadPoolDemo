package com.biglove.threadpooldemo;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by enid on 2016/2/27.
 */
public class ThreadPoolManager {
    private static ThreadPoolManager mInstance;
    private List<Runnable> mList;
    private ThreadPoolExecutor mExecutor;
    private ThreadProgressInterface mProgressInterface;

    private ThreadPoolManager(){
        mExecutor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
    }

    public static ThreadPoolManager Instance(){
        if (mInstance == null){
            mInstance = new ThreadPoolManager();
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
            if (!mExecutor.isShutdown())
                mExecutor.execute(runnable);
        }

        while(true){
            if (mExecutor.getCompletedTaskCount() == mList.size()) {
                mExecutor.shutdown();//shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
                System.out.println("终止线程池");
                threadTerminated();
                break;
            }
        }
    }

    /**终止线程池后，判断线程池是否终止*/
    public void threadTerminated() {
        while (true){
            if (mExecutor.isTerminated()){
                break;
            }
        }

        if (mProgressInterface != null)
        mProgressInterface.onThreadEnd();
    }


    public void setProgressInterface(ThreadProgressInterface progressInterface){
        this.mProgressInterface = progressInterface;
    }
    public interface ThreadProgressInterface{
        public void onThreadStart();
        public void onThreadEnd();
    }

}
