package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    long findAccountIdByUserId(long userId);
    void updateFirstAccount(Long transfer_id, Long account_id);
    void updateSecondAccount(Long transfer_id, Long account_id);
    BigDecimal getBalance(String username);
    String getUsernameByAccountId(Long accountId);
    void update(Long transfer_id, Long account_from, Long account_to);

}
