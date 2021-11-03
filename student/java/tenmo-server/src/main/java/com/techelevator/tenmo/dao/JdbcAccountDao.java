package com.techelevator.tenmo.dao;

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
    public BigDecimal getBalance(String username) throws UsernameNotFoundException {
        String sql = "SELECT balance FROM accounts " +
                "JOIN users ON accounts.user_id = users.user_id " +
                "WHERE username = ?;";
        BigDecimal userbalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return userbalance;
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }
}
