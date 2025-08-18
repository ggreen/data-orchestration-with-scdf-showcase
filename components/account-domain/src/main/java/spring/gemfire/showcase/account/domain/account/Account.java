package spring.gemfire.showcase.account.domain.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account
{
    private String id;
    private String name;
    private Customer customer;
}




