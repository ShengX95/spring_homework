package com.sx.utils;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author shengx
 * @date 2020/3/3 15:19
 */
public class CglibProxyService implements MethodInterceptor {

    private Object instance;

    public CglibProxyService(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        TransactionManager transactionManager = TransactionManager.getInstance();
        Object result = null;
        try {
            transactionManager.beginTransaction();
            result = method.invoke(instance, objects);
            transactionManager.commit();
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback();
            throw e;
        } finally {
//            transactionManager.close();
        }
        return result;
    }
}
