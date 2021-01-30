package one.opensource.hystrix.demo.data;

import one.opensource.hystrix.demo.domain.Account;
import one.opensource.hystrix.demo.domain.Customer;
import one.opensource.util.ChineseNameUtil;

import java.util.HashMap;
import java.util.Map;

public class DataCache {

    private static final Map<Long, Customer> customers = new HashMap<Long, Customer>();
    private static final Map<Long, Account> accounts = new HashMap<Long, Account>();


    static {
        for (long customerId = 1; customerId < 101; customerId++) {
            String customerName = ChineseNameUtil.generate();
            customers.put(customerId, new Customer(customerId, customerName));
        }
    }

    public static Customer getCustomerById(Long customerId) {
        return customers.get(customerId);
    }

    public static Account getAccountById(Long customerId) {
        return accounts.get(customerId);
    }
}
