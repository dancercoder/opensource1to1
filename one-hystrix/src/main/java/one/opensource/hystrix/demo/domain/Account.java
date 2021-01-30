package one.opensource.hystrix.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class Account {

    private Long accountId;
    private Long customerId;
    private BigDecimal balance;

}
