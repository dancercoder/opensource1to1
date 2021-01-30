package one.opensource.hystrix.demo.domain;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import one.opensource.hystrix.demo.data.DataCache;

import static one.opensource.util.ExecuteUtil.*;

public class CustomerService {

    public Customer getAccount(Long customerId) {
        return new GetCustomerCommand(customerId).execute();
    }

    public class GetCustomerCommand extends HystrixCommand<Customer> {

        private Long customerId;

        GetCustomerCommand(Long customerId) {
            super(HystrixCommandGroupKey.Factory.asKey("CustomerService"));
            this.customerId = customerId;
        }

        protected Customer run() throws Exception {
            blockRandomTeenMills();
            throwRandomException(0.001f);
            randomBlockHundredMills(0.002f);
            return getCustomerInternal(this.customerId);
        }
    }

    private Customer getCustomerInternal(Long customerId) {
        //Caused by: java.lang.NoClassDefFoundError: Could not initialize class one.opensource.hystrix.demo.data.DataCache
        return DataCache.getCustomerById(customerId);
    }
}
