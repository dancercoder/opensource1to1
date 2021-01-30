package one.opensource.hystrix.getstart;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutionException;

@Slf4j
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String> {

    private String name;

    public ObservableCommandHelloWorld(String groupName, String name) {
        super(HystrixCommandGroupKey.Factory.asKey(groupName));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        //Observable没有公有的构造函数，只有create方法，使用Rx编程风格
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    log.info("执行方法call");
                    if (!observer.isUnsubscribed()) {
                        //example代码中有错误，不能添加两个onNext调用
                        observer.onNext("Hello " + name + " !");
                        observer.onCompleted();
                        log.info(Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });//.subscribeOn(Schedulers.io());
    }


    public static class UnitTest {

        @Test
        public void test() throws ExecutionException, InterruptedException {
            //设置不同的group会生成多个线程池
            ObservableCommandHelloWorld command1 = new ObservableCommandHelloWorld("group1", "Bob");
            ObservableCommandHelloWorld command2 = new ObservableCommandHelloWorld("group2", "Lucy");

//            command1.observe().toBlocking().single();
            command1.toObservable().toBlocking().single();
        }
    }
}
