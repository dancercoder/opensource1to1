package one.opensource.hystrix;

import com.netflix.config.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;
import one.opensource.hystrix.demo.domain.Customer;
import one.opensource.hystrix.demo.domain.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ApplicationMainServlet extends HttpServlet {

    private final ThreadPoolExecutor requestClientExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.MINUTES,
            new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void init() throws ServletException {
//        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.coreSize", 4);
//        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.GetCustomerCommand.execution.isolation.thread.timeoutInMilliseconds", 30);
//        //ConfigurationManager.getConfigInstance().setProperty("hystrix.command.*.execution.isolation.thread.timeoutInMilliseconds", 50);
//        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.metrics.rollingPercentile.numBuckets", 60);

        runSimulateTrade();
    }

    public void runSimulateTrade() {
        //模拟单例AccountService注入
        final CustomerService customerService = new CustomerService();
        //启动无限循环，模拟交易发生
        while (true) {
            if (Thread.interrupted()) {
                System.out.println("AppMain被终止，退出退出模拟....");
                requestClientExecutor.shutdownNow();
                break;
            } else {
                try {
                    requestClientExecutor.submit(new Runnable() {
                        public void run() {
                            Long randomCustomerId = (long) Math.random() * 100 + 1;
                            Customer customer = customerService.getAccount(randomCustomerId);
                        }
                    });
                } catch (RejectedExecutionException rejE) {
                    log.warn("提交交易请求，被拒绝，错误信息：{}", rejE.getMessage());
                }
            }
//            try{
//                Thread.sleep(2);
//            }catch (InterruptedException e){
//                log.error(e.getLocalizedMessage());
//            }
        }

    }
}
