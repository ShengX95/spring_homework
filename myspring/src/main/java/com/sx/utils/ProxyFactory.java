package com.sx.utils;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @author shengx
 * @date 2020/3/3 14:52
 */
public class ProxyFactory {
    private ProxyFactory(){}

    private static class ProxyFactoryHolder{
        public static final ProxyFactory proxy = new ProxyFactory();
    }

    public ProxyFactory getInstance(){
        return ProxyFactoryHolder.proxy;
    }

    public static Object getJdkTransactionProxy(Object instance){
        return Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(), new JdkProxyService(instance));
    }

    public static Object getCglibTransactionProxy(Object instance){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        enhancer.setCallback(new CglibProxyService(instance));
        return enhancer.create();
    }
}
