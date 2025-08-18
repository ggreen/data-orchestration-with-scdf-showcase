package spring.gemfire.batch.account.batch.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import spring.gemfire.showcase.account.domain.account.Account;

@Repository
public interface AccountRepository extends KeyValueRepository<Account,String> {
}
