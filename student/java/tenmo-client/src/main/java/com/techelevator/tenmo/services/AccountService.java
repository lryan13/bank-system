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

    public Transfer[] getTransfersByUser(Long accountToId) {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(this.baseUrl + "transfer/list/" + accountToId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("error found");
        }
        return transfers;
    }

    public Transfer[] getPendingTransfers(Long accountToId) {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(this.baseUrl + "transfer/pending/" + accountToId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfers;
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

    public String findUsernameByAccountId(Long accountId) throws AccountServiceException {
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "account/username/" + accountId;
        ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), String.class);
        return response.getBody();
    }


    public boolean createTransfer(Transfer transfer){
        boolean success = false;
        try {
            restTemplate.exchange(this.baseUrl + "transfer/send", HttpMethod.POST, makeTransferEntity(transfer), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    public Transfer getTransferById(long transferId) throws AccountServiceException {
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "transfer/get/" + transferId;
        ResponseEntity<Transfer> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), Transfer.class);
        return response.getBody();
    }

    public User[] printRecipients() {
        String path = this.baseUrl + "account/user/list";
        ResponseEntity<User[]> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), User[].class);
        System.out.println("************************************\n" + "Users\n" + "ID            Name\n" + "************************************");
        for(User user: response.getBody()) {
            if (!user.getUsername().equals(this.user.getUser().getUsername())) {
                System.out.println(user.getId() + "          " + user.getUsername());
            }
        }
        System.out.println("***********\n");
        return response.getBody();
    }

    public boolean createRequest(Transfer transfer){
        boolean success = false;
        try {
            restTemplate.exchange(this.baseUrl + "transfer/request", HttpMethod.POST, makeTransferEntity(transfer), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    public boolean reject(Transfer transfer){
        boolean success = false;
        try {
            restTemplate.exchange(this.baseUrl + "transfer/reject/" + transfer.getTransferId(), HttpMethod.POST, makeTransferEntity(transfer), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    public boolean approve(Transfer transfer){
        boolean success = false;
        try {
            restTemplate.exchange(this.baseUrl + "transfer/approve/" + transfer.getTransferId(), HttpMethod.POST, makeTransferEntity(transfer), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return success;
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
