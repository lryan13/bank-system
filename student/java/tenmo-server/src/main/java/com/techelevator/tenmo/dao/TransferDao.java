package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    void sendTransfer(String usernameFrom, String usernameTo, BigDecimal amountToSend);
    List<Transfer> getTransfers(String username);
}
