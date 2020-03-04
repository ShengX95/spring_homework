package com.sx.service;

import com.sx.anno.Autowired;
import com.sx.anno.Service;
import com.sx.anno.Transactional;
import com.sx.dao.ITransferDao;
import com.sx.pojo.Account;
import com.sx.utils.TransactionManager;

import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/3/1 20:51
 */
@Service(value = "transferService")
@Transactional
public class TransferServiceImpl implements ITransferService {
    @Autowired
    ITransferDao transferDao;

    public void setTransferDao(ITransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @Override
    public void transferMoney(Account acc1, Account acc2, int amount) {
//        TransactionManager transactionManager = TransactionManager.getInstance();
//        try {
//            transactionManager.beginTransaction();
            acc1.setAmount(acc1.getAmount() - amount);
            acc2.setAmount(acc2.getAmount() + amount);
            transferDao.transfer(acc1);
//            int a = 1/0;
            transferDao.transfer(acc2);
//            transactionManager.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                transactionManager.rollback();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Override
    public Account getAccount(String account) {
        return transferDao.queryAccount(account);
    }
}
