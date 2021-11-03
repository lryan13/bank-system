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
    public void create(Principal principal, @RequestBody Transfer transfer) {
        String user = principal.getName();
        long userId = userDao.findIdByUsername(user);
        long accountId = accountDao.findAccountIdByUserId(userId);
        System.out.println("Account ID is" + accountId);
        System.out.println("User is " + user);
        transferDao.create(2, 2, accountId, transfer.getAccountToId(), transfer.getAmount());

    }
    //POST transfer to transfer table, insterting all table column values

    /*@RequestMapping(value = "/send/{userToId}/{amount}", method = RequestMethod.PUT)
    public void sendTransfer(Principal principal, @RequestParam Long userToId, @RequestParam BigDecimal amount) {
        Long userFrom = (long)userDao.findIdByUsername(principal.getName());
        transferDao.sendTransfer(userFrom, userToId, amount);
    }*/

}
