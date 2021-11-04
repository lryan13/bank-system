package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(?,?,?,?,?) RETURNING transfer_id;";
        long transferId =  jdbcTemplate.queryForObject(sql, long.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
        return new Transfer(transferId);
    }

    @Override
    public List<Transfer> getTransfersByUsername(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers " +
                "JOIN accounts ON accounts.account_id = transfers.account_from " +
                "JOIN users ON users.user_id = accounts.user_id " +
                "WHERE username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        while(rowSet.next()){
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(Long transferId) {
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowset.next()) {
            return mapRowToTransfer(rowset);
        } else {
            return null;
        }
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
