package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    long create(long transferTypeId, long transferStatusId, long accountFrom, long accountTo, BigDecimal amount);
    void reject(long transferId);
    void approve(long transferId);
    List<Transfer> getTransfersByUsername(String username);
    List<Transfer> getTransfersByUsername(String username, Long accountToId);
    Transfer getTransferByTransferId(Long transferId);
    List<Transfer> getTransferByAccountId(Long accountId);
}
