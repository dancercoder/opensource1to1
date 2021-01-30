package one.opensource.hystrix.getstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

@Slf4j
public class CommandHelloWorldFailure extends HystrixCommand<String> {

    private String name;

    public CommandHelloWorldFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        throw new HystrixBadRequestException("command always fails");
//        throw new RuntimeException("command always fails");
    }

    /**
     * 失败一定会执行的Fallback方法，但是默认是不支持的操作，抛出异常
     *
     * @return
     */
    @Override
    protected String getFallback() {
        log.info("执行方法getFallBack");
        return name;
    }


    public static class UnitTest {

        @Test
        public void testSynchronous() {
            CommandHelloWorldFailure command1 = new CommandHelloWorldFailure("Bob");
            Assert.assertEquals("Bob", command1.execute());
        }
        @Test
        public void testAsynchronous2() throws Exception {

            Future<String> fWorld = new CommandHelloWorldFailure("World").queue();
            Future<String> fBob = new CommandHelloWorldFailure("Bob").queue();

            assertEquals("World", fWorld.get());
            assertEquals("Bob", fBob.get());
        }
    }
}
