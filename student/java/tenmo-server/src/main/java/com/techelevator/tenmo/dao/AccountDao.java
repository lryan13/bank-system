package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    long findAccountIdByUserId(long userId);
    void updateSenderAccount(Long account_id);
    void updateRecipientAccount(Long account_id);
    BigDecimal getBalance(String username);
    String getUsernameByAccountId(Long accountId);

}
