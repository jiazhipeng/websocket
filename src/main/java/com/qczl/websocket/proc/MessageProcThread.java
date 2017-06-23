package com.qczl.websocket.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MessageProcThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcThread.class);
    
    private MessageProcessor proc;
    
    public MessageProcThread(MessageProcessor proc, int index) {
        this.proc = proc;
        this.setName("Thread-" + index);
        this.setDaemon(true);
    }
    
    public void run() {
        for(;;) {
            try {
                Map<String,Object> taskMap = this.proc.take();

                if (taskMap == null) {
                    continue;
                }
                
                this.proc.send(taskMap);
            }
            catch (Exception ex) {
                logger.error("消息发送异常", ex);
            }
        }
    }
}
