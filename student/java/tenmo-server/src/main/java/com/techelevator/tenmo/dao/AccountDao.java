package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    long findAccountIdByUserId(long userId);
    BigDecimal getBalance(String username);

}
