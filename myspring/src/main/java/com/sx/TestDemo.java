package com.sx;

import com.sx.context.ApplicationContext;
import com.sx.pojo.Account;
import com.sx.pojo.User;
import com.sx.service.ITransferService;
import com.sx.service.IUserService;
import com.sx.service.UserServiceImpl;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/2/28 10:41
 */
public class TestDemo {
    @Test
    public void test(){
        IUserService userService = new UserServiceImpl();
        User u = new User();
        u.setId(6);
        u.setName("555");
        u.setAge(55);
        u.setEmail("222@");
        try {
            System.out.println(userService.getUserById(1));
            userService.insert(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        ApplicationContext context = new ApplicationContext();
        IUserService userService = (IUserService) context.getBean("userService");
        try {
            System.out.println(userService.getUserById(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        ApplicationContext context = new ApplicationContext();
        ITransferService transferService = (ITransferService) context.getBean("transferService");
        Account account1 = transferService.getAccount("123456");
        Account account2 = transferService.getAccount("654321");
        transferService.transferMoney(account2, account1, 100);
    }

    @Test
    public void test4(){
        ApplicationContext context = new ApplicationContext();
        ITransferService transferService = (ITransferService) context.getBean("transferService");
//        s proxySer = (ITransferService) ProxyService.getProxyService(transferService);
        Account account1 = transferService.getAccount("123456");
        Account account2 = transferService.getAccount("654321");
        transferService.transferMoney(account1, account2, 100);
    }
}
