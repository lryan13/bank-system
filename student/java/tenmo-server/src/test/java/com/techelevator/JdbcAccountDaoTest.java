package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private Account testAccount;

    private JdbcAccountDao sut;
    private JdbcUserDao user;
    private JdbcTransferDao transfer;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(dataSource);
        testAccount = new Account(0L, 0L, new BigDecimal(9999));

        user = new JdbcUserDao(dataSource);
        user.create("user", "password");
        user.create("userTwo", "password");


    }

    @Test
    public void findAccountIdByUserId_returns_correct_value_for_account_matching_user() {

        long accountId = sut.findAccountIdByUserId(1001);
        Assert.assertEquals(2001, accountId);
    }

    @Test
    public void getBalance_returns_correct_balance_from_account() {
        BigDecimal actualBalance = sut.getBalance("user");
        BigDecimal expectedbalance = new BigDecimal("1000.00");
        Assert.assertEquals(expectedbalance, actualBalance);
    }

    @Test
    public void updateFirstAccount_updates_account_to_expected_final_balance(){
        transfer = new JdbcTransferDao(dataSource);
        Long id = transfer.create( 2L, 2L, (long)2001, (long)2002, new BigDecimal("100"));
        Transfer currentTransfer = transfer.getTransferByTransferId(id);
        sut.updateFirstAccount(currentTransfer.getTransferId(), currentTransfer.getAccountFromId());
        BigDecimal actualResult = sut.getBalance("user");
        BigDecimal expectedResult = new BigDecimal("900.00");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void updateSecondAccount_updates_account_to_expected_final_balance(){
        transfer = new JdbcTransferDao(dataSource);
        Long id = transfer.create( 2L, 2L, (long)2001, (long)2002, new BigDecimal("100"));
        Transfer currentTransfer = transfer.getTransferByTransferId(id);
        sut.updateSecondAccount(currentTransfer.getTransferId(), currentTransfer.getAccountToId());
        BigDecimal actualResult = sut.getBalance("userTwo");
        BigDecimal expectedResult = new BigDecimal("1100.00");
        Assert.assertEquals(expectedResult, actualResult);
    }

    // update method tested by previous two methods

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccount_id(), actual.getAccount_id());
        Assert.assertEquals(expected.getUser_id(), actual.getUser_id());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
