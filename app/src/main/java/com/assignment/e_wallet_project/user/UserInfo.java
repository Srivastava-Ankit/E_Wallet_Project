package com.assignment.e_wallet_project.user;

public class UserInfo {

    String lastName;
    String firstName;
    String balanceAmount;
    String numberOfCards;

    public UserInfo(){
        this.balanceAmount = 0 + "";
    }

    public UserInfo(String firstName, String lastName){

        this.firstName = firstName;
        this.lastName = lastName;
        this.balanceAmount = 0 + "";   // convert the balance into String
        this.numberOfCards = "0";

    }


    public UserInfo(String lastName, String firstName, String balanceAmount) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.balanceAmount = balanceAmount;
        this.numberOfCards = "0";
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public String getNumberOfCards(){ return numberOfCards; }
}
