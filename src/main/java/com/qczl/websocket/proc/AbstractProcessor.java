package com.qczl.websocket.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class  AbstractProcessor<T> {
    private static final Logger                    logger    = LoggerFactory.getLogger(AbstractProcessor.class);
    
    protected int                                  coresize  = 10;

    protected int                                  queuesize = 1000;
    
    protected ExecutorService                      executorService;
    
    protected LinkedBlockingQueue<T> queue;
    
    public void addMessageTask(T t) {
        try {
            queue.put(t);
            logger.debug("消息已插入待发送队列:{}", t);
        } catch (Exception e) {
            logger.error("插入待发送消息失败:{}", e);
            e.printStackTrace();
        }
    }
    
    public abstract void send(T t);
    
    public T take() throws InterruptedException {
        return queue.take();
    }
    
    public void setCoresize(int coresize) {
        this.coresize = coresize;
    }

    public void setQueuesize(int queuesize) {
        this.queuesize = queuesize;
    }
    
}
