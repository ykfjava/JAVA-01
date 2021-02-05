package com.ykfjava.week04;

import com.ykfjava.week04.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class Thead03_Callable_pool implements Callable {
    private static final Logger logger = LoggerFactory.getLogger(Thead03_Callable_pool.class);

    private Entity entity;

    public Thead03_Callable_pool() { }

    public Thead03_Callable_pool(Entity entity) {
        this.entity = entity;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Entity entity = new Entity(56, "old");
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(new Thead03_Callable_pool(entity));
        if (!future.isDone()) {
            logger.info("状态： {} ; 当前线程 ： {} ; 操作数： {}" , "运行中", Thread.currentThread().getName(), ( (Entity) future.get()).getAge());
        }
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(),( (Entity) future.get()).getAge());
    }

    @Override
    public Object call() throws Exception {
        logger.info("当前线程 ： {} ; 操作数： {}" , Thread.currentThread().getName(), entity.getAge());
        entity.addAge();
        logger.info("当前线程 ： {} ; 操作之后数： {}" , Thread.currentThread().getName(), entity.getAge());
        return entity;
    }
}
