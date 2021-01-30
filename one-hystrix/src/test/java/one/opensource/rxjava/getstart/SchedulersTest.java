package one.opensource.rxjava.getstart;

import org.junit.Assert;
import org.junit.Test;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Scheduler用来调度operation
 * Schedulers是生成Scheduler的工厂类
 */

public class SchedulersTest {

    String result="";

    @Test
    public void test(){
        Scheduler scheduler= Schedulers.immediate();
        Scheduler.Worker worker=scheduler.createWorker();
        worker.schedule(() -> result += "action");

        Assert.assertTrue(result.equals("action"));
    }
}
