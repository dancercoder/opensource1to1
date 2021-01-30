package one.opensource.hystrix.getstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

@Slf4j
public class CommandHelloWorld extends HystrixCommand<String> {

    private String name;
    private String action;

    public CommandHelloWorld(String name, String action) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup")));
        this.name = name;
        this.action = action;
    }

    protected String run() throws Exception {
        log.info("执行了方法:name={},action={}", name, action);
        return name + " is " + action;
    }


//    @Override
//    protected String getCacheKey() {
//        return name;
//    }

    public static class UnitTests {

        /**
         * Typically this context will be initialized and shut down via a ServletFilter that wraps a user request or some other lifecycle hook.
         */

        @Test
        public void testReponseFromCache() {
            HystrixRequestContext context = HystrixRequestContext.initializeContext();
            try {
                CommandHelloWorld command1 = new CommandHelloWorld("xiaoming", "chifan");
                CommandHelloWorld command2 = new CommandHelloWorld("xiaoming", "shuijiao");

                command1.execute();
                command2.execute();

                Assert.assertFalse(command1.isResponseFromCache());
                Assert.assertTrue(command2.isResponseFromCache());

            } finally {
                context.shutdown();
            }
        }

        @Test
        public void testObservable() {
//            HystrixRequestContext.initializeContext();
            Observable<String> fBob = new CommandHelloWorld("Bob", "eating").observe();
            Observable<String> fLily = new CommandHelloWorld("Lily", "dancing").observe();
            //这个位置run方法已经执行

            Assert.assertEquals("Bob is eating", fBob.toBlocking().single());
            Assert.assertEquals("Bob is eating", fBob.toBlocking().single());
            Assert.assertEquals("Bob is eating", fBob.toBlocking().single());

            fBob.subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                    log.info("执行方法onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    log.info("执行方法onError");
                }

                @Override
                public void onNext(String s) {
                    log.info("执行方法onNext");
                    log.info(s);
                }
            });

            fLily.subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    log.info("执行方法call");
                    log.info(s);
                }
            });
        }
    }
}
