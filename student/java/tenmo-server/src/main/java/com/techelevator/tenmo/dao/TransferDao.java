package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer create(long transferTypeId, long transferStatusId, long accountFrom, long accountTo, BigDecimal amount);
    void sendTransfer(Long userIdFrom, Long userIdTo, BigDecimal amountToSend);
    List<Transfer> getTransfers(String username);
}
