package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransferDao  implements TransferDao{
    private List<Transfer> transfers;
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer create(long transferTypeId, long transferStatusId, long accountFrom, long accountTo, BigDecimal amount) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(?,?,?,?,?) RETURNING transfer_id;";
        long transferId =  jdbcTemplate.queryForObject(sql, long.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
        return new Transfer(transferId);
    }

    // JOINS to get user_ids associated with accounts to and from for transfer (transfer, account)
    // update balance based on
    @Override
    public void sendTransfer(Long userIdFrom, Long userIdTo, BigDecimal amountToSend) {
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account SET balance = balance - ? WHERE user_id = ?; " +
                "UPDATE account SET balance = balance + ? WHERE user_id = ?; " +
                "COMMIT";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, amountToSend, userIdFrom, amountToSend, userIdTo);
        mapRowToTransfer(rowSet);
    }

    @Override
    public List<Transfer> getTransfers(String username) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setTransferTypeId(rowSet.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getLong("transfer_status_id"));
        transfer.setAccountFromId(rowSet.getLong("account_from"));
        transfer.setAccountToId(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
