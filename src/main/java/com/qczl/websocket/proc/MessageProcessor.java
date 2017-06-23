package com.qczl.websocket.proc;

import com.qczl.websocket.constans.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MessageProcessor extends AbstractProcessor<Map<String,Object>> {
    private static final Logger   logger = LoggerFactory.getLogger(MessageProcessor.class);

    @PostConstruct
    public void init() {
        this.queue = new LinkedBlockingQueue<>(queuesize);
        this.executorService = Executors.newFixedThreadPool(coresize);
        
        for (int i = 0; i < coresize; i++) {
            MessageProcThread thread = new MessageProcThread(this, i);
            this.executorService.execute(thread);
        }
    }

    private AtomicLong ato = new AtomicLong();

    @Override
    public void send(Map<String,Object> taskMap) {
        try {
            WebSocketSession session = (WebSocketSession)(taskMap.get(Constants.SOCKETSESSION_KEY));

            String message = String.valueOf(taskMap.get(Constants.MESSAGE_KEY));

            synchronized (session){
                if (session.isOpen()){

                    session.sendMessage(new TextMessage(message));
                    logger.info("{}开始发送消息{}",ato.getAndIncrement(), message);
                }
            }
        }
        catch (Exception ex) {
            logger.error("消息发送异常:{}", ex);
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void destory() {
        if (!this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
    }
}
