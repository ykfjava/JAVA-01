package com.ykfjava.week04;

import com.ykfjava.week04.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class Thead03_Callable_FutureTask implements Callable {
    private static final Logger logger = LoggerFactory.getLogger(Thead03_Callable_FutureTask.class);

    private Entity entity;

    public Thead03_Callable_FutureTask() { }

    public Thead03_Callable_FutureTask(Entity entity) {
        this.entity = entity;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Entity entity = new Entity(56, "old");
        FutureTask futureTask = new FutureTask(new Thead03_Callable_FutureTask(entity));
        Thread thead01 = new Thread(futureTask);
        thead01.start();
        if (!futureTask.isDone()) {
            logger.info("状态： {} ; 当前线程 ： {} ; 操作数： {}" , "运行中", Thread.currentThread().getName(), entity.getAge());
        }
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(),( (Entity) futureTask.get()).getAge());
    }

    @Override
    public Object call() throws Exception {
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(), entity.getAge());
        entity.addAge();
        logger.info("当前线程 ： {} ; 操作之后数： {}" , Thread.currentThread().getName(), entity.getAge());
        return entity;
    }
}
