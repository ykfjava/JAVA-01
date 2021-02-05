package com.ykfjava.week04;

import com.ykfjava.week04.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Thead02_Sleep extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Thead02_Sleep.class);

    private Entity entity;


    public static void main(String[] args) throws InterruptedException {
        Entity entity = new Entity(56, "old");

        Thread thead01 = new Thead02_Sleep("Thead02_Sleep", entity);
        thead01.start();
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(), entity.getAge());
        while (thead01.isAlive()) {
            Thread.sleep(0);
        }
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(), entity.getAge());
    }

    public Thead02_Sleep() { }

    public Thead02_Sleep(String name, Entity entity) {
        super(name);
        this.entity = entity;
    }

    @Override
    public void run() {
        super.run();
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(), entity.getAge());
        entity.addAge();
        logger.info("当前线程 ： {} ; 操作之后数： {}" , Thread.currentThread().getName(), entity.getAge());
    }
}
