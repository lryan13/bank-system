package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private static final Account ACCOUNT_1 = new Account(1L, 1L, new BigDecimal(100));
    private static final Account ACCOUNT_2 = new Account(2L, 2L, new BigDecimal(200));

    private Account testAccount;

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(dataSource);
        testAccount = new Account(0L, 0L, new BigDecimal(9999));
    }

    @Test
    public void findAccountIdByUserId_returns_null_if_id_not_found() {
        long accountId = sut.findAccountIdByUserId(2L);
        Assert.assertEquals(null, accountId);

    }

    @Test
    public void findAccountIdByUserId_returns_correct_value_for_account_matching_user() {

    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccount_id(), actual.getAccount_id());
        Assert.assertEquals(expected.getUser_id(), actual.getUser_id());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
