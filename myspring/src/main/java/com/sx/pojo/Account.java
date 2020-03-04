package com.sx.pojo;

/**
 * @author shengx
 * @date 2020/3/1 20:32
 */
public class Account {
    private String name;
    private int amount;
    private String account;

    public Account() {
    }

    public Account(String name, int amount, String account) {
        this.name = name;
        this.amount = amount;
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", account='" + account + '\'' +
                '}';
    }
}
