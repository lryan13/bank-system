package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
    return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "/user/{username}", method = RequestMethod.GET)
    public int userId(@PathVariable String username) {
        return userDao.findIdByUsername(username);
    }

    @RequestMapping(path = "/user/list", method = RequestMethod.GET)
    public List<User> allUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/userId/{userId}", method = RequestMethod.GET)
    public long accountId(@PathVariable int userId) {
        return accountDao.findAccountIdByUserId(userId);
    }

    @RequestMapping(path = "/username/{accountId}", method = RequestMethod.GET)
    public String username(@PathVariable long accountId) {
        return accountDao.getUsernameByAccountId(accountId);
    }
}
