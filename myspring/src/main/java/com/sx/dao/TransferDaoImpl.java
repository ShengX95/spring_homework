package com.sx.dao;

import com.sx.anno.Repository;
import com.sx.anno.Service;
import com.sx.pojo.Account;
import com.sx.utils.ConnectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/3/1 20:35
 */
@Repository
public class TransferDaoImpl implements ITransferDao {

    @Override
    public void transfer(Account account) {
        Connection conn;
        PreparedStatement preparedStatement;
        try {
            conn = ConnectionUtils.getInstance().getConnction();
            String sql = "UPDATE transfer SET amount=? WHERE account=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, account.getAmount());
            preparedStatement.setObject(2, account.getAccount());
            preparedStatement.execute();
            preparedStatement.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account queryAccount(String account) {
        Connection conn;
        PreparedStatement preparedStatement;
        try {
            conn = ConnectionUtils.getInstance().getConnction();
            String sql = "SELECT * FROM transfer WHERE account=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            Account result = new Account();
            while(resultSet.next()){
                String name = resultSet.getString(1);
                int amount = resultSet.getInt(2);
                String acc = resultSet.getString(3);
                result.setName(name);
                result.setAmount(amount);
                result.setAccount(acc);
            }
            preparedStatement.close();
//            conn.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
