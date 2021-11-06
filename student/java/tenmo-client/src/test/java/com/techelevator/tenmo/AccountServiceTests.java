package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AccountServiceException;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountServiceTests {

    private AccountService accountService;
    private AuthenticatedUser user;
    private AuthenticationService authenticationService;
    private UserCredentials userCredentials;
    private User myUser;

    public AccountServiceTests(){

    }


    @Before
    public void setup(){
        accountService = new AccountService();
        user = null;
        myUser = new User();
        authenticationService = new AuthenticationService("http://localhost:8080/");
        userCredentials = new UserCredentials("Test", "password");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = new UserCredentials("Test", "password");
            try {
                authenticationService.register(credentials);
                isRegistered = true;
            } catch(AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: "+e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }

        isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = new UserCredentials("TestTwo", "password");
            try {
                authenticationService.register(credentials);
                isRegistered = true;
            } catch(AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: "+e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }

        while (user == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = new UserCredentials("Test", "password");
            try {
                user = authenticationService.login(credentials);
                accountService.setAuthenticatedUser(user);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: "+e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }




    @Test
    public void getBalance_returns_correct_balance_for_logged_in_user() throws AccountServiceException {
        BigDecimal actualBalance = accountService.getBalance();
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void getTransfersByUser_returns_accurate_transfers_for_user_logged_in() throws AccountServiceException {
        //accountService.createTransfer(new Transfer(3999, 1, 1, accountService.findAccountIdByUserId(user.getUser().getId())), );
        Transfer[] actual = accountService.getTransfersByUser(user.getUser().getId());
        Transfer[] expected = new Transfer[1];
    }

}
