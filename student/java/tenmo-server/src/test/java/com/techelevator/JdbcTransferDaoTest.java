package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests{

    private JdbcTransferDao transfer;
    private JdbcAccountDao account;
    private JdbcUserDao user;
    private Transfer testTransfer;

    @Before
    public void setup() {
        transfer = new JdbcTransferDao(dataSource);
        user = new JdbcUserDao(dataSource);
        user.create("Jeff", "password");
        user.create("Ryan", "password");
        testTransfer = new Transfer((long)3999, 2L, 2L, (long)2002, (long)2001, new BigDecimal("1000.00"));
    }

    @Test
    public void create_method_creates_new_transaction_in_database(){
        long transferId = transfer.create(testTransfer.getTransferTypeId(), testTransfer.getTransferStatusId(), testTransfer.getAccountFromId(), testTransfer.getAccountToId(), testTransfer.getAmount());
        Transfer actual = transfer.getTransferByTransferId(transferId);
        testTransfer.setTransferId(transferId);
        Assert.assertEquals(testTransfer.toString(), actual.toString());
    }

    @Test
    public void getTransfersByUsername_returns_list_of_all_transfers_by_specified_user(){
        List<Transfer> transfers = transfer.getTransfersByUsername("Jeff");
        String actual = transfers.toString();
        List<User> expected = new ArrayList<>();
        String expectedResult = expected.toString();
        Assert.assertEquals(expectedResult, actual);
        //@TODO passes because list of transfers is empty
    }

    @Test
    public void getTransferByTransferId_returns_correct_transfer_based_on_id(){
        long transferId = transfer.create(testTransfer.getTransferTypeId(), testTransfer.getTransferStatusId(), testTransfer.getAccountFromId(), testTransfer.getAccountToId(), testTransfer.getAmount());
        Transfer actual = transfer.getTransferByTransferId(transferId);
        testTransfer.setTransferId(transferId);
        Assert.assertEquals(testTransfer.toString(), actual.toString());
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getAccountFromId(), actual.getAccountFromId());
        Assert.assertEquals(expected.getAccountToId(), actual.getAccountToId());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }
}
