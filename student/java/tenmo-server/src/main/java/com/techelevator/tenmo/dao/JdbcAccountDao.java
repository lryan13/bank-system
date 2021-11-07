package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long findAccountIdByUserId(long user_id) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, user_id);
        if (id != null){
            return id;
        }
        else{
            throw new UsernameNotFoundException("User " + user_id + " was not found.");
        }
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

    @Override
    public String getUsernameByAccountId(Long accountId){
        String sql = "SELECT username FROM users " +
                "JOIN accounts ON users.user_id = accounts.user_id " +
                "WHERE account_id = ?;";
        String username = jdbcTemplate.queryForObject(sql, String.class, accountId);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()){
            return username;
        }
        throw new UsernameNotFoundException("Account with ID " + accountId + " was not found.");
    }

    //TODO refactor with transferId, remove subqueries, combine methods
    @Override
    public void updateFirstAccount(Long transfer_id, Long account_id) {
        jdbcTemplate.execute("BEGIN TRANSACTION");
        String sql = "UPDATE accounts SET balance = balance - " +
                "(SELECT amount FROM transfers WHERE transfer_id = ?) WHERE account_id = ?; ";
        jdbcTemplate.update(sql, transfer_id, account_id);
        jdbcTemplate.execute("COMMIT");
    }

    @Override
    public void updateSecondAccount(Long transfer_id, Long account_id) {
        jdbcTemplate.execute("BEGIN TRANSACTION");
        String sql = "UPDATE accounts SET balance = balance + " +
                "(SELECT amount FROM transfers WHERE transfer_id = ?) WHERE account_id = ?; ";
        jdbcTemplate.update(sql, transfer_id, account_id);
        jdbcTemplate.execute("COMMIT");
    }

    @Override
    public void update(Long transfer_id, Long account_from, Long account_to){
        updateFirstAccount(transfer_id, account_from);
        updateSecondAccount(transfer_id, account_to);
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccount_id(rowSet.getLong("account_id"));
        account.setUser_id(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
