package com.sx.processor;

import com.sx.anno.Transactional;
import com.sx.utils.ProxyFactory;

/**
 * @author shengx
 * @date 2020/3/3 14:42
 */
public class TransactionalBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class clazz = bean.getClass();
        if(clazz.getAnnotation(Transactional.class) != null){
            Class[] interfaces = clazz.getInterfaces();
            if(interfaces.length > 0){
                bean = ProxyFactory.getJdkTransactionProxy(bean);
            }else{
                bean = ProxyFactory.getCglibTransactionProxy(bean);
            }
        }
        return bean;
    }
}
