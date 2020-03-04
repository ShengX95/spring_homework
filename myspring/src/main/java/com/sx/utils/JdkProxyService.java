package com.sx.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author shengx
 * @date 2020/3/2 19:27
 */
public class JdkProxyService implements InvocationHandler {
    private Object service;

    public JdkProxyService(Object service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionManager transactionManager = TransactionManager.getInstance();
        Object result = null;
        try {
            transactionManager.beginTransaction();
            result = method.invoke(service, args);
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

    public static Object getProxyService(Object obj){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new JdkProxyService(obj));
    }


}
