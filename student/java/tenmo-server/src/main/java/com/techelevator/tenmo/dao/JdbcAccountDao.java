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
    public long findAccountIdByUserId(long user_id) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        /*SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user_id);*/
        Long id = jdbcTemplate.queryForObject(sql, Long.class, user_id);
        if (id != null){
            return id;
        }
        throw new UsernameNotFoundException("User " + user_id + " was not found.");
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

    /*private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccount_id(rowSet.getLong("account_id"));
        account.setUser_id(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }*/
    //TODO make an update balancce w sql, param for id and amt/add/subtract
}
