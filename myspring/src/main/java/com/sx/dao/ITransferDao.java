package com.sx.dao;

import com.sx.pojo.Account;

public interface ITransferDao {
    public void transfer(Account account);

    public Account queryAccount(String account);
}
