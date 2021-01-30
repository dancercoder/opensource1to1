package one.opensource.hystrix.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class Customer implements Serializable {
    private Long customerId;
    private String name;
}
