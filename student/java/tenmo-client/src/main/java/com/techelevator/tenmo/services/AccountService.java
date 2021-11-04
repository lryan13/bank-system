package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private static final String baseUrl = "http://localhost:8080/";
    private AuthenticatedUser user;
    RestTemplate restTemplate = new RestTemplate();

    public void setAuthenticatedUser(AuthenticatedUser user){
        this.user = user;
    }


    public BigDecimal getBalance() throws AccountServiceException{
        BigDecimal balance = new BigDecimal(0);
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "account/balance";
        ResponseEntity<BigDecimal> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
        return response.getBody();
    }

    public Transfer[] getTransfersByUser() {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(this.baseUrl + "transfer/list", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("error found");
        }
        return transfers;
    }

    public Transfer[] getPendingTransfers() {
        return null;
    }

    public long findIdByUsername(String username) throws AccountServiceException {
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "account/user/" + username;
        ResponseEntity<Long> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), long.class);
        return response.getBody();
    }

    public long findAccountIdByUserId(Long userId) throws AccountServiceException {
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "account/userId/" + userId;
        ResponseEntity<Long> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), long.class);
        return response.getBody();
    }


    public boolean createTransfer(Transfer transfer){
        boolean success = false;
        try {
            restTemplate.put(this.baseUrl + "transfer/new", makeTransferEntity(transfer));
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("error found");
        }
        return success;
    }

    public void printRecipients() {
        String path = this.baseUrl + "account/user/list";
        ResponseEntity<User[]> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), User[].class);
        for(User user: response.getBody()) {
            System.out.println("Username: " + user.getUsername() + " User Id: " + user.getId());
        }
    }



    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);

    }
}
