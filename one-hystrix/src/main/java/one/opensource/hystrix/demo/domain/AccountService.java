package one.opensource.hystrix.demo.domain;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import one.opensource.hystrix.demo.data.DataCache;

import static one.opensource.util.ExecuteUtil.*;

public class AccountService {

    public Account getAccount(Long customerId) {
        return new GetAccountCommand(customerId).execute();
    }

    public class GetAccountCommand extends HystrixCommand<Account> {

        private Long customerId;

        GetAccountCommand(Long customerId) {
            super(HystrixCommandGroupKey.Factory.asKey("AccountService"));
            this.customerId = customerId;
        }

        protected Account run() throws Exception {
            blockRandomTeenMills();
            throwRandomException(0.001f);
            randomBlockHundredMills(0.02f);
            return getAccountInternal(this.customerId);
        }
    }

    private Account getAccountInternal(Long customerId) {
        return DataCache.getAccountById(customerId);
    }
}
