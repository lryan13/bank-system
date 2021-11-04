package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private Integer id;
    private Integer typeId;
    private Integer statusId;
    private Integer accountFromId;
    private Integer accountToId;
    private BigDecimal amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Integer accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Integer getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Integer accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", statusId=" + statusId +
                ", accountFromId=" + accountFromId +
                ", accountToId=" + accountToId +
                ", amount=" + amount +
                '}';
    }
}
