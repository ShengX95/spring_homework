package com.sx.service;

import com.sx.pojo.Account;

/**
 * @author shengx
 * @date 2020/3/1 20:50
 */
public interface ITransferService {
    public void transferMoney(Account acc1, Account acc2, int amount);
    public Account getAccount(String account);
}
