package com.rjx.bio.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author junxi
 * @Date 2023/11/20   14:45
 * @Project ioCode
 * @description:
 */
public class HandlerSocketServerPool {
    //1. 创建一个线程池的成员变量用于存储一个线程池对象
    private ExecutorService executorService;


    /**
     * 2.创建这个类的的对象的时候就需要初始化线程池对象
     * public ThreadPoolExecutor(int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue)
     */
    public HandlerSocketServerPool(int maxThreadNum, int queueSize){
        this.executorService = new ThreadPoolExecutor(3,maxThreadNum,120,
                TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));

    }
    public void execute(Runnable target){
        executorService.execute(target);
    }
}
