package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public void createAndUpdate(Principal principal, @RequestBody Transfer transfer) {
        String user = principal.getName();
        long userId = userDao.findIdByUsername(user);
        long accountId = accountDao.findAccountIdByUserId(userId);
        System.out.println("Account ID is" + accountId);
        System.out.println("User is " + user);
        if(transfer.getAmount().compareTo(new BigDecimal(0)) <= 0){
            System.out.println("can not send a negative or 0 amount");
        }
        else if(transfer.getAmount().compareTo(accountDao.getBalance(user)) <= 0 || accountDao.getBalance(user).compareTo(new BigDecimal(0)) <= 0) {
            transferDao.create(2, 2, accountId, transfer.getAccountToId(), transfer.getAmount());
            transfer.setAccountToId(transfer.getAccountToId());
            accountDao.updateSenderAccount(accountId);
            accountDao.updateRecipientAccount(transfer.getAccountToId());
        }
        else System.out.println("not enough money");
    }

    
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<Transfer> transfers(Principal principal) {
        return transferDao.getTransfersByUsername(principal.getName());
    }

    @RequestMapping(path = "/get/{transferId}", method = RequestMethod.GET)
    public Transfer transfer(@PathVariable Long transferId) {
        return transferDao.getTransferByTransferId(transferId);
    }

}
