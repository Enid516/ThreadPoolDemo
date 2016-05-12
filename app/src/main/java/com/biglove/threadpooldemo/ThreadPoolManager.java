package com.biglove.threadpooldemo;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by enid on 2016/2/27.
 */
public class ThreadPoolManager {
    private static final String TAG = "ThreadPoolManager";
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 200;
    private static final int FIXED_CAPACITY_DEFAULT = 5;
    private static ThreadPoolManager mInstance;
    private List<Runnable> mList;
    private ThreadPoolExecutor mExecutor;
    private ThreadProgressInterface mProgressInterface;

    private ThreadPoolManager(){
        //ThreadPoolExecutor方法 参数的含义:
        //corePoolSize:线程池维护线程的最小数量
        //maximumPoolSize:线程池维护线程的最大数量
        //keepAliveTime:线程池对维护线程的允许空闲时间
        //unit:（线程池对维护线程的允许空闲时间）单位
        //woreQueue:线程池阻塞任务队列
        //handler:线程池对拒绝处理任务的解决方案（当提交任务数 > maximumPoolSize + woreQueue 时处理）
        mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(FIXED_CAPACITY_DEFAULT), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                Log.d(TAG, "线程池拒绝处理任务");
            }
        });
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
                Log.d(TAG,"终止线程池");
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
        public void onThreadProgress(int progress);
    }
}
