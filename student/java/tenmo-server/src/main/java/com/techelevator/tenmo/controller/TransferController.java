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

    private final long pending = 1;
    private final long approved = 2;

    private final long request = 1;
    private final long send = 2;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void createAndUpdate(Principal principal, @RequestBody Transfer transfer) {
        String user = principal.getName();
        long userId = userDao.findIdByUsername(user);
        long accountId = accountDao.findAccountIdByUserId(userId);
        if(transfer.getAmount().compareTo(new BigDecimal(0)) <= 0){
            System.out.println("can not send a negative or 0 amount");
        }
        else if(transfer.getAmount().compareTo(accountDao.getBalance(user)) <= 0 || accountDao.getBalance(user).compareTo(new BigDecimal(0)) >= 0) {
            transferDao.create(send, approved, accountId, transfer.getAccountToId(), transfer.getAmount());
            transfer.setAccountToId(transfer.getAccountToId());
            accountDao.updateSenderAccount(accountId);
            accountDao.updateRecipientAccount(transfer.getAccountToId());
        }
        else System.out.println("not enough money");
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public void request(Principal principal, @RequestBody Transfer transfer){
        String user = principal.getName();
        long userId = userDao.findIdByUsername(user);
        long accountId = accountDao.findAccountIdByUserId(userId);
        if(transfer.getAmount().compareTo(new BigDecimal(0)) <= 0){
            System.out.println("can not request a negative or 0 amount");
        }
        else {
            transferDao.create(request, pending, accountId, transfer.getAccountToId(), transfer.getAmount());
        }
    }

    @RequestMapping(path = "/reject/{transferId}", method = RequestMethod.POST)
    public void reject(@PathVariable long transferId) {
        transferDao.reject(transferId);
    }

    @RequestMapping(path = "/approve/{transferId}", method = RequestMethod.POST)
    public void approve(@PathVariable long transferId) {
        transferDao.approve(transferId);
    }


    @RequestMapping(path = "/list/{accountToId}", method = RequestMethod.GET)
    public List<Transfer> transfers(Principal principal, @PathVariable Long accountToId) {
        return transferDao.getTransfersByUsername(principal.getName(), accountToId);
    }

    @RequestMapping(path = "/pending/{accountToId}", method = RequestMethod.GET)
    public List<Transfer> transfers(@PathVariable long accountToId) {
        return transferDao.getTransferByAccountId(accountToId);
    }

    @RequestMapping(path = "/get/{transferId}", method = RequestMethod.GET)
    public Transfer transfer(@PathVariable Long transferId) {
        return transferDao.getTransferByTransferId(transferId);
    }

}
