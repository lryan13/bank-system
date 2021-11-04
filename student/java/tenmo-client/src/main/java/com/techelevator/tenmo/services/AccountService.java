package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private String baseUrl = "localhost:8080/";
    private AuthenticatedUser user;
    RestTemplate restTemplate = new RestTemplate();

    public void setAuthenticatedUser(AuthenticatedUser user){
        this.user = user;
    }


    public BigDecimal getBalance() throws AccountServiceException{
        if (this.user == null) {
            throw new AccountServiceException();
        }
        String path = this.baseUrl + "account/balance";
        ResponseEntity<BigDecimal> response = restTemplate.exchange(path, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
        return response.getBody();
    }

    public Transfer[] getTransfersByUser(Integer userId) {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(this.baseUrl + "transfer/user/" + userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("error found");
        }
        return transfers;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);

    }
}
