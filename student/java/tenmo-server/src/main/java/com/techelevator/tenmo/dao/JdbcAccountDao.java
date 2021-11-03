package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long findAccountIdByUserId(long userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()){
            long id = mapRowToAccount(rowSet).getAccountId();
            return id;
        }
        throw new UsernameNotFoundException("User " + userId + " was not found.");
    }

    @Override
    public BigDecimal getBalance(String username) throws UsernameNotFoundException {
        String sql = "SELECT balance FROM accounts " +
                "JOIN users ON accounts.user_id = users.user_id " +
                "WHERE username = ?;";
        BigDecimal userBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return userBalance;
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getLong("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
    //TODO make an update balancce w sql, param for id and amt/add/subtract
}
