package spring.gemfire.batch.account.batch.mapper;

import lombok.SneakyThrows;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.gemfire.showcase.account.domain.account.Account;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRowMapperTest {

    private AccountRowMapper subject;
    @Mock
    private ResultSet rs;

    @BeforeEach
    void setUp() {
        subject = new AccountRowMapper();
    }

    @SneakyThrows
    @Test
    void mapRow() {
        Account expected = JavaBeanGeneratorCreator.of(Account.class).create();

        when(rs.getString(anyString()))
                .thenReturn(expected.getId())
                .thenReturn(expected.getName())
                .thenReturn(expected.getCustomer().getFirstName())
                .thenReturn(expected.getCustomer().getLastName())
                .thenReturn(expected.getCustomer().getContact().getEmail())
                .thenReturn(expected.getCustomer().getContact().getPhone());

        var actual = subject.mapRow(rs,1);
        assertEquals(expected, actual);
    }
}