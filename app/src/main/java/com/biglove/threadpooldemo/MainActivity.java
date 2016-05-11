package com.biglove.threadpooldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        for (int i = 0; i < 5; i++) {
            TestTask testTask = new TestTask(i);
            executor.execute(testTask);
            System.out.println("线程中的线程数：" + executor.getPoolSize());
            System.out.println("已执行线程数：" + executor.getCompletedTaskCount());
            System.out.println("等待线程数：" + executor.getQueue().size());
        }

        executor.shutdown();*/
//        testManager();
        testManager2();
    }


    private void testManager() {
        List<Runnable> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new TestTask(i));
        }

        ThreadPoolManager poolManager = ThreadPoolManager.Instance();
        poolManager.setProgressInterface(new ThreadPoolManager.ThreadProgressInterface() {
            @Override
            public void onThreadStart() {
                System.out.println("==================================线程池内的任务开始执行");
            }

            @Override
            public void onThreadEnd() {
                System.out.println("======线程池已经终止，任务全部执行结束，可以开始做下一步操作");
            }
        });
        poolManager.addThreadToPool(list);
        poolManager.start();
    }

    private void testManager2() {
        List<Runnable> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new TestTask(i));
        }

        ThreadPoolManager2 poolManager = ThreadPoolManager2.Instance();
        poolManager.setProgressInterface(new ThreadPoolManager2.ThreadProgressInterface() {
            @Override
            public void onThreadStart() {
                System.out.println("==================================线程池内的任务开始执行");
            }

            @Override
            public void onThreadEnd() {
                System.out.println("======线程池已经终止，任务全部执行结束，可以开始做下一步操作");
            }
        });
        poolManager.addThreadToPool(list);
        poolManager.start();
    }
}
