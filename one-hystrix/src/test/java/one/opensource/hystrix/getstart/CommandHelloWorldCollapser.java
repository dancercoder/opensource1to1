package one.opensource.hystrix.getstart;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

/**
 * 命令合成器
 * 支持两种类型的合并：request-scoped、globally-scoped，通过构造方法指定参数，默认的是request-scoped
 * request-scoped每一个HystrixRequestContext中的请求合并一个batch；globally-scoped多个HystrixRequestContext合并一个batch
 * 如果被依赖的服务不能在一个调用中处理多个HystrixRequestContext，应该选择request-scoped
 * 在Hystrix中仅仅使用request-scoped，因为已经基于假设"一个command中仅仅使用一个HystrixRequestContext，
 * 那么一个batch仅仅对应一个request，命令collapsing在，相同命令，使用不同参数的时候，起作用"
 */
@Slf4j
public class CommandHelloWorldCollapser extends HystrixCollapser<List<String>, String, Integer> {

    private final Integer key;

    public CommandHelloWorldCollapser(Integer key) {
        this.key = key;
    }

    @Override
    public Integer getRequestArgument() {
        return key;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, Integer>> collapsedRequests) {
        return new BatchCommand(collapsedRequests);
    }

    @Override
    protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, Integer>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<String, Integer> request : collapsedRequests) {
            request.setResponse(batchResponse.get(count++));
        }
    }

    private static final class BatchCommand extends HystrixCommand<List<String>> {

        private final Collection<CollapsedRequest<String, Integer>> requests;

        private BatchCommand(Collection<CollapsedRequest<String, Integer>> requests) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey")));
            this.requests = requests;
        }

        @Override
        protected List<String> run() throws Exception {
            ArrayList<String> response = new ArrayList<String>();
            for (CollapsedRequest<String, Integer> request : requests) {
                response.add("ValueForKey: " + request.getArgument());
            }
            return response;
        }
    }

    public static class UnitTest {

        @Test
        public void testCollapser() throws InterruptedException, ExecutionException {
            HystrixRequestContext context = HystrixRequestContext.initializeContext();
            try {
                Future<String> f1 = new CommandHelloWorldCollapser(1).queue();
                Future<String> f2 = new CommandHelloWorldCollapser(2).queue();
                Future<String> f3 = new CommandHelloWorldCollapser(3).queue();
                Future<String> f4 = new CommandHelloWorldCollapser(4).queue();

                assertEquals("ValueForKey: 1", f1.get());
                assertEquals("ValueForKey: 2", f2.get());
                assertEquals("ValueForKey: 3", f3.get());
                assertEquals("ValueForKey: 4", f4.get());

                assertEquals(1, HystrixRequestLog.getCurrentRequest().getExecutedCommands().size());

            } finally {
                context.shutdown();
            }
        }
    }
}
