package com.assignment.e_wallet_project.card;

public class BankCards {

    String bankName;
    String cardNumber;

    public BankCards(String bankName, String cardNumber) {
        this.bankName = bankName;
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
