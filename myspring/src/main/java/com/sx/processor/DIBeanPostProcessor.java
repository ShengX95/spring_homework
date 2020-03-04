package com.sx.processor;

import com.sx.context.ApplicationContext;
import com.sx.context.BeanDefinition;
import com.sx.context.BeanFactory;
import com.sx.context.PropertyValue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shengx
 * @date 2020/3/3 21:13
 */
public class DIBeanPostProcessor implements BeanPostProcessor {
    BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        ApplicationContext applicationContext = (ApplicationContext) beanFactory;
        Map<String, BeanDefinition> beanDefinitionMap = applicationContext.getBeanDefinitionMap();
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Set<PropertyValue> propertyValueSet = beanDefinition.getPropertyValues();
        for(PropertyValue propertyValue : propertyValueSet){
            // XML方式
            if(!propertyValue.isAutowired()){
                String name = propertyValue.getName();
                String ref = propertyValue.getRef();
                Object diObj = applicationContext.getSingletonBeans().get(ref);
                try {
                    Method method = bean.getClass().getDeclaredMethod("set" + name, diObj.getClass().getInterfaces()[0]);
                    method.invoke(bean, diObj);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            // autowired 方式
            else {
                Class clazz = propertyValue.getClazz();
                String name = propertyValue.getName();
                try {
                    Field field = bean.getClass().getDeclaredField(name);
                    Class fieldType = field.getType();
                    for (HashMap.Entry<String, Object> entry2 : applicationContext.getSingletonBeans().entrySet()) {
                        String key2 = entry2.getKey();
                        Object obj2 = entry2.getValue();
                        if (fieldType.isInstance(obj2)) {
                            field.setAccessible(true);
                            try {
                                field.set(bean, obj2);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
