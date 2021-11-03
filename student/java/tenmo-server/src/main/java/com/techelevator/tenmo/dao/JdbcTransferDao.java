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
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void sendTransfer(String userIdFrom, String userIdTo, BigDecimal amountToSend) {
        String sql = "BEGIN TRANSACTION" +
                "UPDATE account SET balance = balance - ? WHERE user_id = ?;" +
                "UPDATE" +
                "COMMIT";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, amountToSend, userIdFrom);
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
